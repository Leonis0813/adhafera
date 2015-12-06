package com.register.android;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationView extends RelativeLayout implements OnClickListener{
	private static final String[] LABELS = {"　　日付：", "　　内容：", "カテゴリ：", "　　金額："};
	private static final String[] ACCOUNT_TYPES = {"収入", "支出"};
	private TextView[] labels;
	private EditText[] fields;
	private TextView[] errorCheckers;
	private RadioButton[] tableButtons;
	private RadioGroup radioGroup;
	private Button OK, cancel;
	
	private ApplicationController ac;
	private Context context;
	
	public RegistrationView(ApplicationController ac, Context context) {
		super(context);
		
		this.ac = ac;
		this.context = context;

		final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
		final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
		LinearLayout ll;
		RelativeLayout.LayoutParams rlp;
		LinearLayout.LayoutParams llp;

		labels = new TextView[LABELS.length];
		fields = new EditText[LABELS.length];
		errorCheckers = new TextView[LABELS.length];
		for(int i=0, j=1;i<labels.length;i++) {
			labels[i] = new TextView(context);
			fields[i] = new EditText(context);
			errorCheckers[i] = new TextView(context);
			labels[i].setText(LABELS[i]);
			labels[i].setTextSize(25);
			labels[i].setId(j++);
			fields[i].setId(j++);
			errorCheckers[i].setText("*");
			errorCheckers[i].setTextSize(25);
			errorCheckers[i].setTextColor(Color.RED);
			errorCheckers[i].setVisibility(INVISIBLE);
			
			ll = new LinearLayout(context);
			llp = new LinearLayout.LayoutParams(MP, WC);
			llp.weight = 1;
			ll.addView(labels[i], llp);
			llp = new LinearLayout.LayoutParams(MP, WC);
			llp.weight = 1;
			ll.addView(fields[i], llp);
			ll.addView(errorCheckers[i]);
			ll.setId(i+1);
			rlp = new RelativeLayout.LayoutParams(MP, WC);
			if(i==0) {
				rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rlp.topMargin = 50;
			} else {
				rlp.addRule(RelativeLayout.BELOW, i);
			}
			rlp.leftMargin = 50;
			rlp.rightMargin = 50;
			addView(ll, rlp);
		}

		tableButtons = new RadioButton[ACCOUNT_TYPES.length];
		radioGroup = new RadioGroup(context);
		radioGroup.setOrientation(LinearLayout.HORIZONTAL);
		ll = new LinearLayout(context);
		for(int i=0;i<tableButtons.length;i++) {
			tableButtons[i] = new RadioButton(context);
			tableButtons[i].setText(ACCOUNT_TYPES[i]);
			tableButtons[i].setTextSize(20);
			radioGroup.addView(tableButtons[i]);
		}
		ll.addView(radioGroup);
		rlp = new RelativeLayout.LayoutParams(WC, WC);
		rlp.addRule(RelativeLayout.BELOW, 4);
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlp.topMargin = 30;
		addView(ll, rlp);
		tableButtons[1].setChecked(true);
		
		ll = new LinearLayout(context);
		OK = new Button(context);
		OK.setText("登録");
		OK.setOnClickListener(this);
		llp = new LinearLayout.LayoutParams(WC, WC);
		llp.leftMargin = 50;
		llp.rightMargin = 25;
		llp.weight = 1;
		ll.addView(OK, llp);
		
		cancel = new Button(context);
		cancel.setText("取消");
		cancel.setOnClickListener(this);
		rlp = new RelativeLayout.LayoutParams(MP, WC);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlp.bottomMargin = 50;
		llp = new LinearLayout.LayoutParams(WC, WC);
		llp.leftMargin = 25;
		llp.rightMargin = 50;
		llp.weight = 1;
		ll.addView(cancel, llp);
		addView(ll, rlp);
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
			String[] inputs = new String[ApplicationController.INPUT_SIZE];
			for(int i=0;i<inputs.length;i++) {
				String input = fields[i].getText().toString();
				inputs[i] = input.equals("") ? null : input;
			}
			ac.registAccount(inputs);
		} else if(v == cancel) {
			for(int i=0;i<labels.length;i++) {
				fields[i].setText("");
				errorCheckers[i].setVisibility(INVISIBLE);
			}
		}
	}
}
