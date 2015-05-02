package android.account.recorder;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {
	public static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    
    private AccountView[] accountViews;
    private int currentView;
    public static final int INPUT = 0;
    public static final int EDIT = 1;
    public static final int DELETE = 2;
    public static final int NUM_VIEW = 3;
    
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Controller ctrl = new Controller(this, new AccountController());
		
		accountViews = new AccountView[NUM_VIEW];
		accountViews[INPUT] = new InputView(this, ctrl);
		accountViews[EDIT] = new SelectView(this, ctrl);
		accountViews[DELETE] = new DeleteView(this, ctrl);
		currentView = INPUT;
		setContentView(accountViews[currentView].getViewGroup());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, INPUT, 0, R.string.menu_input);
		menu.add(0, EDIT, 0, R.string.menu_edit);
	    menu.add(0, DELETE, 0, R.string.menu_delete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		currentView = item.getItemId();
		accountViews[currentView].initialize();
		if(currentView == INPUT) {
			accountViews[currentView].setState(InputView.CREATE);
		}
		setContentView(accountViews[currentView].getViewGroup());
		return true;
	}
	
	public void switchView(int viewID) {
		currentView = viewID;
		setContentView(accountViews[currentView].getViewGroup());
	}
	
	public void updateView() {
		accountViews[currentView].initialize();
	}
	
	public void sendMessage(String message) {
		accountViews[currentView].notice(message);
	}
	
	public void showAccount(String[] infos) {
		accountViews[currentView].show(infos);
	}
	
	public void changeState(int state) {
		accountViews[currentView].setState(state);
	}
	
	public void noteInputError(String note, ArrayList<Integer> ids) {
		Iterator<Integer> it = ids.iterator();
		while(it.hasNext()) {
			((InputView)accountViews[currentView]).noticeError(it.next());
		}
		accountViews[currentView].notice(note);
	}
}
