package com.register.android.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.register.android.MainActivity;
import com.register.android.R;

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

public class RegistrationView extends RelativeLayout implements OnClickListener {
	private InputView[] inputViews;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	private TextView settleView;
	
	private Context context;
	
	public RegistrationView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		
		View layout = View.inflate(context, R.layout.registration_view, this);

		inputViews = new InputView[4];
		inputViews[0] = (InputView) layout.findViewById(R.id.date);
		inputViews[1] = (InputView) layout.findViewById(R.id.content);
		inputViews[2] = (InputView) layout.findViewById(R.id.category);
		inputViews[3] = (InputView) layout.findViewById(R.id.price);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

		OK = (Button) findViewById(R.id.OK);
		OK.setOnClickListener(this);

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);

		settleView = (TextView) findViewById(R.id.result_settle);
	}

	public void setToday() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		inputViews[0].setInputText(simpleDateFormat.format(new Date()));
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

	public void resetField() {
		for(int i=0;i<inputViews.length;i++) {
		  inputViews[i].setInputText("");
		  inputViews[i].errorChecker.setVisibility(INVISIBLE);
		}
	}

	public void setCategories(String[] names) {
	  ((CategoryView) inputViews[2]).setCategories(names);
	}

	public String getLabel(int id) {
	  switch(id) {
	  case 0:
	    return DateView.LABEL;
	  case 1:
	    return ContentView.LABEL;
	  case 2:
	    return CategoryView.LABEL;
	  case 3:
	    return PriceView.LABEL;
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
			inputs[4] = radioButton.getText().toString().equals("収入") ? "income" : "expense";
			((MainActivity)context).registPayment(inputs);
		} else if(v == cancel) {
          resetField();
		}
	}
}
