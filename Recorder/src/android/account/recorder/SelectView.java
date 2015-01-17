package android.account.recorder;

import java.util.HashMap;
import java.util.Iterator;

import android.account.recorder.state.Delete;
import android.account.recorder.state.Edit;
import android.account.recorder.state.State;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectView extends ListView {

	private ArrayAdapter<String> adapter;
	private HashMap<String, Integer> idMap;
	
	private State[] states;
	
	public SelectView(Context context) {
		super(context);
		
	    adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        setAdapter(adapter);
        
        idMap = new HashMap<String, Integer>();

        states = new State[MainActivity.NUM_OPERATION];
        states[MainActivity.INPUT] = null;
        states[MainActivity.EDIT] = new Edit(context, idMap);
        states[MainActivity.DELETE] = new Delete(context, idMap);
	}
	
	public void setup(HashMap<Integer, String> accountMap, int state) {
		adapter.clear();
		idMap.clear();
		Iterator<Integer> it = accountMap.keySet().iterator();
		while(it.hasNext()){
			int value = it.next();
			String key = accountMap.get(value);
			idMap.put(key, value);
			adapter.add(key);
		}
		setOnItemClickListener(states[state]);
	}
}
