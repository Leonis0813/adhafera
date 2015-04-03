package android.account.recorder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class AccountView extends ViewGroup {
	protected Context context;
	protected Controller ctrl;
	protected ViewGroup viewGroup;
	
	public AccountView(Context context, Controller ctrl) {
		super(context);
		this.context = context;
		this.ctrl = ctrl;
	}
	
	public ViewGroup getViewGroup() {
		return viewGroup;
	}
	
	public void notice(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public abstract void initialize();
	public abstract void show(String[] infos);
	public abstract void setState(int state);
}
