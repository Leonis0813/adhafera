package com.register.android;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RegistrationView implements OnClickListener{
	private TextView[] labels;
	private EditText[] fields;
	private RadioButton[] tableButton;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	
	private ApplicationController ac;
	
	public RegistrationView(ApplicationController ac) {
		
	}
	
	public void showMessage(String message) {
		
	}
	
	public void showWrongInput(ArrayList<Integer> ids) {
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
