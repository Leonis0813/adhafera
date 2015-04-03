package android.account.recorder;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectView extends AccountView implements AdapterView.OnItemClickListener {
	private ArrayAdapter<String> adapter;
	
	public SelectView(Context context, Controller ctrl) {
		super(context, ctrl);
		viewGroup = new ListView(context);

	    adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        ((ListView) viewGroup).setAdapter(adapter);
        ((AdapterView<?>) viewGroup).setOnItemClickListener(this);
	}
	
	public void initialize() {
		adapter.clear();
		ctrl.getAllAccount();
	}
	
	public void show(String[] infos) {
		String item = infos[0];
		for(int i=1;i<infos.length;i++) {
			item += ", " + infos[i];
		}
		adapter.add(item);
	}
	
	public void setState(int stateID){}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView listView = (ListView) parent;
        String item = (String) listView.getItemAtPosition(position);
        ctrl.editAccount(item);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {}
}
