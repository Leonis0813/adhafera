package com.leonis.android.adhafera;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.leonis.android.adhafera.lib.HTTPClient;
import com.leonis.android.adhafera.service.InputChecker;
import com.leonis.android.adhafera.view.RegistrationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
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
        rv = findViewById(R.id.registration);
        inputChecker = new InputChecker();
        settle();
        getCategories();
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

    public void registPayment(String[] inputs) {
        ArrayList<Integer> ids = inputChecker.checkEmpty(inputs);
        if(!ids.isEmpty()) {
            Iterator<Integer> it = ids.iterator();
            String errorMessage = rv.getLabel(it.next());
            while(it.hasNext()) {
                errorMessage += "," + rv.getLabel(it.next());
            }
            errorMessage += "が入力されていません";
            noticeError(errorMessage, ids);
            return;
        }
        if(!inputChecker.checkDate(inputs[RegistrationView.INPUT_VIEW_DATE])) { ids.add(RegistrationView.INPUT_VIEW_DATE); }
        if(!inputChecker.checkPrice(inputs[RegistrationView.INPUT_VIEW_PRICE])) { ids.add(RegistrationView.INPUT_VIEW_PRICE); }
        if(!ids.isEmpty()) {
            Iterator<Integer> it = ids.iterator();
            String errorMessage = rv.getLabel(it.next());
            while(it.hasNext()) {
                errorMessage += "," + rv.getLabel(it.next());
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
                    rv.showMessage("収支情報を登録しました");
                    rv.resetFields();
                    rv.resetErrorCheckers();
                    rv.setToday();
                    settle();
                } else if (code == 400) {
                    rv.showMessage("収支情報の登録に失敗しました");
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
                if (code == 200) {
                    try {
                        JSONArray body = new JSONArray(data.get("body").toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                        String yearmonth = sdf.format(Calendar.getInstance().getTime());
                        if (body == null) {
                            rv.showSettlement("0");
                        } else {
                            for (int i = 0; i < body.length(); i++) {
                                if (yearmonth.equals(body.getJSONObject(i).getString("date"))) {
                                    rv.showSettlement(body.getJSONObject(i).getString("price"));
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    rv.showMessage("収支の取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 1);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {
            }
        });
    }

    public void getCategories() {
        getLoaderManager().initLoader(LOADER_ID + 2, new Bundle(), new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                return new HTTPClient(activity, "");
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONArray body = new JSONArray(data.get("body").toString());
                        String[] names = new String[body.length()];
                        for(int i=0;i<body.length();i++) {
                            names[i] = body.getJSONObject(i).getString("name");
                        }
                        rv.setCategories(names);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    rv.showMessage("カテゴリの取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 2);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }
}
