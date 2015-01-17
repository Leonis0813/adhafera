package android.account.recorder.state;

import java.util.HashMap;

import android.account.recorder.MainActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Delete extends State implements DialogInterface.OnClickListener {
	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	
	private String selected;
	
	public Delete(Context context, HashMap<String, Integer> idMap) {
		this.context = context;
		this.idMap = idMap;
		
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("â∆åvïÎÇÃçÌèú");
        alertDialogBuilder.setMessage("ñ{ìñÇ…ÇÊÇÎÇµÇ¢Ç≈Ç∑Ç©ÅH");
        alertDialogBuilder.setPositiveButton("OK", this);
        alertDialogBuilder.setNegativeButton("Cancel", this);
        alertDialog = alertDialogBuilder.create();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView listView = (ListView) parent;
        selected = (String) listView.getItemAtPosition(position);
        
        alertDialogBuilder.setCancelable(true);
        alertDialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which){
		case DialogInterface.BUTTON_POSITIVE:{
			((MainActivity) context).deleteAccount(idMap.get(selected));
			break;
		}
		case DialogInterface.BUTTON_NEGATIVE:{
			break;
		}
		}
	}

}
