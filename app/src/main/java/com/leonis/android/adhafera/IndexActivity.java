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
import com.leonis.android.adhafera.models.Payment;
import com.leonis.android.adhafera.service.InputChecker;
import com.leonis.android.adhafera.views.index.IndexView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by leonis on 2018/12/30.
 */

public class IndexActivity extends AppCompatActivity {
    private static final int LOADER_ID = 0;

    private Context activity;
    private IndexView indexView;
    private InputChecker inputChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        activity = this;
        indexView = findViewById(R.id.index);
        inputChecker = new InputChecker();
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
        if(id == R.id.create) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
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
                        indexView.setCategories(names);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    indexView.showMessage("カテゴリの取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 2);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }

    public void searchPayments(HashMap<String, String> query) {
        ArrayList<String> fields = new ArrayList<>();
        if((query.containsKey("date_before") && inputChecker.isValidDate(query.get("date_before"))) ||
                (query.containsKey("date_after") && inputChecker.isValidDate(query.get("date_after")))) {
            fields.add("期間");
            indexView.showWrongInput("期間");
        }
        if((query.containsKey("price_upper") && inputChecker.isValidPrice(query.get("price_upper"))) ||
                (query.containsKey("price_lower") && inputChecker.isValidPrice(query.get("price_lower")))) {
            fields.add("金額");
            indexView.showWrongInput("金額");
        }
        if(!fields.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for(String field : fields) {
                message.append(field).append(",");
            }
            indexView.showMessage(message.substring(0, message.length() - 1) + "が不正です");
            return;
        }

        Bundle args = new Bundle();
        args.putSerializable("query", query);
        getLoaderManager().initLoader(LOADER_ID, args, new LoaderManager.LoaderCallbacks<HashMap<String, Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public Loader<HashMap<String, Object>> onCreateLoader(int id, Bundle args) {
                HTTPClient httpClient = new HTTPClient(activity);
                httpClient.getPayments(((HashMap<String, String>) args.getSerializable("query")));
                return httpClient;
            }

            @Override
            public void onLoadFinished(Loader<HashMap<String, Object>> loader, HashMap<String, Object> data) {
                int code = Integer.parseInt(data.get("statusCode").toString());
                if(code == 200) {
                    try {
                        JSONObject body = new JSONObject(data.get("body").toString());
                        JSONArray jsonPayments = body.getJSONArray("payments");
                        ArrayList<Payment> payments = new ArrayList<>();
                        for(int i = 0;i < jsonPayments.length();i++) {
                            JSONObject payment = jsonPayments.getJSONObject(i);
                            JSONArray categories = payment.getJSONArray("categories");
                            String[] category_names = new String[categories.length()];
                            for(int j = 0;j < categories.length();j++) {
                                category_names[j] = categories.getJSONObject(j).getString("name");
                            }
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
                                Date date = format.parse(payment.getString("date"));
                                String content = payment.getString("content");
                                int price = payment.getInt("price");
                                String paymentType = payment.getString("payment_type");
                                payments.add(new Payment(date, content, category_names, price, paymentType));
                            } catch (ParseException e) {
                                indexView.showMessage("収支情報の検索に失敗しました");
                            }
                        }
                        indexView.addPayments(payments);
                    } catch (JSONException e) {
                        indexView.showMessage("収支情報の検索に失敗しました");
                    }
                } else {
                    indexView.showMessage("収支情報の検索に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID);
            }

            @Override
            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }
}
