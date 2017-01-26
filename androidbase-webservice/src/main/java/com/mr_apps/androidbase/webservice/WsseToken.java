package com.mr_apps.androidbase.webservice;

import android.content.Context;
import android.util.Base64;

import com.mr_apps.androidbase.preferences.SecurityPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Class that manages the WsseToken for the web service
 *
 * @author Denis Brandi
 */
public class WsseToken {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_WSSE = "X-WSSE";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    //in our case, User is an entity (just a POJO) persisted into sqlite database
    private String user;
    private String password;
    private String nonce;
    private String createdAt;
    private String digest;

    /**
     * Constructor that takes the context
     *
     * @param context the context
     */
    public WsseToken(Context context) {
        //we need the user object because we need his username
        this.user = SecurityPreferences.getUser(context);
        this.password = SecurityPreferences.getToken(context);
        this.createdAt = generateTimestamp();
        this.nonce = generateNonce();
        this.digest = generateDigest();
    }

    /**
     * Generates the random key for the WSSE
     *
     * @return the random key for the WSSE
     */
    private String generateNonce() {
        SecureRandom random = new SecureRandom();
        byte seed[] = random.generateSeed(10);
        return bytesToHex(seed);
    }

    /**
     * Generates a random key, and encodes it with the MD5 algorithm
     *
     * @return the random key encoded with MD5
     */
    private String myNonce() {
        long ts = (new Date()).getTime();

        Random random = new Random();
        int rand = random.nextInt(999999 - 1) + 1;

        String nonce = BaseLoopJSecurity.MD5(String.valueOf(rand) + String.valueOf(ts));

        return nonce;
    }

    /**
     * Utility method to convert an array of bytes in hexadecimal values
     *
     * @param bytes the array of bytes to convert
     * @return the bytes array converted in hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Generates a time stamp
     *
     * @return a time stamp
     */
    private String generateTimestamp() {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    /**
     * Generates the digest for the WSSE Token
     *
     * @return the digest of the WSEE Token
     */
    private String generateDigest() {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            StringBuilder sb = new StringBuilder();
            sb.append(this.nonce);
            sb.append(this.createdAt);
            sb.append(this.password);
            byte sha[] = md.digest(sb.toString().getBytes());
            digest = Base64.encodeToString(sha, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    /**
     * Gets the header of the WSSE Token
     *
     * @return the header of the WSSE
     */
    public String getWsseHeader() {
        StringBuilder header = new StringBuilder();
        header.append("UsernameToken Username=\"");
        header.append(this.user);
        header.append("\", PasswordDigest=\"");
        header.append(this.digest);
        header.append("\", Nonce=\"");
        header.append(Base64.encodeToString(this.nonce.getBytes(), Base64.NO_WRAP));
        header.append("\", Created=\"");
        header.append(this.createdAt);
        header.append("\"");

        //Logger.d("wsse", header.toString());
        return header.toString();
    }

    /**
     * Gets the authorization header of the WSSE Token
     *
     * @return the authorization header of the WSSE
     */
    public String getAuthorizationHeader() {
        return "WSSE profile=\"UsernameToken\"";
    }

}
