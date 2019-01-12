package com.leonis.android.adhafera.lib;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
public class HTTPClientTest {
    private static final String[] attributes = {"date", "content", "category", "price", "payment_type"};
    private HashMap<String, Object> ret;
    private HttpURLConnection con;

    public HTTPClientTest() {}

    @Before
    public void setUp() throws Exception {
        con = mock(HttpURLConnection.class);
        doNothing().when(con).connect();
    }

    @Test
    public void testRegisterPayment_OK() {
        String[] inputs = {"2015-01-01", "for http client test", "test", "100", "expense"};
        HTTPClient httpClient = new HTTPClient(getContext(), inputs);
        setupMock(httpClient, 201, "{\"date\":\"2015-01-01\",\"content\":\"for http client test\",\"category\":\"test\",\"price\":\"100\",\"payment_type\":\"expense\"}");

        ret = httpClient.loadInBackground();

        assertStatusCode(201);

        try {
            HashMap<String, String> expectedPayment = new HashMap<>();
            JSONObject responseBody = new JSONObject(ret.get("body").toString());
            HashMap<String, String> actualPayment = new HashMap<>();

            for (int i = 0; i < attributes.length; i++) {
                expectedPayment.put(attributes[i], inputs[i]);
                actualPayment.put(attributes[i], responseBody.getString(attributes[i]));
            }

            assertEquals(expectedPayment, actualPayment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterPayment_NG_categoryIsAbsent() {
        String[] inputs = {"2015-01-01", "for http client test", null, "100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext(), inputs);
        setupMock(httpClient, 400, "[{\"error_code\":\"absent_param_category\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"absent_param_category"});
    }

    @Test
    public void testRegisterPayment_NG_dateIsInvalid() {
        String[] inputs = {"invalid_date", "テスト用データ", "テスト", "100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext(), inputs);
        setupMock(httpClient, 400, "[{\"error_code\":\"invalid_param_date\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"invalid_param_date"});
    }

    @Test
    public void testRegisterPayment_NG_multipleInputsIsAbsent() {
        String[] inputs = {"2015-01-01", null, "テスト", null, "expense"};

        HTTPClient httpClient = new HTTPClient(getContext(), inputs);
        setupMock(httpClient, 400, "[{\"error_code\":\"absent_param_content\"}, {\"error_code\":\"absent_param_price\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"absent_param_content", "absent_param_price"});
    }

    @Test
    public void testRegisterPayment_NG_multipleInputsIsInvalid() {
        String[] inputs = {"invalid_date", "テスト用データ", "テスト", "-100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext(), inputs);
        setupMock(httpClient, 400, "[{\"error_code\":\"invalid_param_date\"}, {\"error_code\":\"invalid_param_price\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"invalid_param_date", "invalid_param_price"});
    }

    @Test
    public void testGetSettlement_OK() {
        HTTPClient httpClient = new HTTPClient(getContext());
        setupMock(httpClient, 200, "[{\"2000-01\":\"1000\"}, {\"2000-02\":\"-100\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(200);

        try {
            JSONObject body = new JSONObject(ret.get("body").toString());
            assertEquals("1000", body.getString("2000-01"));
            assertEquals("-100", body.getString("2000-02"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCategories_OK() {
        HTTPClient httpClient = new HTTPClient(getContext(), "");
        setupMock(httpClient, 200, "[{\"id\":\"1\", \"name\":\"test\", \"description\":\"test category\"}, {\"id\":\"2\", \"name\":\"test2\", \"description\":\"test category\"}]");

        ret = httpClient.loadInBackground();

        assertStatusCode(200);

        try {
            JSONArray body = new JSONArray(ret.get("body").toString());
            assertEquals("1", body.getJSONObject(0).getString("id"));
            assertEquals("test", body.getJSONObject(0).getString("name"));
            assertEquals("test category", body.getJSONObject(0).getString("description"));
            assertEquals("2", body.getJSONObject(1).getString("id"));
            assertEquals("test2", body.getJSONObject(1).getString("name"));
            assertEquals("test category", body.getJSONObject(1).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void assertStatusCode(int expectedCode) {
        int actualCode = Integer.parseInt(ret.get("statusCode").toString());
        assertEquals(expectedCode, actualCode);
    }

    private void assertErrorCode(String[] errorCodes) {
        try {
            HashMap<String, String> error;

            ArrayList<HashMap<String, String> > expectedErrors = new ArrayList<>();
            for (String errorCode : errorCodes) {
                error = new HashMap<>();
                error.put("errorCode", errorCode);
                expectedErrors.add(error);
            }

            JSONArray jsonArray = new JSONArray(ret.get("body").toString());
            ArrayList<HashMap<String, String> > actualErrors = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++) {
                error = new HashMap<>();
                error.put("errorCode", jsonArray.getJSONObject(i).getString("error_code"));
                actualErrors.add(error);
            }

            assertEquals(expectedErrors, actualErrors);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupMock(HTTPClient httpClient, int statusCode, final String responseBody) {
        try {
            when(con.getRequestMethod()).thenReturn("");
            when(con.getResponseCode()).thenReturn(statusCode);
            when(con.getInputStream()).thenReturn(new InputStream() {
                private int position = 0;
                @Override
                public int read() throws IOException {
                    return position < responseBody.length() ? responseBody.charAt(position++) : -1;
                }

                @Override
                public void close() throws IOException {}
            });

            Class<? extends HTTPClient> c = httpClient.getClass();
            Field f = c.getDeclaredField("con");
            f.setAccessible(true);
            f.set(httpClient, con);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}