package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class PriceView extends LinearLayout {
    private final EditText priceUpper;
    private final EditText priceLower;
    private final TextView errorChecker;

    public PriceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_price_view, this);

        priceUpper = layout.findViewById(R.id.index_field_price_upper);
        priceLower = layout.findViewById(R.id.index_field_price_lower);
        errorChecker = layout.findViewById(R.id.index_check_price);
    }

    public String getPriceUpper() {
        return priceUpper.getText().toString();
    }

    public String getPriceLower() {
        return priceLower.getText().toString();
    }

    public void checkError() {
        errorChecker.setVisibility(VISIBLE);
    }

    public void uncheckError() {
        errorChecker.setVisibility(INVISIBLE);
    }
}
