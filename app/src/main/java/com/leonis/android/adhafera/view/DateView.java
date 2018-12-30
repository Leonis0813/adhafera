package com.leonis.android.adhafera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.leonis.android.adhafera.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class DateView extends InputView {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
    private final EditText fieldView;

    public DateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.create_date_view, this);

        fieldView = layout.findViewById(R.id.field_date);
        errorChecker = layout.findViewById(R.id.check_date);

        setInputText(simpleDateFormat.format(new Date()));
    }

    public void setInputText(String text) {
        fieldView.setText(text);
    }

    public String getInputText() {
        return fieldView.getText().toString();
    }
}
