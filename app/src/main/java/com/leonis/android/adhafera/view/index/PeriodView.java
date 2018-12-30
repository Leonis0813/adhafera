package com.leonis.android.adhafera.view.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class PeriodView extends InputView {
    private final EditText dateBefore;
    private final EditText dateAfter;

    public PeriodView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_period_view, this);

        dateBefore = layout.findViewById(R.id.index_field_date_before);
        dateAfter = layout.findViewById(R.id.index_field_date_after);
        errorChecker = layout.findViewById(R.id.index_check_period);
    }
}
