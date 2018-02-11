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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by leonis on 2018/02/11.
 */

public class RegistrationView extends RelativeLayout implements OnClickListener {
    public static final int INPUT_VIEW_DATE = 0;
    public static final int INPUT_VIEW_CONTENT = 1;
    public static final int INPUT_VIEW_CATEGORY = 2;
    public static final int INPUT_VIEW_PRICE = 3;
    public static final int INPUT_VIEW_SIZE = 4;
    public static final int INPUTS_PAYMENT_TYPE = 4;
    private InputView[] inputViews;
    private RadioGroup radioGroup;
    private Button OK, cancel;
    private TextView settleView;

    private Context context;

    public RegistrationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        View layout = View.inflate(context, R.layout.registration_view, this);

        inputViews = new InputView[INPUT_VIEW_SIZE];
        inputViews[INPUT_VIEW_DATE] = (InputView) layout.findViewById(R.id.date);
        inputViews[INPUT_VIEW_CONTENT] = (InputView) layout.findViewById(R.id.content);
        inputViews[INPUT_VIEW_CATEGORY] = (InputView) layout.findViewById(R.id.category);
        inputViews[INPUT_VIEW_PRICE] = (InputView) layout.findViewById(R.id.price);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        OK = (Button) findViewById(R.id.OK);
        OK.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        settleView = (TextView) findViewById(R.id.result_settle);
    }

    public void setToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        inputViews[INPUT_VIEW_DATE].setInputText(simpleDateFormat.format(new Date()));
    }

    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showWrongInput(ArrayList<Integer> ids) {
        Iterator<Integer> it = ids.iterator();
        while(it.hasNext()) {
            inputViews[it.next()].errorChecker.setVisibility(VISIBLE);
        }
    }

    public void showSettlement(String settlement) {
        settleView.setText(settlement + " 円");
    }

    public void resetFields() {
        for(int i=0;i<inputViews.length;i++) {
            inputViews[i].setInputText("");
        }
    }

    public void resetErrorCheckers() {
        for(int i=0;i<inputViews.length;i++) {
            inputViews[i].errorChecker.setVisibility(INVISIBLE);
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
            RadioButton radioButton = (RadioButton) findViewById(id);
            inputs[INPUTS_PAYMENT_TYPE] = radioButton.getText().toString().equals(getResources().getString(R.string.income)) ? "income" : "expense";
            resetErrorCheckers();
            ((MainActivity)context).registPayment(inputs);
        } else if(v == cancel) {
            resetFields();
            resetErrorCheckers();
        }
    }
}
