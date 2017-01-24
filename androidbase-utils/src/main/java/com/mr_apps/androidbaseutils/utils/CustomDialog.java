package com.mr_apps.androidbaseutils.utils;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mr_apps.androidbaseutils.R;


/**
 * Class that contains a lot of useful methods to create standard dialogs
 *
 * @author Mattia Ruggiero
 * @author Denis Brandi
 */
public class CustomDialog {

    public interface EditTextDialogCallback {
        void onPositive(EditText editText);
    }

    public interface StandardDialogCallback {
        void onPositive();
    }

    /**
     * Shows a standard dialog with only a ok button
     *
     * @param context  the context
     * @param title    the title that should be displayed at the top of the dialog
     * @param message  the message of the dialog
     * @param callback the callback to determine the actions that should be done on "Ok" tap
     */
    public static void showOkDialog(Context context, String title, String message, final StandardDialogCallback callback) {
        showOkDialog(context, title, message, null, callback);
    }

    /**
     * Shows a standard dialog with only a ok button
     *
     * @param context    the context
     * @param title      the title that should be displayed at the top of the dialog
     * @param message    the message of the dialog
     * @param buttonText the text of the positive button of the dialog
     * @param callback   the callback to determine the actions that should be done on "Ok" tap
     */
    public static void showOkDialog(Context context, String title, String message, String buttonText, final StandardDialogCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);

        String myButtonText = context.getString(android.R.string.ok);

        if (buttonText != null)
            myButtonText = buttonText;

        builder.setPositiveButton(myButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (callback != null)
                    callback.onPositive();
            }
        });

        builder.show();

    }

    /**
     * Shows a standard dialog with only a ok button
     *
     * @param context      the context
     * @param titleResId   the string's resource id of the title that should be displayed at the top of the dialog
     * @param messageResId the string's resource id of the message of the dialog
     * @param callback     the callback to determine the actions that should be done on "Ok" tap
     */
    public static void showOkDialog(Context context, int titleResId, int messageResId, final StandardDialogCallback callback) {
        showOkDialog(context, titleResId == 0 ? null : context.getString(titleResId), messageResId == 0 ? null : context.getString(messageResId), null, callback);
    }

    /**
     * Shows a standard dialog with only a ok button with the default "Ok" tap's action
     *
     * @param context      the context
     * @param titleResId   the string's resource id of the title that should be displayed at the top of the dialog
     * @param messageResId the string's resource id of the message of the dialog
     */
    public static void showOkDialog(Context context, int titleResId, int messageResId) {
        showOkDialog(context, titleResId == 0 ? null : context.getString(titleResId), messageResId == 0 ? null : context.getString(messageResId), null, null);
    }

    /**
     * Shows a standard dialog with only a ok button with the default "Ok" tap's action
     *
     * @param context the context
     * @param title   the title that should be displayed at the top of the dialog
     * @param message the message of the dialog
     */
    public static void showOkDialog(Context context, String title, String message) {
        showOkDialog(context, title, message, null, null);
    }

    /**
     * Shows a standard dialog with only a positive button with a customizable tap's action
     *
     * @param context    the context
     * @param title      the title that should be displayed at the top of the dialog
     * @param message    the message of the dialog
     * @param buttonText the text of the positive button of the dialog
     */
    public static void showOkDialog(Context context, String title, String message, String buttonText) {
        showOkDialog(context, title, message, buttonText, null);
    }

    /**
     * Shows a standard dialog with only a positive button with a customizable tap's action
     *
     * @param context         the context
     * @param titleResId      the string's resource id of the title that should be displayed at the top of the dialog
     * @param messageResId    the string's resource id of the message of the dialog
     * @param buttonTextResId the string's resource id  of the text of the positive button of the dialog
     */
    public static void showOkDialog(Context context, int titleResId, int messageResId, int buttonTextResId) {
        showOkDialog(context, titleResId == 0 ? null : context.getString(titleResId), messageResId == 0 ? null : context.getString(messageResId), context.getString(buttonTextResId), null);
    }

    /**
     * Shows a dialog with two possible actions, "yes" or "no"
     *
     * @param context      the context
     * @param title        the title that should be displayed at the top of the dialog
     * @param message      the message of the dialog
     * @param positiveText a text to show on the "positive" button of the dialog. A null value sets the button text to "ok"
     * @param negativeText a text to show on the "negative" button of the dialog. A null value sets the button text to "cancel"
     * @param callback     the callback to determine the actions that should be done on "yes" tap (on "no" tap there's a simple dialog dismiss)
     */
    public static void showYesNoDialog(Context context, String title, String message, String positiveText, String negativeText, final StandardDialogCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeText == null ? context.getString(android.R.string.no) : negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(positiveText == null ? context.getString(android.R.string.yes) : positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive();
                    }
                })
                .show();
    }

    /**
     * Shows a dialog with two possible actions, "yes" or "no"
     *
     * @param context       the context
     * @param title         the title that should be displayed at the top of the dialog
     * @param message       the message of the dialog
     * @param positiveResId the resource id of the string to show on the "positive" button of the dialog. A 0 value sets the button text to "ok"
     * @param negativeResId the resource id of the string to show on the "negative" button of the dialog. A 0 value sets the button text to "cancel"
     * @param callback      the callback to determine the actions that should be done on "yes" tap (on "no" tap there's a simple dialog dismiss)
     */
    public static void showYesNoDialog(Context context, String title, String message, int positiveResId, int negativeResId, final StandardDialogCallback callback) {
        showYesNoDialog(context, title, message, positiveResId == 0 ? null : context.getString(positiveResId), negativeResId == 0 ? null : context.getString(negativeResId), callback);
    }

    /**
     * Shows a dialog with two possible actions, "yes" or "no"
     *
     * @param context  the context
     * @param title    the title that should be displayed at the top of the dialog
     * @param message  the message of the dialog
     * @param callback the callback to determine the actions that should be done on "yes" tap (on "no" tap there's a simple dialog dismiss)
     */
    public static void showYesNoDialog(Context context, String title, String message, final StandardDialogCallback callback) {
        showYesNoDialog(context, title, message, null, null, callback);
    }

    /**
     * Shows a dialog with two possible actions, "yes" or "no"
     *
     * @param context      the context
     * @param titleResId   the string's resource id of the title that should be displayed at the top of the dialog
     * @param messageResId the string's resource id of the message of the dialog
     * @param callback     the callback to determine the actions that should be done on "yes" tap
     */
    public static void showYesNoDialog(Context context, int titleResId, int messageResId, final StandardDialogCallback callback) {
        showYesNoDialog(context, titleResId == 0 ? null : context.getString(titleResId), titleResId == 0 ? null : context.getString(messageResId), null, null, callback);
    }

    /**
     * Shows a dialog with two possible actions, "yes" or "no"
     *
     * @param context       the context
     * @param titleResId    the string's resource id of the title that should be displayed at the top of the dialog
     * @param messageResId  the string's resource id of the message of the dialog
     * @param positiveResId the resource id of the string to show on the "positive" button of the dialog. A 0 value sets the button text to "ok"
     * @param negativeResId the resource id of the string to show on the "negative" button of the dialog. A 0 value sets the button text to "cancel"
     * @param callback      the callback to determine the actions that should be done on "yes" tap
     */
    public static void showYesNoDialog(Context context, int titleResId, int messageResId, int positiveResId, int negativeResId, final StandardDialogCallback callback) {
        showYesNoDialog(context, titleResId == 0 ? null : context.getString(titleResId), titleResId == 0 ? null : context.getString(messageResId),
                positiveResId == 0 ? null : context.getString(positiveResId), negativeResId == 0 ? null : context.getString(negativeResId), callback);
    }

    /**
     * Shows a dialog with an edit text, with all the given parameters to configure it
     *
     * @param context   the context
     * @param titleId   the string's resource id of the title that should be displayed at the top of the dialog
     * @param hintId    the string's resource id of the hint that should be displayed in the edit text. A null value removes the hint from the edit text
     * @param messageId the string's resource id of the message of the dialog, placed above the edit text. A null value removes the message from the dialog
     * @param inputType the input type of the edit text
     * @param lower     lower bound of the edit text, to show an error message if the value of the edit text is lower. A null value removes the controls
     * @param upper     upper bound of the edit text, to show an error message if the value of the edit text is higher. A null value removes the controls
     * @param callback  the callback to determine the actions that should be done on "ok" tap
     */
    public static void showEditDialog(Context context, int titleId, int hintId, int messageId, int inputType, String lower, String upper, final EditTextDialogCallback callback) {
        showEditDialog(context, titleId == 0 ? null : context.getString(titleId), hintId == 0 ? null : context.getString(hintId), messageId == 0 ? null : context.getString(messageId), inputType, lower, upper, callback);
    }

    /**
     * Shows a dialog with an edit text, with all the given parameters to configure it
     *
     * @param context   the context
     * @param title     the title that should be displayed at the top of the dialog
     * @param hint      the hint that should be displayed in the edit text. A null value removes the hint from the edit text
     * @param message   the message of the dialog, placed above the edit text. A null value removes the message from the dialog
     * @param inputType the input type of the edit text
     * @param lower     lower bound of the edit text, to show an error message if the value of the edit text is lower. A null value removes the controls
     * @param upper     upper bound of the edit text, to show an error message if the value of the edit text is higher. A null value removes the controls
     * @param callback  the callback to determine the actions that should be done on "ok" tap
     */
    public static void showEditDialog(final Context context, String title, String hint, String message, int inputType, final String lower, final String upper, final EditTextDialogCallback callback) {
        showEditDialog(context, title, hint, message, null, inputType, lower, upper, null, null, callback);
    }

    /**
     * Shows a dialog with an edit text, with all the given parameters to configure it
     *
     * @param context         the context
     * @param title           the title that should be displayed at the top of the dialog
     * @param hint            the hint that should be displayed in the edit text. A null value removes the hint from the edit text
     * @param message         the message of the dialog, placed above the edit text. A null value removes the message from the dialog
     * @param precompiledText the text to set to the edit when created
     * @param inputType       the input type of the edit text
     * @param lower           lower bound of the edit text, to show an error message if the value of the edit text is lower. A null value removes the controls
     * @param upper           upper bound of the edit text, to show an error message if the value of the edit text is higher. A null value removes the controls
     * @param callback        the callback to determine the actions that should be done on "ok" tap
     */
    public static void showEditDialog(final Context context, String title, String hint, String message, String precompiledText, int inputType, final String lower, final String upper, final EditTextDialogCallback callback) {
        showEditDialog(context, title, hint, message, precompiledText, inputType, lower, upper, null, null, callback);
    }

    /**
     * Shows a dialog with an edit text, with all the given parameters to configure it
     *
     * @param context         the context
     * @param title           the title that should be displayed at the top of the dialog
     * @param hint            the hint that should be displayed in the edit text. A null value removes the hint from the edit text
     * @param message         the message of the dialog, placed above the edit text. A null value removes the message from the dialog
     * @param precompiledText the text to set to the edit when created
     * @param inputType       the input type of the edit text
     * @param lower           lower bound of the edit text, to show an error message if the value of the edit text is lower. A null value removes the controls
     * @param upper           upper bound of the edit text, to show an error message if the value of the edit text is higher. A null value removes the controls
     * @param positiveResId   the resource id of the string to show on the "positive" button of the dialog. A 0 value sets the button text to "ok"
     * @param negativeResId   the resource id of the string to show on the "negative" button of the dialog. A 0 value sets the button text to "cancel"
     * @param callback        the callback to determine the actions that should be done on "ok" tap
     */
    public static void showEditDialog(final Context context, String title, String hint, String message, String precompiledText, int inputType, final String lower, final String upper, int positiveResId, int negativeResId, final EditTextDialogCallback callback) {
        showEditDialog(context, title, hint, message, precompiledText, inputType, lower, upper, positiveResId == 0 ? null : context.getString(positiveResId), negativeResId == 0 ? null : context.getString(negativeResId), callback);
    }

    /**
     * Shows a dialog with an edit text, with all the given parameters to configure it
     *
     * @param context         the context
     * @param title           the title that should be displayed at the top of the dialog
     * @param hint            the hint that should be displayed in the edit text. A null value removes the hint from the edit text
     * @param message         the message of the dialog, placed above the edit text. A null value removes the message from the dialog
     * @param precompiledText the text to set to the edit when created
     * @param inputType       the input type of the edit text
     * @param lower           lower bound of the edit text, to show an error message if the value of the edit text is lower. A null value removes the controls
     * @param upper           upper bound of the edit text, to show an error message if the value of the edit text is higher. A null value removes the controls
     * @param positiveText    a text to show on the "positive" button of the dialog. A null value sets the button text to "ok"
     * @param negativeText    a text to show on the "negative" button of the dialog. A null value sets the button text to "cancel"
     * @param callback        the callback to determine the actions that should be done on "ok" tap
     */
    public static void showEditDialog(final Context context, String title, String hint, String message, String precompiledText, int inputType, final String lower, final String upper, final String positiveText, final String negativeText, final EditTextDialogCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);

        final TextView textView = (TextView) view.findViewById(R.id.message);

        final EditText edit = (EditText) view.findViewById(R.id.edit_dialog);

        final TextInputLayout til_valore = (TextInputLayout) view.findViewById(R.id.til_valore);

        textView.setVisibility(message == null ? View.GONE : View.VISIBLE);
        textView.setText(message == null ? "" : message);

        edit.setInputType(inputType);
        edit.setHint(hint == null ? "" : hint);

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

                if (lower != null && upper != null && s.length() > 0) {
                    double value = Double.parseDouble(s.toString());

                    double lowerValue = Double.parseDouble(lower);
                    double upperValue = Double.parseDouble(upper);

                    if (value < lowerValue || value > upperValue) {
                        til_valore.setError(context.getString(R.string.Devi_inserire_un_valore_tra__d_e__d, (int) lowerValue, (int) upperValue));
                    } else {
                        til_valore.setError(null);
                    }

                } else {
                    til_valore.setError(null);
                }

            }
        });

        if (precompiledText != null)
            edit.setText(precompiledText);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setNegativeButton(negativeText == null ? context.getString(android.R.string.cancel) : negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(positiveText == null ? context.getString(android.R.string.ok) : positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive(edit);
                    }
                })
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    /**
     * Shows a progress dialog
     *
     * @param context   the context
     * @param titleId   the string's resource id of the title of the progress dialog
     * @param messageId the string's resource id of the loading message of the dialog
     * @return the progressDialog displayed
     */
    public static ProgressDialog show(Context context, int titleId, int messageId) {
        return show(context, titleId != 0 ? context.getString(titleId) : null, messageId != 0 ? context.getString(messageId) : null);
    }

    /**
     * Shows a progress dialog
     *
     * @param context the context
     * @param title   the title of the progress dialog
     * @param message the loading message of the dialog
     * @return the progressDialog displayed
     */
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
        else
            dialog.setContentView(new ProgressBar(context));

        return dialog;

    }

    /**
     * Dismisses the given progress dialog
     *
     * @param dialog the progress dialog that has to be dismissed
     */
    public static void dismiss(ProgressDialog dialog) {
        try {

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
