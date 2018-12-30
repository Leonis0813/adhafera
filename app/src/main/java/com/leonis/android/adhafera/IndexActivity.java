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
import com.leonis.android.adhafera.views.index.IndexView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by leonis on 2018/12/30.
 */

public class IndexActivity extends AppCompatActivity {
    private static final int LOADER_ID = 0;

    private Context activity;
    private IndexView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        activity = this;
        iv = findViewById(R.id.index);
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
                        iv.setCategories(names);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == 400) {
                    iv.showMessage("カテゴリの取得に失敗しました");
                }
                getLoaderManager().destroyLoader(LOADER_ID + 2);
            }

            public void onLoaderReset(Loader<HashMap<String, Object>> loader) {}
        });
    }
}
