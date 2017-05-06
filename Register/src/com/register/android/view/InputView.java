package com.register.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class InputView extends LinearLayout {
  protected Context context;
  protected TextView labelView;
  protected TextView errorChecker;
  
  public InputView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    
    this.context = context;
  }
  
  abstract public void setInputText(String text);
  abstract public String getInputText();
}
