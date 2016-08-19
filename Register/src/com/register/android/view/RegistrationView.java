package com.register.android.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.register.android.MainActivity;
import com.register.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationView extends RelativeLayout implements OnClickListener{
	public static final String[] LABELS = {"日付", "内容", "カテゴリ", "金額"};

	private EditText[] fields;
	private TextView[] errorCheckers;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	private TextView settleView;
	
	private Context context;
	
	public RegistrationView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;

		View layout = LayoutInflater.from(context).inflate(R.layout.registration_view, this);

		fields = new EditText[LABELS.length];
		fields[0] = (EditText) layout.findViewById(R.id.field_date);
		fields[1] = (EditText) layout.findViewById(R.id.field_content);
		fields[2] = (EditText) layout.findViewById(R.id.field_category);
		fields[3] = (EditText) layout.findViewById(R.id.field_price);
		setToday();

		errorCheckers = new TextView[LABELS.length];
		errorCheckers[0] = (TextView) layout.findViewById(R.id.check_date);
		errorCheckers[1] = (TextView) layout.findViewById(R.id.check_content);
		errorCheckers[2] = (TextView) layout.findViewById(R.id.check_category);
		errorCheckers[3] = (TextView) layout.findViewById(R.id.check_price);

		radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);

		OK = (Button) layout.findViewById(R.id.OK);
		OK.setOnClickListener(this);
		cancel = (Button) layout.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		settleView = (TextView) layout.findViewById(R.id.result_settle);
		((MainActivity) context).settle();
	}

	public void setToday() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		fields[0].setText(simpleDateFormat.format(new Date()));
	}

	public void showMessage(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public void showWrongInput(ArrayList<Integer> ids) {
		Iterator<Integer> it = ids.iterator();
		while(it.hasNext()) {
			errorCheckers[it.next()].setVisibility(VISIBLE);
		}
	}

	public void showSettlement(String settlement) {
	  settleView.setText(settlement);
	}

	public void resetField() {
		for(int i=0;i<fields.length;i++) {
			fields[i].setText("");
			errorCheckers[i].setVisibility(INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if(v == OK) {
			String[] inputs = new String[MainActivity.INPUT_SIZE];
			for(int i=0;i<LABELS.length;i++) {
				String input = fields[i].getText().toString();
				inputs[i] = input.equals("") ? null : input;
			}
			int id = radioGroup.getCheckedRadioButtonId();
			RadioButton radioButton = (RadioButton) findViewById(id);
			inputs[4] = radioButton.getText().toString().equals("収入") ? "income" : "expense";
			((MainActivity)context).registAccount(inputs);
		} else if(v == cancel) {
			for(int i=0;i<LABELS.length;i++) {
				resetField();
			}
		}
	}
}
