package com.mr_apps.androidbase.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.mr_apps.androidbase.R;


/**
 * Created by denis on 04/02/16
 */
public class CustomDialog {

    public interface CustomDialogCallback {

        void onPositive(EditText editText);

    }

    public static void showOkDialog(Context context, int titleResId, int messageResId) {

        showOkDialog(context, context.getString(titleResId), context.getString(messageResId));
    }

    public static void showOkDialog(Context context, String title, String message) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showEditDialog(Context context, int titleId, int hintId, int inputType, String lower, String upper, final CustomDialogCallback callback) {
        showEditDialog(context, context.getString(titleId), context.getString(hintId), inputType, lower, upper, callback);
    }

    public static void showEditDialog(final Context context, String title, String hint, int inputType, final String lower, final String upper, final CustomDialogCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);

        final EditText edit = (EditText) view.findViewById(R.id.edit_dialog);

        final TextInputLayout til_valore = (TextInputLayout) view.findViewById(R.id.til_valore);

        edit.setInputType(inputType);

        til_valore.setErrorEnabled(false);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(lower!=null && upper!=null && s.length()>0)
                {
                    double value=Double.parseDouble(s.toString());

                    double lowerValue=Double.parseDouble(lower);
                    double upperValue=Double.parseDouble(upper);

                    if(value<lowerValue || value>upperValue)
                    {
                        til_valore.setError(context.getString(R.string.Devi_inserire_un_valore_tra__d_e__d, (int) lowerValue, (int) upperValue));
                        til_valore.setErrorEnabled(true);
                    }
                    else
                    {
                        til_valore.setErrorEnabled(false);
                    }

                }
                else
                {
                    til_valore.setErrorEnabled(false);
                }

            }
        });

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive(edit);
                    }
                })
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public static ProgressDialog show(Context context, int titleId, int messageId) {
        return show(context, titleId != 0 ? context.getString(titleId) : null, messageId != 0 ? context.getString(messageId) : null);
    }

    public static ProgressDialog show(Context context, String title, String message) {
        ProgressDialog dialog = new ProgressDialog(context);

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.setCancelable(false);
        dialog.setIndeterminate(true);

        if (title != null)
            dialog.setTitle(title);

        if (message != null)
            dialog.setMessage(message);

        return dialog;

    }

    public static void dismiss(ProgressDialog dialog) {
        try {

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
