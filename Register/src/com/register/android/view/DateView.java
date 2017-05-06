package com.register.android.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.register.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DateView extends InputView {
  private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private EditText fieldView;

  public DateView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);

    View layout = View.inflate(context, R.layout.date_view, this);

    fieldView = (EditText) layout.findViewById(R.id.field_date);
    errorChecker = (TextView) layout.findViewById(R.id.check_date);

    setInputText(simpleDateFormat.format(new Date()));
  }

  public void setInputText(String text) {
    fieldView.setText(text);
  }

  public String getInputText() {
    return fieldView.getText().toString();
  }
}