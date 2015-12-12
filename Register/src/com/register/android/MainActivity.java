package com.register.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements LoaderCallbacks<HashMap<String, Object> >{
	public static final int INPUT_SIZE = 5;
	private RegistrationView rv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		rv = new RegistrationView(this);
		setContentView(rv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void registAccount(String[] inputs) {
		Bundle args = new Bundle();
		args.putStringArray("inputs", inputs);
	    getLoaderManager().initLoader(0, args, this);
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
			rv.showMessage("‰ÆŒv•ë‚ð“o˜^‚µ‚Ü‚µ‚½");
		} else if (code == 400) {
			rv.showMessage("‰ÆŒv•ë‚Ì“o˜^‚ÉŽ¸”s‚µ‚Ü‚µ‚½");
		}
	}

	@Override
	public void onLoaderReset(Loader<HashMap<String, Object>> loader) {
		
	}
}
