package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
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
    private HashMap<String, String> query;
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
        nextPage.setVisibility(INVISIBLE);

        payments = new ArrayList<>();
        paymentListAdapter = new PaymentListAdapter(context, payments);
        paymentListView = layout.findViewById(R.id.index_payment_list);

        currentPage = 1;
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showWrongInput(String field) {
        if(field.equals(getResources().getString(R.string.period))) {
            periodView.checkError();
        } else if (field.equals(getResources().getString(R.string.price))) {
            priceView.checkError();
        }
    }

    public void setCategories(String[] names) {
        categoryView.setCategories(names);
    }

    public void addPayments(ArrayList<Payment> payments) {
        for(Payment payment : payments) {
            this.payments.add(payment);
        }
        paymentListView.setAdapter(paymentListAdapter);
        fixListViewHeight(paymentListView);
        nextPage.setVisibility(payments.isEmpty() ? INVISIBLE : VISIBLE);
    }

    private void fixListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();

        for (int i = 0;i < itemCount;i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() - 50;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if(v == submit) {
            periodView.uncheckError();
            priceView.uncheckError();

            query = new HashMap<>();
            if(!periodView.getDateBefore().isEmpty()) {
                query.put("date_before", periodView.getDateBefore());
            }
            if(!periodView.getDateAfter().isEmpty()) {
                query.put("date_after", periodView.getDateAfter());
            }
            if(!contentView.getContent().isEmpty()) {
                String[] contentTypes = getResources().getStringArray(R.array.content_types);
                for(int i = 0;i < contentTypes.length;i++) {
                    if (contentView.getContentType().equals(contentTypes[i])) {
                        query.put(ContentView.CONTENT_TYPE_KEYS[i], contentView.getContent());
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
            String[] paymentTypes = getResources().getStringArray(R.array.payment_types);
            if(!paymentType.getSelectedItem().toString().equals(paymentTypes[0])) {
                for(int i = 1;i < paymentTypes.length;i++) {
                    if (paymentType.getSelectedItem().toString().equals(paymentTypes[i])) {
                        query.put("payment_type", PAYMENT_TYPE_VALUES[i-1]);
                    }
                }
            }
            currentPage = 1;
            query.put("page", String.valueOf(currentPage));
            payments.clear();
            ((IndexActivity) context).searchPayments(query);
        } else if(v == nextPage) {
            query.put("page", String.valueOf(++currentPage));
            ((IndexActivity) context).searchPayments(query);
        }
    }
}
