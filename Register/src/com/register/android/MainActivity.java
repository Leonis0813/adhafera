package com.register.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.register.android.lib.HTTPClient;
import com.register.android.service.InputChecker;
import com.register.android.view.RegistrationView;

import android.support.v7.app.ActionBarActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	public static final int INPUT_SIZE = 5;
	private static final int LOADER_ID = 0;

	private Context activity;
	private RegistrationView rv;
	private InputChecker inputChecker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		activity = this;
		rv = (RegistrationView) findViewById(R.id.registration);
		inputChecker = new InputChecker();
		settle();
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
			errorMessage += "が入力されていません";
			noticeError(errorMessage, ids);
			return;
		}
		if(!inputChecker.checkDate(inputs[0])) { ids.add(0); }
		if(!inputChecker.checkPrice(inputs[3])) { ids.add(3); }
		if(!ids.isEmpty()) {
			Iterator<Integer> it = ids.iterator();
			String errorMessage = RegistrationView.LABELS[it.next()];
			while(it.hasNext()) {
				errorMessage += "," + RegistrationView.LABELS[it.next()];
			}
			errorMessage += "が不正です";
			noticeError(errorMessage, ids);
			return;
		}

		Bundle args = new Bundle();
		args.putStringArray("inputs", inputs);
	    getLoaderManager().initLoader(LOADER_ID, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
	      @Override
	      public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
	        return new HTTPClient(activity, args.getStringArray("inputs"));
	      }
	      
	      @Override
	      public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
	          int code = Integer.parseInt(data.get("statusCode").toString());
	          if(code == 201) {
	              rv.showMessage("家計簿を登録しました");
	              rv.resetField();
	              rv.setToday();
	              settle();
	          } else if (code == 400) {
	              rv.showMessage("家計簿の登録に失敗しました");
	          }
	          getLoaderManager().destroyLoader(LOADER_ID);
	      }
	      
	      @Override
	      public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
	    });
	}
	
	public void noticeError(String errorMessage, ArrayList<Integer> ids) {
		rv.showMessage(errorMessage);
		rv.showWrongInput(ids);
	}
	
	public void noticeResult(String result) {
		rv.showMessage(result);
	}

	public void settle() {
      getLoaderManager().initLoader(LOADER_ID + 1, new Bundle(), new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
        @Override
        public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
          return new HTTPClient(activity);
        }

        @Override
        public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
          int code = Integer.parseInt(data.get("statusCode").toString());
          if(code == 200) {
            try {
              JSONObject body = new JSONObject(data.get("body").toString());
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
              String yearmonth = sdf.format(Calendar.getInstance().getTime());
              if(body.isNull(yearmonth)) {
                rv.showSettlement("0");
              } else {
                rv.showSettlement(body.getString(yearmonth));
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          } else if (code == 400) {
            rv.showMessage("収支の取得に失敗しました");
          }
          getLoaderManager().destroyLoader(LOADER_ID + 1);
        }

        public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
      });
	}
}
