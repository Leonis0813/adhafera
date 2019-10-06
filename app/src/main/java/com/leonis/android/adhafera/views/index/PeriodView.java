package com.leonis.android.adhafera.views.index;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leonis.android.adhafera.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by leonis on 2018/12/30.
 */

public class PeriodView extends LinearLayout implements OnTouchListener {
    private final EditText dateBefore;
    private final EditText dateAfter;
    private final TextView errorChecker;

    private final Context context;

    public PeriodView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.index_period_view, this);

        dateBefore = layout.findViewById(R.id.index_field_date_before);
        dateBefore.setOnTouchListener(this);

        dateAfter = layout.findViewById(R.id.index_field_date_after);
        dateAfter.setOnTouchListener(this);

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

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            final Calendar date = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String text = String.format(Locale.JAPAN, "%d-%02d-%02d", year, month + 1, dayOfMonth);
                            ((EditText) v).setText(text);
                        }
                    },
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DATE)
            );
            datePickerDialog.show();
        }
        return false;
    }
}
