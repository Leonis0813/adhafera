package com.leonis.android.adhafera.views.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leonis.android.adhafera.R;
import com.leonis.android.adhafera.models.Payment;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by leonis on 2018/12/30.
 */

public class PaymentListAdapter extends BaseAdapter {
    private Context context;
    private List<Payment> payments;

    private class ViewHolder {
        TextView contentView;
        TextView dateView;
        TextView categoryView;
        TextView priceView;
    }

    public PaymentListAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @Override
    public int getCount() {
        return payments.size();
    }

    @Override
    public Object getItem(int position) {
        return payments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        Payment payment = payments.get(position);


        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.payment_list_view, parent, false);

            TextView contentView = view.findViewById(R.id.payment_content);
            TextView dateView = view.findViewById(R.id.payment_date);
            TextView categoryView = view.findViewById(R.id.payment_category);
            TextView priceView = view.findViewById(R.id.payment_price);

            holder = new ViewHolder();
            holder.contentView = contentView;
            holder.dateView = dateView;
            holder.categoryView = categoryView;
            holder.priceView = priceView;
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.contentView.setText(payment.getContent());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        holder.dateView.setText(format.format(payment.getDate()));

        String[] categories = payment.getCategories();
        String categoryString = "";
        for(String category : categories) {
            categoryString += category + ",";
        }
        holder.categoryView.setText(categoryString.substring(0, categoryString.length() - 1));

        holder.priceView.setText(String.valueOf(payment.getPrice()));

        return view;
    }
}
