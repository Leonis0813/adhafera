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
import static org.junit.Assert.fail;
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
        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.createPayment(inputs);
        JSONObject payment = new JSONObject();
        try {
            payment.put("date", inputs[0]);
            payment.put("content", inputs[1]);
            payment.put("category", inputs[2]);
            payment.put("price", inputs[3]);
            payment.put("payment_type", inputs[4]);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 201, payment.toString());

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
            fail();
        }
    }

    @Test
    public void testRegisterPayment_NG_categoryIsAbsent() {
        String[] inputs = {"2015-01-01", "for http client test", null, "100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.createPayment(inputs);
        JSONArray errors = new JSONArray();
        try {
            JSONObject error = new JSONObject();
            error.put("error_code", "absent_param_category");
            errors.put(error);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 400, errors.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"absent_param_category"});
    }

    @Test
    public void testRegisterPayment_NG_dateIsInvalid() {
        String[] inputs = {"invalid_date", "テスト用データ", "テスト", "100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.createPayment(inputs);
        JSONArray errors = new JSONArray();
        try {
            JSONObject error = new JSONObject();
            error.put("error_code", "invalid_param_date");
            errors.put(error);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 400, errors.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"invalid_param_date"});
    }

    @Test
    public void testRegisterPayment_NG_multipleInputsIsAbsent() {
        String[] inputs = {"2015-01-01", null, "テスト", null, "expense"};

        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.createPayment(inputs);
        JSONArray errors = new JSONArray();
        try {
            JSONObject error = new JSONObject();
            error.put("error_code", "absent_param_content");
            errors.put(error);
            error = new JSONObject();
            error.put("error_code", "absent_param_price");
            errors.put(error);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 400, errors.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"absent_param_content", "absent_param_price"});
    }

    @Test
    public void testRegisterPayment_NG_multipleInputsIsInvalid() {
        String[] inputs = {"invalid_date", "テスト用データ", "テスト", "-100", "expense"};

        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.createPayment(inputs);
        JSONArray errors = new JSONArray();
        try {
            JSONObject error = new JSONObject();
            error.put("error_code", "invalid_param_date");
            errors.put(error);
            error = new JSONObject();
            error.put("error_code", "invalid_param_price");
            errors.put(error);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 400, errors.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(400);
        assertErrorCode(new String[]{"invalid_param_date", "invalid_param_price"});
    }

    @Test
    public void testGetSettlement_OK() {
        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.getSettlements();
        JSONArray settlements = new JSONArray();
        try {
            JSONObject settlement = new JSONObject();
            settlement.put("date", "2000-01");
            settlement.put("price", "1000");
            settlements.put(settlement);
            settlement = new JSONObject();
            settlement.put("date", "2000-02");
            settlement.put("price", "-100");
            settlements.put(settlement);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 200, settlements.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(200);

        try {
            JSONArray body = new JSONArray(ret.get("body").toString());
            assertEquals("2000-01", body.getJSONObject(0).get("date"));
            assertEquals("1000", body.getJSONObject(0).get("price"));
            assertEquals("2000-02", body.getJSONObject(1).get("date"));
            assertEquals("-100", body.getJSONObject(1).get("price"));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetCategories_OK() {
        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.getCategories("");
        JSONArray categories = new JSONArray();
        try {
            JSONObject category = new JSONObject();
            category.put("id", "1");
            category.put("name", "test");
            category.put("description", "test category");
            categories.put(category);
            category = new JSONObject();
            category.put("id", "2");
            category.put("name", "test2");
            category.put("description", "test category");
            categories.put(category);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 200, categories.toString());

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
            fail();
        }
    }

    @Test
    public void testGetPayments_OK() {
        HTTPClient httpClient = new HTTPClient(getContext());
        httpClient.getPayments(new HashMap<String, String>());
        JSONArray payments = new JSONArray();
        try {
            JSONObject payment = new JSONObject();
            payment.put("id", "1");
            payment.put("payment_type", "income");
            payment.put("date", "1000-01-01");
            payment.put("content", "test");
            JSONArray categories = new JSONArray();
            JSONObject category = new JSONObject();
            category.put("id", "1");
            category.put("name", "test");
            category.put("description", "test category");
            categories.put(category);
            payment.put("categories", categories);
            payment.put("price", "1000");
            payments.put(payment);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
        setupMock(httpClient, 200, payments.toString());

        ret = httpClient.loadInBackground();

        assertStatusCode(200);

        try {
            JSONArray body = new JSONArray(ret.get("body").toString());
            assertEquals("1", body.getJSONObject(0).getString("id"));
            assertEquals("income", body.getJSONObject(0).getString("payment_type"));
            assertEquals("1000-01-01", body.getJSONObject(0).getString("date"));
            assertEquals("test", body.getJSONObject(0).getString("content"));
            assertEquals("1000", body.getJSONObject(0).getString("price"));

            JSONArray categories = body.getJSONObject(0).getJSONArray("categories");
            assertEquals("1", categories.getJSONObject(0).getString("id"));
            assertEquals("test", categories.getJSONObject(0).getString("name"));
            assertEquals("test category", categories.getJSONObject(0).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
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
            fail();
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