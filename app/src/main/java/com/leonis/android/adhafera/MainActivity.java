package com.leonis.android.adhafera;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.leonis.android.adhafera.lib.HTTPClient;
import com.leonis.android.adhafera.service.InputChecker;
import com.leonis.android.adhafera.views.create.CreateView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int LOADER_ID = 0;

    private Context activity;
    private CreateView createView;
    private InputChecker inputChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        createView = findViewById(R.id.create);
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
        if(id == R.id.index) {
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void createPayment(String[] inputs) {
        ArrayList<Integer> ids = inputChecker.checkEmpty(inputs);
        if(!ids.isEmpty()) {
            Iterator<Integer> it = ids.iterator();
            StringBuilder errorMessage = new StringBuilder(createView.getLabel(it.next()));
            while(it.hasNext()) {
                errorMessage.append(",").append(createView.getLabel(it.next()));
            }
            errorMessage.append("が入力されていません");
            noticeError(errorMessage.toString(), ids);
            return;
        }
        if(!inputChecker.checkDate(inputs[CreateView.INPUT_VIEW_DATE])) { ids.add(CreateView.INPUT_VIEW_DATE); }
        if(!inputChecker.checkPrice(inputs[CreateView.INPUT_VIEW_PRICE])) { ids.add(CreateView.INPUT_VIEW_PRICE); }
        if(!ids.isEmpty()) {
            Iterator<Integer> it = ids.iterator();
            StringBuilder errorMessage = new StringBuilder(createView.getLabel(it.next()));
            while(it.hasNext()) {
                errorMessage.append(",").append(createView.getLabel(it.next()));
            }
            errorMessage.append("が不正です");
            noticeError(errorMessage.toString(), ids);
            return;
        }

        Bundle args = new Bundle();
        args.putStringArray("inputs", inputs);
        getLoaderManager().initLoader(LOADER_ID, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.createPayment(args.getStringArray("inputs"));
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 201) {
                    createView.showMessage("収支情報を登録しました");
                    createView.resetFields();
                    createView.resetErrorCheckers();
                    createView.setToday();
                    settle();
                } else if (code == 400) {
                    createView.showMessage("収支情報の登録に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID);
            }

            @Override
            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }

    private void noticeError(String errorMessage, ArrayList<Integer> ids) {
        createView.showMessage(errorMessage);
        createView.showWrongInput(ids);
    }

    private void settle() {
        getLoaderManager().initLoader(LOADER_ID + 1, new Bundle(), new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getSettlements();
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if (code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        JSONArray settlements = body.getJSONArray("settlements");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.JAPAN);
                        String year_month = sdf.format(Calendar.getInstance().getTime());
                        if (settlements.length() == 0) {
                            createView.showSettlement("0");
                        } else {
                            for (int i = 0; i < settlements.length(); i++) {
                                if (year_month.equals(settlements.getJSONObject(i).getString("date"))) {
                                    createView.showSettlement(settlements.getJSONObject(i).getString("price"));
                                    break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    createView.showMessage("収支の取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 1);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {
            }
        });
    }

    private void getCategories() {
        getLoaderManager().initLoader(LOADER_ID + 2, new Bundle(), new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getCategories();
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        JSONArray categories = body.getJSONArray("categories");
                        String[] names = new String[categories.length()];
                        for(int i=0;i<categories.length();i++) {
                            names[i] = categories.getJSONObject(i).getString("name");
                        }
                        createView.setCategoriesToDialog(names);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    createView.showMessage("カテゴリの取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 2);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }

    public void getDictionaries(String content) {
        Bundle args = new Bundle();
        args.putString("content", content);

        getLoaderManager().initLoader(LOADER_ID + 3, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getDictionaries(args.getString("content"));
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        JSONArray dictionaries = body.getJSONArray("dictionaries");
                        JSONArray categories = dictionaries.getJSONObject(0).getJSONArray("categories");
                        String[] names = new String[categories.length()];
                        for(int i = 0;i < categories.length();i++) {
                            names[i] = categories.getJSONObject(i).getString("name");
                        }
                        createView.setCategoriesToForm(names);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    createView.showMessage("辞書情報の取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 3);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }
}
