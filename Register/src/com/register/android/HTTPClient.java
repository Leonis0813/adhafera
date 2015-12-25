package com.register.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class HTTPClient extends AsyncTaskLoader<HashMap<String, Object> >{
	private static final String host = "160.16.66.112";
	private static final String path = "accounts";
	
	private HttpURLConnection con;
	private JSONObject param;
	private HashMap<String, Object> response;

	public HTTPClient(Context context, String[] inputs) {
		super(context);

		param = new JSONObject();
		try {
			param.put("account_type", inputs[4]);
			param.put("date", inputs[0]);
			param.put("content", inputs[1]);
			param.put("category", inputs[2]);
			param.put("price", inputs[3]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		response = new HashMap<String, Object>();
	}

	public HashMap<String, Object> sendAccount() {
		URL url;
		try {
			url = new URL("http://" + host + "/" + path);
			con = (HttpURLConnection)url.openConnection();
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
			con.connect();

			StringBuffer sb = new StringBuffer();
	        String st = "";
	        BufferedReader br = null;
	        if(con.getResponseCode() == 201) {
	        	br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	        } else if (con.getResponseCode() == 400) {
	        	br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
	        }
	        while((st = br.readLine()) != null){
	            sb.append(st);
	        }
	        
	        response.put("statusCode", con.getResponseCode());
	        if(con.getResponseCode() == 201) {
	        	JSONObject responseBody = new JSONObject(sb.toString());
	        	HashMap<String, String> a = new HashMap<String, String>();
	        	a.put("account_type", responseBody.getString("account_type"));
	        	a.put("date", responseBody.getString("date"));
	        	a.put("content", responseBody.getString("content"));
	        	a.put("category", responseBody.getString("category"));
	        	a.put("price", responseBody.getString("price"));
	        	response.put("body", a);
	        } else if (con.getResponseCode() == 400) {
	        	JSONArray array = new JSONArray(sb.toString());
	        	ArrayList<HashMap<String, String> > errors = new ArrayList<HashMap<String, String> >();
	        	for(int i=0;i<array.length();i++) {
	        		HashMap<String, String> e = new HashMap<String, String>();
	        		e.put("errorCode", array.getJSONObject(i).getString("error_code"));
	        		errors.add(e);
	        	}
	        	response.put("body", errors);
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public HashMap<String, Object> loadInBackground() {
        return sendAccount();
	}
	
	@Override
    protected void onStartLoading() {
        forceLoad();
    }
}
