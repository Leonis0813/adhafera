package com.leonis.android.adhafera.lib;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by leonis on 2018/02/11.
 */

public class HTTPClient extends AsyncTaskLoader<HashMap<String, Object> >{
    private static final String host = "160.16.66.112";
    private static final String base_path = "/algieba/api";
    private HttpURLConnection con;
    private JSONObject param;
    private String port = "80";
    private static final String application_id = "68c58a4f26cb84bd";
    private static final String application_key = "a469856b9b1b873a5230a0e1b36ee170";

    private HashMap<String, Object> response;

    public HTTPClient(Context context, String[] inputs) {
        super(context);

        try {
            param = new JSONObject();
            param.put("payment_type", inputs[4]);
            param.put("date", inputs[0]);
            param.put("content", inputs[1]);
            param.put("category", inputs[2]);
            param.put("price", inputs[3]);

            con = (HttpURLConnection) new URL("http://" + host + ":" + port + base_path + "/payments").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic " + credential());
            con.setDoInput(true);
            con.setDoOutput(true);

            JSONObject payment = new JSONObject();
            payment.put("payments", param);

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(payment.toString());
            out.flush();
            out.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HTTPClient(Context context, String keyword) {
        super(context);

        try {
            keyword = keyword == "" ? "" : "?keyword=" + keyword;
            con = (HttpURLConnection) new URL("http://" + host + ":" + port + base_path + "/categories" + keyword).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + credential());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HTTPClient(Context context) {
        super(context);

        try {
            con = (HttpURLConnection) new URL("http://" + host + ":" + port + base_path + "/settlement?interval=monthly").openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + credential());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> sendRequest() {
        try {
            con.connect();

            StringBuffer sb = new StringBuffer();
            String st = "";
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } catch (FileNotFoundException e) {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            while((st = br.readLine()) != null){
                sb.append(st);
            }

            response = new HashMap<String, Object>();
            response.put("statusCode", con.getResponseCode());
            response.put("body", sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public HashMap<String, Object> loadInBackground() {
        return sendRequest();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private String credential() {
        byte[] credential = Base64.encode((application_id + ":" + application_key).getBytes(), Base64.DEFAULT);
        try {
            return new String(credential, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

}
