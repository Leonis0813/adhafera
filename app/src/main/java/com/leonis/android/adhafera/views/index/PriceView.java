package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class PriceView extends InputView {
    private final EditText priceUpper;
    private final EditText priceLower;

    public PriceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_price_view, this);

        priceUpper = layout.findViewById(R.id.index_field_price_upper);
        priceLower = layout.findViewById(R.id.index_field_price_lower);
        errorChecker = layout.findViewById(R.id.index_check_price);
    }
}
