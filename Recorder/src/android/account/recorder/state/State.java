package android.account.recorder.state;

import java.util.HashMap;

import android.content.Context;
import android.widget.AdapterView;

public abstract class State implements AdapterView.OnItemClickListener {
	
	protected Context context;
	protected HashMap<String, Integer> idMap;

}
