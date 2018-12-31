package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.leonis.android.adhafera.IndexActivity;
import com.leonis.android.adhafera.R;
import com.leonis.android.adhafera.models.Payment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leonis on 2018/12/30.
 */

public class IndexView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_PERIOD = 0;
    public static final int INPUT_VIEW_CONTENT = 1;
    public static final int INPUT_VIEW_CATEGORY = 2;
    public static final int INPUT_VIEW_PRICE = 3;
    private static final int INPUT_VIEW_SIZE = 4;
    private final PeriodView periodView;
    private final ContentView contentView;
    private final CategoryView categoryView;
    private final PriceView priceView;
    private final Spinner paymentType;
    private final static String[] PAYMENT_TYPE_VALUES = {"income", "expense"};
    private final Button submit;
    private PaymentListAdapter paymentListAdapter;
    private ListView paymentListView;
    private ArrayList<Payment> payments;
    private final Button nextPage;
    private int currentPage;

    private final Context context;

    public IndexView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.index_view, this);

        periodView = layout.findViewById(R.id.index_period);
        contentView = layout.findViewById(R.id.index_content);
        categoryView = layout.findViewById(R.id.index_category);
        priceView = layout.findViewById(R.id.index_price);
        paymentType = layout.findViewById(R.id.index_payment_type);
        submit = layout.findViewById(R.id.index_submit);
        submit.setOnClickListener(this);
        nextPage = layout.findViewById(R.id.index_next_page);
        nextPage.setOnClickListener(this);

        payments = new ArrayList<>();
        paymentListAdapter = new PaymentListAdapter(context, payments);
        paymentListView = layout.findViewById(R.id.index_payment_list);

        currentPage = 0;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void setCategories(String[] names) {
        categoryView.setCategories(names);
    }

    public void addPayments(ArrayList<Payment> payments) {
        for(Payment payment : payments) {
            this.payments.add(payment);
        }
        paymentListView.setAdapter(paymentListAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v == submit) {
            HashMap<String, String> query = new HashMap<>();
            if(!periodView.getDateBefore().isEmpty()) {
                query.put("date_before", periodView.getDateBefore());
            }
            if(!periodView.getDateAfter().isEmpty()) {
                query.put("date_after", periodView.getDateAfter());
            }
            if(!contentView.getContent().isEmpty()) {
                String[] contentTypes = getResources().getStringArray(R.array.content_types);
                for(int i = 1;i < contentTypes.length;i++) {
                    if (contentView.getContentType().equals(contentTypes[i])) {
                        query.put(ContentView.CONTENT_TYPE_KEYS[i-1], contentView.getContent());
                    }
                }
            }
            if(!categoryView.getCategory().isEmpty()) {
                query.put("category", categoryView.getCategory());
            }
            if(!priceView.getPriceUpper().isEmpty()) {
                query.put("price_upper", priceView.getPriceUpper());
            }
            if(!priceView.getPriceLower().isEmpty()) {
                query.put("price_lower", priceView.getPriceLower());
            }
            if(!paymentType.getSelectedItem().toString().isEmpty()) {
                String[] paymentTypes = getResources().getStringArray(R.array.payment_types);
                for(int i = 1;i < paymentTypes.length;i++) {
                    if (paymentType.getSelectedItem().toString().equals(paymentTypes[i])) {
                        query.put("payment_type", PAYMENT_TYPE_VALUES[i-1]);
                    }
                }
            }
            Log.d("query", query.toString());
            ((IndexActivity) context).searchPayments(query);
        } else if(v == nextPage) {
        }
    }
}
