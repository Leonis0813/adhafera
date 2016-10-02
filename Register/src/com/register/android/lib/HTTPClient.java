package com.register.android.lib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class HTTPClient extends AsyncTaskLoader<HashMap<String, Object> >{
  private static final String host = "160.16.66.112";
  private HttpURLConnection con;
  private JSONObject param;
  private String port = "80";

  private HashMap<String, Object> response;

  public HTTPClient(Context context, String[] inputs) {
    super(context);

    try {
      param = new JSONObject();
      param.put("account_type", inputs[4]);
      param.put("date", inputs[0]);
      param.put("content", inputs[1]);
      param.put("category", inputs[2]);
      param.put("price", inputs[3]);
      
      con = (HttpURLConnection) new URL("http://" + host + ":" + port + "/accounts").openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setDoInput(true);
      con.setDoOutput(true);

      JSONObject account = new JSONObject();
      account.put("accounts", param);

      OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
      out.write(account.toString());
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

  public HTTPClient(Context context) {
    super(context);

    try {
      con = (HttpURLConnection) new URL("http://" + host + ":" + port + "/settlement?interval=monthly").openConnection();
      con.setRequestMethod("GET");
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
}
