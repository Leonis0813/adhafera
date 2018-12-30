package com.leonis.android.adhafera.view.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class CategoryView extends IndexView {
    private final Spinner category;

    public CategoryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View layout = View.inflate(context, R.layout.index_category_view, this);

        category = layout.findViewById(R.id.index_field_category);
    }
}
