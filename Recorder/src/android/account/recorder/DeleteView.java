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
	private RelativeLayout relativeLayout;
	private ArrayList<CheckBox> checkBoxs;
	private ArrayList<TextView> textViews;
	private Button deleteButton;
	
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	
	public DeleteView(Context context, Controller ctrl) {
		super(context, ctrl);
		
		viewGroup = new RelativeLayout(context);
		
		createRemoveButton();
		
		ScrollView scrollView = new ScrollView(context);
		relativeLayout = new RelativeLayout(context);
		scrollView.addView(relativeLayout);
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
		relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		relativeParams.addRule(RelativeLayout.ABOVE, 100);
		viewGroup.addView(scrollView, relativeParams);

		checkBoxs = new ArrayList<CheckBox>();
		textViews = new ArrayList<TextView>();
		
        alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("â∆åvïÎÇÃçÌèú");
        alertDialogBuilder.setMessage("ñ{ìñÇ…ÇÊÇÎÇµÇ¢Ç≈Ç∑Ç©ÅH");
        alertDialogBuilder.setPositiveButton("OK", this);
        alertDialogBuilder.setNegativeButton("Cancel", this);
        alertDialog = alertDialogBuilder.create();
	}
	
	private void createRemoveButton() {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(MainActivity.MP, MainActivity.WC);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        deleteButton = new Button(context);
        deleteButton.setId(100);
		deleteButton.setText("çÌèú");
		deleteButton.setOnClickListener(this);
		viewGroup.addView(deleteButton, relativeParams);
	}
	
	private void addComponent(String item) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setId(checkBoxs.size()+1);
		
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
		if(checkBoxs.isEmpty()){
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		}else{
			relativeParams.addRule(RelativeLayout.BELOW, checkBoxs.size());
		}
		checkBoxs.add(checkBox);
		textViews.add(textView);
		relativeLayout.addView(linearLayout, relativeParams);
	}
	
	public void initialize() {
		relativeLayout.removeAllViews();
		checkBoxs.clear();
		textViews.clear();
		ctrl.getAllAccount();
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
		alertDialogBuilder.setCancelable(true);
        alertDialog.show();		
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which){
		case DialogInterface.BUTTON_POSITIVE:{
			ArrayList<String> deleteList = new ArrayList<String>();
			for(int i=0;i<checkBoxs.size();i++) {
				if(checkBoxs.get(i).isChecked()) {
					deleteList.add(textViews.get(i).getText().toString());
				}
			}
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
