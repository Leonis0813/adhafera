package com.leonis.android.adhafera.view;

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

public class RegistrationView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_DATE = 0;
    private static final int INPUT_VIEW_CONTENT = 1;
    private static final int INPUT_VIEW_CATEGORY = 2;
    public static final int INPUT_VIEW_PRICE = 3;
    private static final int INPUT_VIEW_SIZE = 4;
    private static final int INPUTS_PAYMENT_TYPE = 4;
    private final InputView[] inputViews;
    private final RadioGroup radioGroup;
    private final Button OK;
    private final Button cancel;
    private final TextView settleView;

    private final Context context;

    public RegistrationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.registration_view, this);

        inputViews = new InputView[INPUT_VIEW_SIZE];
        inputViews[INPUT_VIEW_DATE] = layout.findViewById(R.id.date);
        inputViews[INPUT_VIEW_CONTENT] = layout.findViewById(R.id.content);
        inputViews[INPUT_VIEW_CATEGORY] = layout.findViewById(R.id.category);
        inputViews[INPUT_VIEW_PRICE] = layout.findViewById(R.id.price);

        radioGroup = findViewById(R.id.radio_group);

        OK = findViewById(R.id.OK);
        OK.setOnClickListener(this);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        settleView = findViewById(R.id.result_settle);
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

    public void setCategories(String[] names) {
        ((CategoryView) inputViews[INPUT_VIEW_CATEGORY]).setCategories(names);
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
            String[] inputs = new String[MainActivity.INPUT_SIZE];
            for(int i=0;i<inputViews.length;i++) {
                String input = inputViews[i].getInputText();
                inputs[i] = input.equals("") ? null : input;
            }
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(id);
            inputs[INPUTS_PAYMENT_TYPE] = radioButton.getText().toString().equals(getResources().getString(R.string.income)) ? "income" : "expense";
            resetErrorCheckers();
            ((MainActivity)context).registerPayment(inputs);
        } else if(v == cancel) {
            resetFields();
            resetErrorCheckers();
        }
    }
}
