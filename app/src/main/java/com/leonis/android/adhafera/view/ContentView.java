package com.leonis.android.adhafera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class ContentView extends InputView {
    private final EditText fieldView;

    public ContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View.inflate(context, R.layout.content_view, this);

        fieldView = findViewById(R.id.field_content);
        errorChecker = findViewById(R.id.check_content);
    }

    public void setInputText(String text) {
        fieldView.setText(text);
    }

    public String getInputText() {
        return fieldView.getText().toString();
    }
}
