package c.iglesias.creditsimulator.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class CustomTextWatcher implements TextWatcher {
    View v;

    public CustomTextWatcher(View v) {
        this.v = v;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (v instanceof EditText) {
            ((EditText) v).removeTextChangedListener(this);
        } else if (v instanceof TextView) {
            ((TextView) v).removeTextChangedListener(this);
        }


        try {
            String originalString = s.toString();

            Long longval;
            if (originalString.contains(",")) {
                originalString = originalString.replaceAll(",", "");
            }
            longval = Long.parseLong(originalString);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            String formattedString = formatter.format(longval);
            if (v instanceof EditText) {
                ((EditText) v).setText(formattedString);
                ((EditText) v).setSelection(((EditText) v).getText().length());
            } else if (v instanceof TextView) {
                ((TextView) v).setText(formattedString);
            }

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if (v instanceof EditText) {
            ((EditText) v).addTextChangedListener(this);
        } else if (v instanceof TextView) {
            ((TextView) v).addTextChangedListener(this);
        }

    }
}

