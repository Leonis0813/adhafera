package com.leonis.android.adhafera.lib;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class HTTPClient extends AsyncTaskLoader<HashMap<String, Object> >{
    private static final String BASE_PATH = "/algieba/api";
    private static final String PORT = "80";

    private HttpURLConnection con;
    private HashMap<String, Object> response;
    private JSONObject requestBody;
    private String baseUrl;
    private String credential;

    public HTTPClient(Context context) {
        super(context);
        InputStream inputStream = context.getClassLoader().getResourceAsStream("web-api.properties");

        try {
            Properties webApiProp = new Properties();
            webApiProp.load(inputStream);
            baseUrl = "http://" + webApiProp.getProperty("host") + ":" + PORT + BASE_PATH;

            String application_id = webApiProp.getProperty("application_id");
            String application_key = webApiProp.getProperty("application_key");
            byte[] credential = Base64.encode((application_id + ":" + application_key).getBytes(), Base64.DEFAULT);
            this.credential = new String(credential, "UTF-8");

            requestBody = new JSONObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPayment(String[] inputs) {
        try {
            con = (HttpURLConnection) new URL(baseUrl + "/payments").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            requestBody.put("payment_type", inputs[4]);
            requestBody.put("date", inputs[0]);
            requestBody.put("content", inputs[1]);
            if(inputs[2] != null) {
                JSONArray categories = new JSONArray();
                for (String category : inputs[2].split(",")) {
                    categories.put(category);
                }
                requestBody.put("categories", categories);
            }
            requestBody.put("price", inputs[3]);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPayments(HashMap<String, String> query) {
        try {
            StringBuilder query_string = new StringBuilder("?");
            query.put("sort", "date");
            query.put("order", "desc");
            for (Map.Entry<String, String> entry : query.entrySet()) {
                query_string.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            query_string = new StringBuilder(query_string.substring(0, query_string.length() - 1));

            con = (HttpURLConnection) new URL(baseUrl + "/payments" + query_string).openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCategories() {
        try {
            con = (HttpURLConnection) new URL(baseUrl + "/categories").openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDictionaries(String content) {
        try {
            String query = content.isEmpty() ? "" : "?content=" + content;
            con = (HttpURLConnection) new URL(baseUrl + "/dictionaries" + query).openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSettlements() {
        try {
            con = (HttpURLConnection) new URL(baseUrl + "/settlements/period?interval=monthly").openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> sendRequest() {
        try {
            con.setRequestProperty("Authorization", "Basic " + credential);

            if(con.getDoOutput()) {
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                out.write(requestBody.toString());
                out.flush();
                out.close();
            }

            con.connect();

            StringBuilder sb = new StringBuilder();
            String st;
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } catch (FileNotFoundException e) {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            while((st = br.readLine()) != null){
                sb.append(st);
            }

            response = new HashMap<>();
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
}
