package android.account.recorder;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import application.view.table.AccountTable;

public class InputView extends AccountView implements OnClickListener, RadioGroup.OnCheckedChangeListener, OnDateSetListener {	
	private static final String[] COLUMN_NAMES = {"　　日付：", "　　内容：", "カテゴリ：", "　　金額："};
	private static final String[] TABLE = {"収入", "支出"};
	private TextView[] labels;
	private EditText[] fields;
	private TextView[] checker;
	
	private RadioGroup radioGroup;
	private RadioButton[] radioButtons;
	
	private Button OK, cancel;
	
	private State[] states;
	public static final int CREATE = 0;
	public static final int UPDATE = 1;
	public static final int NUM_STATE = 2;
	private State currentState;

	public InputView(Context context, Controller ctrl) {
		super(context, ctrl);
		viewGroup = new RelativeLayout(context);

		labels = new TextView[COLUMN_NAMES.length];
        fields = new EditText[COLUMN_NAMES.length];
        checker = new TextView[COLUMN_NAMES.length];
        for(int i=0;i<COLUMN_NAMES.length;i++) {
        	createColumn(COLUMN_NAMES[i], i);
        }
        createRadioButtons();
		createButtons();
		
		states = new State[NUM_STATE];
		states[CREATE] = new Create(ctrl);
		states[UPDATE] = new Update(ctrl);
		currentState = states[CREATE];
	}
	
	private void createColumn(String label, int ID) {
		RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setId(ID+1);
		LinearLayout.LayoutParams linearParam;
		
		labels[ID] = new TextView(context);
		labels[ID].setTextSize(20);
		labels[ID].setTextColor(Color.BLACK);
		labels[ID].setText(label);
		linearParam = new LinearLayout.LayoutParams(MainActivity.WC, MainActivity.WC);
		linearLayout.addView(labels[ID], linearParam);

		fields[ID] = new EditText(context);
		fields[ID].setWidth(400);
		fields[ID].setEnabled(true);
		fields[ID].setTextSize(20);
		fields[ID].setTextColor(Color.BLACK);
		if(ID==0){
			fields[ID].setClickable(true);
			fields[ID].setOnClickListener(this);
		}
		linearParam = new LinearLayout.LayoutParams(MainActivity.WC, MainActivity.WC);
		linearLayout.addView(fields[ID], linearParam);
		
		checker[ID] = new TextView(context);
		checker[ID].setTextSize(20);
		checker[ID].setTextColor(Color.RED);
		checker[ID].setText("*");
		checker[ID].setVisibility(INVISIBLE);
		linearParam = new LinearLayout.LayoutParams(MainActivity.WC, MainActivity.WC);
		linearLayout.addView(checker[ID], linearParam);
		
		relativeParam.setMargins(20, ID==0 ? 50 : 20, 0, 0);
		relativeParam.addRule(RelativeLayout.BELOW, ID);
		
		viewGroup.addView(linearLayout, relativeParam);
	}

	private void createRadioButtons() {
		radioGroup = new RadioGroup(context);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setGravity(Gravity.CENTER_HORIZONTAL);
        radioButtons = new RadioButton[TABLE.length];
        
		RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		LinearLayout.LayoutParams linearParam;
		
		for(int i=0;i<TABLE.length;i++){
			linearParam = new LinearLayout.LayoutParams(MainActivity.WC, MainActivity.WC);
			linearParam.weight = 1;
			linearParam.setMargins(i==0 ? 0: 50, 0, i==0 ? 50 : 0, 0);
			radioButtons[i] = new RadioButton(context);
			radioButtons[i].setId(i);
			radioButtons[i].setText(TABLE[i]);
			radioButtons[i].setTextSize(20);
			radioGroup.addView(radioButtons[i], linearParam);
		}
		radioButtons[1].setChecked(true);
		
        relativeParam.addRule(RelativeLayout.BELOW, 4);
        relativeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeParam.setMargins(0, 50, 0, 0);
		viewGroup.addView(radioGroup, relativeParam);
	}
	
	private void createButtons() {
		OK = new Button(context);
		OK.setText("保存");
		OK.setOnClickListener(this);
		cancel = new Button(context);
		cancel.setText("取消");
		cancel.setOnClickListener(this);
		
		RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		
		LinearLayout linearLayout = new LinearLayout(context);
		LinearLayout.LayoutParams linearParam;
		linearParam = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		linearParam.weight = 1;
		linearParam.setMargins(50, 0, 20, 0);
		linearLayout.addView(OK, linearParam);
		
		linearParam = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		linearParam.weight = 1;
		linearParam.setMargins(20, 0, 50, 0);
		linearLayout.addView(cancel, linearParam);
		
		relativeParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relativeParam.setMargins(0, 50, 0, 0);
		viewGroup.addView(linearLayout, relativeParam);
	}
	
	public void initialize() {
		for(int i=0;i<COLUMN_NAMES.length;i++) {
			fields[i].setText("");
			checker[i].setVisibility(INVISIBLE);
		}
	}

	public void show(String[] infos) {
		if(infos[Account.TYPE].equals("収入")) {
			radioButtons[0].setChecked(true);
			radioButtons[1].setChecked(false);
		}else if(infos[Account.TYPE].equals("支出")) {
			radioButtons[0].setChecked(false);
			radioButtons[1].setChecked(true);
		}else{}
		for(int i=1;i<infos.length;i++) {
			fields[i-1].setText(infos[i]);
			checker[i-1].setVisibility(INVISIBLE);
		}
	}

	public void setState(int stateID) {
		currentState = states[stateID];
	}
	
	public void noticeError(int column) {
		checker[column].setVisibility(VISIBLE);
	}
	
	@Override
	public void onClick(View view) {
		if(view == fields[AccountTable.DATE]) {
			Calendar calendar = Calendar.getInstance();
	        int year = calendar.get(Calendar.YEAR); // 年
	        int month = calendar.get(Calendar.MONTH); // 月
	        int day = calendar.get(Calendar.DAY_OF_MONTH); // 日
	        
			DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
	        datePickerDialog.show();
		}else{
			for(int i=0;i<fields.length;i++) {
				checker[i].setVisibility(INVISIBLE);
			}
			if(view == OK) {
				String[] column = {
						TABLE[radioGroup.getCheckedRadioButtonId()],
						fields[Account.DATE-1].getText().toString(),
						fields[Account.CONTENT-1].getText().toString(),
						fields[Account.CATEGORY-1].getText().toString(),
						fields[Account.PRICE-1].getText().toString(),
				};
				currentState.putAccount(column);
			} else if(view == cancel) {
				currentState.clickCancel();
			}
		}
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		String monthOfStr = month < 9 ? "0"+(++month) : ""+(++month);
		String dayOfStr = day < 10 ? "0"+day : ""+day;
		fields[0].setText(year + "-" + monthOfStr + "-" + dayOfStr);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {}
}
