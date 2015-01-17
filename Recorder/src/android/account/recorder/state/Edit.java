package android.account.recorder.state;

import java.util.HashMap;

import android.account.recorder.MainActivity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Edit extends State {
	
	public Edit(Context context, HashMap<String, Integer> idMap) {
		this.context = context;
		this.idMap = idMap;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView listView = (ListView) parent;
        String item = (String) listView.getItemAtPosition(position);
        ((MainActivity) context).showAccount(item, idMap.get(item));
	}
}
