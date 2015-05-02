package android.account.recorder;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DeleteView extends AccountView implements DialogInterface.OnClickListener, View.OnClickListener{
	private RelativeLayout accountViewGroup;
	private ArrayList<CheckBox> checkBoxes;
	private ArrayList<TextView> textViews;
	private Button deleteButton;
	private Button deleteAllButton;
	private ArrayList<String> deleteList;
	
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	
	public DeleteView(Context context, Controller ctrl) {
		super(context, ctrl);
		
		viewGroup = new RelativeLayout(context);
		
		createRemoveButtons();
		createScrollView();
		createDialog();

		checkBoxes = new ArrayList<CheckBox>();
		textViews = new ArrayList<TextView>();
        deleteList = new ArrayList<String>();
	}
	
	private void createRemoveButtons() {		
		RelativeLayout.LayoutParams relativeParam = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setId(100);
		LinearLayout.LayoutParams linearParam;
		linearParam = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		linearParam.weight = 1;
		linearParam.setMargins(50, 0, 20, 0);
		deleteButton = new Button(context);
		deleteButton.setText("削除");
		deleteButton.setOnClickListener(this);
		linearLayout.addView(deleteButton, linearParam);
		
		linearParam = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		linearParam.weight = 1;
		linearParam.setMargins(20, 0, 50, 0);
		deleteAllButton = new Button(context);
		deleteAllButton.setText("全削除");
		deleteAllButton.setOnClickListener(this);
		linearLayout.addView(deleteAllButton, linearParam);
		
		relativeParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relativeParam.setMargins(0, 50, 0, 0);
		viewGroup.addView(linearLayout, relativeParam);
	}
	
	private void createScrollView() {
		ScrollView scrollView = new ScrollView(context);
		accountViewGroup = new RelativeLayout(context);
		scrollView.addView(accountViewGroup);
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		relativeParams.addRule(RelativeLayout.ABOVE, 100);
		viewGroup.addView(scrollView, relativeParams);
	}
	
	private void createDialog() {
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("家計簿の削除");
        alertDialogBuilder.setMessage("本当によろしいですか？");
        alertDialogBuilder.setPositiveButton("OK", this);
        alertDialogBuilder.setNegativeButton("Cancel", this);
        alertDialog = alertDialogBuilder.create();
	}
	
	private void addComponent(String item) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setId(checkBoxes.size()+1);
		
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		CheckBox checkBox = new CheckBox(context);
		linearParams.weight = 1;
		linearLayout.addView(checkBox);
		
		linearParams = new LinearLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		TextView textView = new TextView(context);
		textView.setTextSize(18);
		textView.setText(item);
		linearParams.weight = 4;
		linearLayout.addView(textView);
		
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		if(checkBoxes.isEmpty()){
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		}else{
			relativeParams.addRule(RelativeLayout.BELOW, checkBoxes.size());
		}
		checkBoxes.add(checkBox);
		textViews.add(textView);
		accountViewGroup.addView(linearLayout, relativeParams);
	}
	
	public void initialize() {
		accountViewGroup.removeAllViews();
		checkBoxes.clear();
		textViews.clear();
		ctrl.getAccount();
	}
	
	public void show(String[] infos) {
		String item = infos[0];
		for(int i=1;i<infos.length;i++) {
			item += ", " + infos[i];
		}
		addComponent(item);
	}
	
	public void setState(int stateID){}
	
	public void onClick(View view) {
		deleteList.clear();
		if(view == deleteButton) {
			for(int i=0;i<checkBoxes.size();i++) {
				if(checkBoxes.get(i).isChecked()) {
					deleteList.add(textViews.get(i).getText().toString());
				}
			}
			if(deleteList.isEmpty()){
				this.notice("何も選択されていません");
				return;
			}
		} else if(view == deleteAllButton) {
			for(int i=0;i<checkBoxes.size();i++) {
				deleteList.add(textViews.get(i).getText().toString());
			}
		}
		alertDialogBuilder.setCancelable(true);
        alertDialog.show();		
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which){
		case DialogInterface.BUTTON_POSITIVE:{
			ctrl.deleteAccount(deleteList);
			break;
		}
		case DialogInterface.BUTTON_NEGATIVE:{
			break;
		}
		}
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {}
}
