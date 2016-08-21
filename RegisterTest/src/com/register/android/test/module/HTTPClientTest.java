package com.register.android.test.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.register.android.lib.HTTPClient;

public class HTTPClientTest extends AndroidTestCase {
  private static final String[] attributes = {"date", "content", "category", "price", "account_type"};
  private HashMap<String, Object> ret;

  public HTTPClientTest() {}

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testSendAccount_normal() {
    String[] inputs = {"2015-01-01", "テスト用データ", "テスト", "100", "expense"};
    HTTPClient httpClient = setupMock(inputs);

    ret = httpClient.sendRequest();

    assertStatusCode(201);

    try {
      HashMap<String, String> expectedAccount = new HashMap<String, String>();
      JSONObject responseBody = new JSONObject(ret.get("body").toString());
      HashMap<String, String> actualAccount = new HashMap<String, String>();

      for(int i=0;i<attributes.length;i++) {
        expectedAccount.put(attributes[i], inputs[i]);
        actualAccount.put(attributes[i], responseBody.getString(attributes[i]));
      }

      assertEquals(expectedAccount, actualAccount);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSendAccount_exception_categoryIsAbsent() {
    String[] inputs = {"2015-01-01", "テスト用データ", null, "100", "expense"};
    HTTPClient httpClient = setupMock(inputs);

    ret = httpClient.sendRequest();

    assertStatusCode(400);
    assertErrorCode(new String[]{"absent_param_category"});
  }

  @Test
  public void testSendAccount_exception_dateIsInvalid() {
    String[] inputs = {"01-01-2015", "テスト用データ", "テスト", "100", "expense"};
    HTTPClient httpClient = setupMock(inputs);

    ret = httpClient.sendRequest();

    assertStatusCode(400);
    assertErrorCode(new String[]{"invalid_param_date"});
  }

  @Test
  public void testSendAccount_exception_multipleInputsIsAbsent() {
    String[] inputs = {"2015-01-01", null, "テスト", null, "expense"};
    HTTPClient httpClient = setupMock(inputs);

    ret = httpClient.sendRequest();

    assertStatusCode(400);
    assertErrorCode(new String[]{"absent_param_content", "absent_param_price"});
  }

  @Test
  public void testSendAccount_exception_multipleInputsIsInvalid() {
    String[] inputs = {"01-01-2015", "テスト用データ", "テスト", "-100", "expense"};
    HTTPClient httpClient = setupMock(inputs);

    ret = httpClient.sendRequest();

    assertStatusCode(400);
    assertErrorCode(new String[]{"invalid_param_date", "invalid_param_price"});
  }

  @Test
  public void testGetSettlement_normal() {
    HTTPClient httpClient = setupMock();

    ret = httpClient.sendRequest();

    assertStatusCode(200);
  }

  private HTTPClient setupMock(String[] inputs) {
    return changePort(new HTTPClient(getContext(), inputs));
  }
  
  private HTTPClient setupMock() {
    return changePort(new HTTPClient(getContext()));
  }

  private HTTPClient changePort(HTTPClient httpClient) {
    Class<? extends HTTPClient> c = httpClient.getClass();
    try {
      Field f = c.getDeclaredField("port");
      f.setAccessible(true);
      f.set(httpClient, "88");
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return httpClient;
  }

  private void assertStatusCode(int expectedCode) {
    int actualCode = Integer.parseInt(ret.get("statusCode").toString());
    assertEquals(expectedCode, actualCode);
  }

  private void assertErrorCode(String[] errorCodes) {
    try {
      HashMap<String, String> error;

      ArrayList<HashMap<String, String> > expectedErrors = new ArrayList<HashMap<String, String> >();
      for(int i=0;i<errorCodes.length;i++) {
        error = new HashMap<String, String>();
        error.put("errorCode", errorCodes[i]);
        expectedErrors.add(error);
      }

      JSONArray array = new JSONArray(ret.get("body").toString());
      ArrayList<HashMap<String, String> > actualErrors = new ArrayList<HashMap<String, String> >();
      for(int i=0;i<array.length();i++) {
        error = new HashMap<String, String>();
        error.put("errorCode", array.getJSONObject(i).getString("error_code"));
        actualErrors.add(error);
      }

      assertEquals(expectedErrors, actualErrors);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
