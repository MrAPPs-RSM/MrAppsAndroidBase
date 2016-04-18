package com.mr_apps.androidbase.account;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import com.mr_apps.androidbase.R;

/**
 * Created by mattia on 18/04/2016.
 *
 * @author Mattia Ruggiero
 */
public class SignUpElement {

    Context context;

    private ElementName name;
    private ElementInputType inputType;

    public SignUpElement(Context context, ElementName name, ElementInputType inputType) {
        this.context = context;
        this.name = name;
        this.inputType = inputType;
    }

    public View getView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if (inputType.equals(ElementInputType.STANDARD) || inputType.equals(ElementInputType.MAIL) || inputType.equals(ElementInputType.PHONE)) {
            view = inflater.inflate(R.layout.standard_element, null);

            TextInputLayout tilName = (TextInputLayout) view.findViewById(R.id.til_name);
            if (tilName != null)
                tilName.setHint(this.name.toString());

            TextInputEditText name = (TextInputEditText) view.findViewById(R.id.standard_name);
            if (name != null) {
                if (inputType.equals(ElementInputType.PHONE)) {
                    name.setInputType(InputType.TYPE_CLASS_PHONE);
                } else if (inputType.equals(ElementInputType.STANDARD)) {
                    name.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                }
            }

        } else if (inputType.equals(ElementInputType.SWITCH)){
            view = inflater.inflate(R.layout.switch_element, null);

            SwitchCompat name = (SwitchCompat) view.findViewById(R.id.switch_name);
            if (name != null) {
                name.setText(this.name.toString());
            }
        } else {
            view = inflater.inflate(R.layout.subsection_element, null);

            AppCompatTextView name = (AppCompatTextView) view.findViewById(R.id.subsection_name);
            if (name != null) {
                name.setText(this.name.toString());
            }
        }

        return view;
    }
}
