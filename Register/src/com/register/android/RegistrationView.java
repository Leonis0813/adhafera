package com.register.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegistrationView extends RelativeLayout implements OnClickListener{
	private static final String[] LABELS = {"ì˙ït", "ì‡óe", "ÉJÉeÉSÉä", "ã‡äz"};
	private static final String[] ACCOUNT_TYPES = {"é˚ì¸", "éxèo"};
	private TextView[] labels;
	private EditText[] fields;
	private RadioButton[] tableButtons;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	
	private ApplicationController ac;
	
	public RegistrationView(ApplicationController ac, Context context) {
		super(context);
		
		this.ac = ac;
		
		labels = new TextView[LABELS.length];
		fields = new EditText[LABELS.length];
		for(int i=0;i<labels.length;i++) {
			labels[i] = new TextView(context);
			fields[i] = new EditText(context);
			labels[i].setText(LABELS[i]);
		}
		
		tableButtons = new RadioButton[ACCOUNT_TYPES.length];
		radioGroup = new RadioGroup(context);
		for(int i=0;i<tableButtons.length;i++) {
			tableButtons[i] = new RadioButton(context);
			tableButtons[i].setText(ACCOUNT_TYPES[i]);
			radioGroup.addView(tableButtons[i]);			
		}
		tableButtons[1].setChecked(true);
		
		OK = new Button(context);
		OK.setText("ìoò^");
		OK.setOnClickListener(this);
		
		cancel = new Button(context);
		cancel.setText("éÊè¡");
		cancel.setOnClickListener(this);
	}
	
	public void showMessage(String message) {
		
	}
	
	public void showWrongInput(ArrayList<Integer> ids) {
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
