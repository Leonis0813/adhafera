package com.register.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.register.android.lib.HTTPClient;
import com.register.android.service.InputChecker;
import com.register.android.view.RegistrationView;

import android.support.v7.app.ActionBarActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements LoaderCallbacks<HashMap<String, Object> >{
	public static final int INPUT_SIZE = 5;
	private static final int LOADER_ID = 0;

	private RegistrationView rv;
	private InputChecker inputChecker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rv = (RegistrationView) findViewById(R.id.registration);
		inputChecker = new InputChecker();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void registAccount(String[] inputs) {
		ArrayList<Integer> ids = inputChecker.checkEmpty(inputs);
		if(!ids.isEmpty()) {
			Iterator<Integer> it = ids.iterator();
			String errorMessage = RegistrationView.LABELS[it.next()];
			while(it.hasNext()) {
				errorMessage += "," + RegistrationView.LABELS[it.next()];
			}
			errorMessage += "Ç™ì¸óÕÇ≥ÇÍÇƒÇ¢Ç‹ÇπÇÒ";
			noticeError(errorMessage, ids);
			return;
		}
		if(!inputChecker.checkDate(inputs[0])) {
			ids.add(0);
		}
		if(!inputChecker.checkPrice(inputs[3])) {
			ids.add(3);
		}
		if(!ids.isEmpty()) {
			Iterator<Integer> it = ids.iterator();
			String errorMessage = RegistrationView.LABELS[it.next()];
			while(it.hasNext()) {
				errorMessage += "," + RegistrationView.LABELS[it.next()];
			}
			errorMessage += "Ç™ïsê≥Ç≈Ç∑";
			noticeError(errorMessage, ids);
			return;
		}

		Bundle args = new Bundle();
		args.putStringArray("inputs", inputs);
	    getLoaderManager().initLoader(LOADER_ID, args, this);
	}
	
	public void noticeError(String errorMessage, ArrayList<Integer> ids) {
		rv.showMessage(errorMessage);
		rv.showWrongInput(ids);
	}
	
	public void noticeResult(String result) {
		rv.showMessage(result);
	}

	@Override
	public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
		String[] inputs = args.getStringArray("inputs");
		return new HTTPClient(this, inputs);
	}

	@Override
	public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
		int code = Integer.parseInt(data.get("statusCode").toString());
		if(code == 201) {
			rv.showMessage("â∆åvïÎÇìoò^ÇµÇ‹ÇµÇΩ");
			rv.resetField();
		} else if (code == 400) {
			rv.showMessage("â∆åvïÎÇÃìoò^Ç…é∏îsÇµÇ‹ÇµÇΩ");
		}
		getLoaderManager().destroyLoader(LOADER_ID);
	}

	@Override
	public void onLoaderReset(Loader<HashMap<String, Object>> loader) {
		
	}
}
