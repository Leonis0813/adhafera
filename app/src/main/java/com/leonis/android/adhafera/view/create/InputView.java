package com.leonis.android.adhafera.view.create;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public abstract class InputView extends LinearLayout {
    protected final Context context;
    public TextView errorChecker;

    public InputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.context = context;
    }

    abstract public void setInputText(String text);
    abstract public String getInputText();
}
