package com.mr_apps.androidbase.account;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.mr_apps.androidbase.R;
import com.mr_apps.androidbase.utils.CustomDialog;
import com.mr_apps.androidbase.utils.Utils;

/**
 * Created by mattia on 18/04/2016.
 *
 * @author Mattia Ruggiero
 */
public class SignUpElement {

    Context context;

    private ElementName name;
    private ElementInputType inputType;
    private View view;

    private boolean required;

    public SignUpElement(Context context, ElementName name, ElementInputType inputType, boolean required) {
        this.context = context;
        this.name = name;
        this.inputType = inputType;
        this.required = required;
    }

    public ElementName getName() {
        return name;
    }

    public ElementInputType getInputType() {
        return inputType;
    }

    public View getView() {
        if (view != null) {
            return view;
        } else {
            return createView();
        }
    }

    private View createView() {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (inputType.equals(ElementInputType.STANDARD) || inputType.equals(ElementInputType.MAIL) || inputType.equals(ElementInputType.PHONE)) {
            view = inflater.inflate(R.layout.standard_element, null);

            final TextInputLayout tilElement = (TextInputLayout) view.findViewById(R.id.til_name);

            if (tilElement != null) {
                tilElement.setHint(this.name.toString());

                TextInputEditText element = (TextInputEditText) view.findViewById(R.id.standard_name);
                if (element != null) {
                    if (inputType.equals(ElementInputType.PHONE)) {
                        element.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (inputType.equals(ElementInputType.STANDARD)) {
                        element.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    }

                    if (this.name.equals(ElementName.EMAIL) || this.name.equals(ElementName.TELEFONO)) {
                        element.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                tilElement.setErrorEnabled(name.equals(ElementName.EMAIL) ? !Utils.isValidEmail(s) && s.length() > 0 : s.length() < 6 && s.length() > 0);
                            }
                        });

                        element.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tilElement.setErrorEnabled(tilElement.isErrorEnabled());
                                    }
                                }, 100);
                            }
                        });

                        element.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus)
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tilElement.setErrorEnabled(tilElement.isErrorEnabled());
                                        }
                                    }, 100);
                            }
                        });
                    }
                }
            }

        } else if (inputType.equals(ElementInputType.SWITCH)) {
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

    /**
     * Consente di aggiungere un listener all'elemento SOLO se è di tipo SUBSECTION
     *
     * @param listener il listener da aggiungere alla view
     */
    public void addOnClickListener(View.OnClickListener listener) {
        if (inputType.equals(ElementInputType.SUBSECTION)) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.subsection_element, null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog.showOkDialog(context, "Nazione", "Trinidad & Tobago\nAntartide\nNegrolandia");
                }
            });
        }
    }

    public boolean isRequired() {
        return required;
    }
}
