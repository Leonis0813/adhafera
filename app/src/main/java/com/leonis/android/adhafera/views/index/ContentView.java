package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class ContentView extends InputView {
    private final EditText content;
    private final Spinner contentType;

    public ContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_content_view, this);

        content = layout.findViewById(R.id.index_field_content);
        contentType = layout.findViewById(R.id.index_field_content_type);
    }
}
