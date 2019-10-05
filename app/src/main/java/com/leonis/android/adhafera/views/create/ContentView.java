package com.leonis.android.adhafera.views.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.leonis.android.adhafera.MainActivity;
import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class ContentView extends InputView implements OnFocusChangeListener {
    private final EditText fieldView;

    public ContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View.inflate(context, R.layout.create_content_view, this);

        fieldView = findViewById(R.id.create_field_content);
        errorChecker = findViewById(R.id.create_check_content);
    }

    public void setInputText(String text) {
        fieldView.setText(text);
    }

    public String getInputText() {
        return fieldView.getText().toString();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(!hasFocus) {
            ((MainActivity) context).getDictionaries(fieldView.getText().toString());
        }
    }
}
