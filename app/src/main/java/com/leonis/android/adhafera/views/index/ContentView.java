package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class ContentView extends LinearLayout {
    private final EditText content;
    private final Spinner contentType;
    public final static String[] CONTENT_TYPE_KEYS = {"content_include", "content_equal"};

    public ContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_content_view, this);

        content = layout.findViewById(R.id.index_field_content);
        contentType = layout.findViewById(R.id.index_field_content_type);
    }

    public String getContent() {
        return content.getText().toString();
    }

    public String getContentType() {
        return contentType.getSelectedItem().toString();
    }
}
