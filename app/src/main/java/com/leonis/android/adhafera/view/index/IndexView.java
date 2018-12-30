package com.leonis.android.adhafera.view.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.leonis.android.adhafera.R;
import com.leonis.android.adhafera.view.create.InputView;

/**
 * Created by leonis on 2018/12/30.
 */

public class IndexView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_DATE_BEFORE = 0;
    public static final int INPUT_VIEW_DATE_AFTER = 1;
    public static final int INPUT_VIEW_CONTENT = 2;
    public static final int INPUT_VIEW_CATEGORY = 3;
    public static final int INPUT_VIEW_PRICE_UPPER = 4;
    public static final int INPUT_VIEW_PRICE_LOWER = 5;
    private static final int INPUT_VIEW_SIZE = 6;
    private final InputView[] inputViews;
//    private final Spinner contentType;
//    private final Spinner paymentType;
//    private final Button submit;
    private ListView results;
//    private final Button nextPage;
    private int currentPage;

    private final Context context;

    public IndexView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.index_view, this);

        inputViews = new InputView[INPUT_VIEW_SIZE];
        /*
        inputViews[INPUT_VIEW_DATE_BEFORE] = layout.findViewById(R.id.date_before);
        inputViews[INPUT_VIEW_DATE_AFTER] = layout.findViewById(R.id.date_after);
        inputViews[INPUT_VIEW_CONTENT] = layout.findViewById(R.id.content);
        inputViews[INPUT_VIEW_CATEGORY] = layout.findViewById(R.id.category);
        inputViews[INPUT_VIEW_PRICE_UPPER] = layout.findViewById(R.id.price_upper);
        inputViews[INPUT_VIEW_PRICE_LOWER] = layout.findViewById(R.id.price_lower);

        contentType = layout.findViewById(R.id.content_type);
        paymentType = layout.findViewById(R.id.payment_type);
        submit = layout.findViewById(R.id.submit);
        nextPage = layout.findViewById(R.id.next_page);
*/
        results = new ListView(context);
        currentPage = 0;
    }

    @Override
    public void onClick(View v) {
//        if(v == submit) {
//        } else if(v == nextPage) {
//        }
    }
}
