package com.leonis.android.adhafera.views.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class PriceView extends InputView {
    private final EditText fieldView;

    public PriceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View.inflate(context, R.layout.create_price_view, this);

        fieldView = findViewById(R.id.create_field_price);
        errorChecker = findViewById(R.id.create_check_price);
    }

    public void setInputText(String text) {
        fieldView.setText(text);
    }

    public String getInputText() {
        return fieldView.getText().toString();
    }
}
