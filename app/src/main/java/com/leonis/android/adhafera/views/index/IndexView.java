package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/12/30.
 */

public class IndexView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_PERIOD = 0;
    public static final int INPUT_VIEW_CONTENT = 1;
    public static final int INPUT_VIEW_CATEGORY = 2;
    public static final int INPUT_VIEW_PRICE = 3;
    private static final int INPUT_VIEW_SIZE = 4;
    private final InputView[] inputViews;
    private final Spinner paymentType;
    private final Button submit;
    private ListView results;
    private final Button nextPage;
    private int currentPage;

    private final Context context;

    public IndexView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.index_view, this);

        inputViews = new InputView[INPUT_VIEW_SIZE];
        inputViews[INPUT_VIEW_PERIOD] = layout.findViewById(R.id.index_period);
        inputViews[INPUT_VIEW_CONTENT] = layout.findViewById(R.id.index_content);
        inputViews[INPUT_VIEW_CATEGORY] = layout.findViewById(R.id.index_category);
        inputViews[INPUT_VIEW_PRICE] = layout.findViewById(R.id.index_price);

        paymentType = layout.findViewById(R.id.index_payment_type);
        submit = layout.findViewById(R.id.index_submit);
        nextPage = layout.findViewById(R.id.index_next_page);

        results = new ListView(context);
        currentPage = 0;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void setCategories(String[] names) {
        ((CategoryView) inputViews[INPUT_VIEW_CATEGORY]).setCategories(names);
    }

    @Override
    public void onClick(View v) {
        if(v == submit) {
        } else if(v == nextPage) {
        }
    }
}
