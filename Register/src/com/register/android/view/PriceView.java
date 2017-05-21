package com.register.android.view;

import com.register.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PriceView extends InputView {
  private EditText fieldView;

  public PriceView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);

    View.inflate(context, R.layout.price_view, this);

    fieldView = (EditText) findViewById(R.id.field_price);
    errorChecker = (TextView) findViewById(R.id.check_price);
  }

  public void setInputText(String text) {
    fieldView.setText(text);
  }

  public String getInputText() {
    return fieldView.getText().toString();
  }
}
