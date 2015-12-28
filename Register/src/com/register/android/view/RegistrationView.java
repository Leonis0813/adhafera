package com.register.android.view;

import java.util.ArrayList;
import java.util.Iterator;

import com.register.android.MainActivity;
import com.register.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
	public static final String[] LABELS = {"ì˙ït", "ì‡óe", "ÉJÉeÉSÉä", "ã‡äz"};
	private static final String[] ACCOUNT_TYPES = {"é˚ì¸", "éxèo"};
	private static final String[] ACCOUNT_TYPES_EN = {"income", "expense"};
	
	private EditText[] fields;
	private TextView[] errorCheckers;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	
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

		errorCheckers = new TextView[LABELS.length];
		errorCheckers[0] = (TextView) layout.findViewById(R.id.check_date);
		errorCheckers[1] = (TextView) layout.findViewById(R.id.check_content);
		errorCheckers[2] = (TextView) layout.findViewById(R.id.check_category);
		errorCheckers[3] = (TextView) layout.findViewById(R.id.check_price);

		radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);
//		radioGroup.addView((RadioButton) layout.findViewById(R.id.income));
//		radioGroup.addView((RadioButton) layout.findViewById(R.id.expense));

		OK = (Button) layout.findViewById(R.id.OK);
		OK.setOnClickListener(this);
		cancel = (Button) layout.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
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
			inputs[4] = radioButton.getText().toString().equals("é˚ì¸") ? "income" : "expense";
			((MainActivity)context).registAccount(inputs);
		} else if(v == cancel) {
			for(int i=0;i<LABELS.length;i++) {
				fields[i].setText("");
				errorCheckers[i].setVisibility(INVISIBLE);
			}
		}
	}
}
