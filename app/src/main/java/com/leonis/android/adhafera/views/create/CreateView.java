package com.leonis.android.adhafera.views.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leonis.android.adhafera.MainActivity;
import com.leonis.android.adhafera.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class CreateView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_DATE = 0;
    private static final int INPUT_VIEW_CONTENT = 1;
    private static final int INPUT_VIEW_CATEGORY = 2;
    public static final int INPUT_VIEW_PRICE = 3;
    private static final int INPUT_VIEW_SIZE = 4;
    private static final int INPUT_PAYMENT_TYPE = 4;
    private static final int INPUT_SIZE = 5;
    private final InputView[] inputViews;
    private final RadioGroup radioGroup;
    private final Button OK;
    private final Button cancel;
    private final TextView settleView;

    private final Context context;

    public CreateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.create_view, this);

        inputViews = new InputView[INPUT_VIEW_SIZE];
        inputViews[INPUT_VIEW_DATE] = layout.findViewById(R.id.create_date);
        inputViews[INPUT_VIEW_CONTENT] = layout.findViewById(R.id.create_content);
        inputViews[INPUT_VIEW_CATEGORY] = layout.findViewById(R.id.create_category);
        inputViews[INPUT_VIEW_PRICE] = layout.findViewById(R.id.create_price);

        radioGroup = layout.findViewById(R.id.radio_group);

        OK = layout.findViewById(R.id.OK);
        OK.setOnClickListener(this);

        cancel = layout.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        settleView = layout.findViewById(R.id.result_settle);
    }

    public void setToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        inputViews[INPUT_VIEW_DATE].setInputText(simpleDateFormat.format(new Date()));
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showWrongInput(ArrayList<Integer> ids) {
        for (Integer id : ids) {
            inputViews[id].errorChecker.setVisibility(VISIBLE);
        }
    }

    public void showSettlement(String settlement) {
        settleView.setText(String.format("%s 円", settlement));
    }

    public void resetFields() {
        for (InputView inputView : inputViews) {
            inputView.setInputText("");
        }
    }

    public void resetErrorCheckers() {
        for (InputView inputView : inputViews) {
            inputView.errorChecker.setVisibility(INVISIBLE);
        }
    }

    public void setCategoriesToDialog(String[] names) {
        ((CategoryView) inputViews[INPUT_VIEW_CATEGORY]).setCategories(names);
    }

    public void setCategoriesToForm(String[] names) {
        StringBuilder text = new StringBuilder();
        for (String name : names) {
            text.append(text.toString().equals("") ? name : "," + name);
        }
        inputViews[INPUT_VIEW_CATEGORY].setInputText(text.toString());
    }

    public String getLabel(int id) {
        switch(id) {
            case INPUT_VIEW_DATE:
                return getResources().getString(R.string.date);
            case INPUT_VIEW_CONTENT:
                return getResources().getString(R.string.content);
            case INPUT_VIEW_CATEGORY:
                return getResources().getString(R.string.category);
            case INPUT_VIEW_PRICE:
                return getResources().getString(R.string.price);
            default:
                return null;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == OK) {
            String[] inputs = new String[INPUT_SIZE];
            for(int i=0;i<inputViews.length;i++) {
                String input = inputViews[i].getInputText();
                inputs[i] = input.equals("") ? null : input;
            }
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(id);
            inputs[INPUT_PAYMENT_TYPE] = radioButton.getText().toString().equals(getResources().getString(R.string.income)) ? "income" : "expense";
            resetErrorCheckers();
            ((MainActivity)context).createPayment(inputs);
        } else if(v == cancel) {
            resetFields();
            resetErrorCheckers();
        }
    }
}
