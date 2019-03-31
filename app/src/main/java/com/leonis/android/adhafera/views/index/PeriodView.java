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

public class PeriodView extends LinearLayout {
    private final EditText dateBefore;
    private final EditText dateAfter;
    private final TextView errorChecker;

    public PeriodView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_period_view, this);

        dateBefore = layout.findViewById(R.id.index_field_date_before);
        dateAfter = layout.findViewById(R.id.index_field_date_after);
        errorChecker = layout.findViewById(R.id.index_check_period);
    }

    public String getDateBefore() {
        return dateBefore.getText().toString();
    }

    public String getDateAfter() {
        return dateAfter.getText().toString();
    }

    public void checkError() {
        errorChecker.setVisibility(VISIBLE);
    }

    public void uncheckError() {
        errorChecker.setVisibility(INVISIBLE);
    }
}
