package com.register.android.view;

import com.register.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ContentView extends InputView {
  public static final String LABEL = "内容";
  private EditText fieldView;

  public ContentView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    
    View.inflate(context, R.layout.content_view, this);
    
    labelView = (TextView) findViewById(R.id.label_content);
    labelView.setText("　　" + LABEL + "：");
    fieldView = (EditText) findViewById(R.id.field_content);
    errorChecker = (TextView) findViewById(R.id.check_content);
  }
  
  public void setInputText(String text) {
    fieldView.setText(text);
  }
  
  public String getInputText() {
    return fieldView.getText().toString();
  }
}
