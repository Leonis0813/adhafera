package com.register.android.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.register.android.HTTPClient;

public class HTTPClientTest extends TestCase{
	public HTTPClientTest() {}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testSendAccount_normal() {
		String[] inputs = {"2015-01-01", "テスト用データ", "テスト", "100", "expense"};
		HTTPClient httpClient = setupMock(inputs);

		HashMap<String, Object> expected_response = new HashMap<String, Object>();
		expected_response.put("statusCode", 201);

		HashMap<String, String> a = new HashMap<String, String>();
    	a.put("account_type", "expense");
    	a.put("date", "2015-01-01");
    	a.put("content", "テスト用データ");
    	a.put("category", "テスト");
    	a.put("price", "100");
		expected_response.put("body", a);

		assertEquals(httpClient.sendAccount(), expected_response);
	}

	@Test
	public void testSendAccount_exception_categoryIsAbsent() {
		String[] inputs = {"2015-01-01", "テスト用データ", null, "100", "expense"};
		HTTPClient httpClient = setupMock(inputs);

		HashMap<String, Object> expected_response = new HashMap<String, Object>();
		expected_response.put("statusCode", 400);

		ArrayList<HashMap<String, String> > errors = new ArrayList<HashMap<String, String> >();
		HashMap<String, String> e = new HashMap<String, String>();
		e.put("errorCode", "absent_param_category");
		errors.add(e);
    	expected_response.put("body", errors);

		assertEquals(httpClient.sendAccount(), expected_response);
	}

	@Test
	public void testSendAccount_exception_dateIsInvalid() {
		String[] inputs = {"01-01-2015", "テスト用データ", "テスト", "100", "expense"};
		HTTPClient httpClient = setupMock(inputs);

		HashMap<String, Object> expected_response = new HashMap<String, Object>();
		expected_response.put("statusCode", 400);

		ArrayList<HashMap<String, String> > errors = new ArrayList<HashMap<String, String> >();
		HashMap<String, String> e = new HashMap<String, String>();
		e.put("errorCode", "invalid_param_date");
		errors.add(e);
    	expected_response.put("body", errors);

		assertEquals(httpClient.sendAccount(), expected_response);
	}

	@Test
	public void testSendAccount_exception_multipleInputsIsAbsent() {
		String[] inputs = {"2015-01-01", null, "テスト", null, "expense"};
		HTTPClient httpClient = setupMock(inputs);

		HashMap<String, Object> expected_response = new HashMap<String, Object>();
		expected_response.put("statusCode", 400);

		ArrayList<HashMap<String, String> > errors = new ArrayList<HashMap<String, String> >();
		HashMap<String, String> e1 = new HashMap<String, String>();
		e1.put("errorCode", "absent_param_content");
		errors.add(e1);
		HashMap<String, String> e2 = new HashMap<String, String>();
		e2.put("errorCode", "absent_param_price");
		errors.add(e2);
    	expected_response.put("body", errors);

		assertEquals(httpClient.sendAccount(), expected_response);
	}

	@Test
	public void testSendAccount_exception_multipleInputsIsInvalid() {
		String[] inputs = {"01-01-2015", "テスト用データ", "テスト", "-100", "expense"};
		HTTPClient httpClient = setupMock(inputs);

		HashMap<String, Object> expected_response = new HashMap<String, Object>();
		expected_response.put("statusCode", 400);

		ArrayList<HashMap<String, String> > errors = new ArrayList<HashMap<String, String> >();
		HashMap<String, String> e1 = new HashMap<String, String>();
		e1.put("errorCode", "invalid_param_date");
		errors.add(e1);
		HashMap<String, String> e2 = new HashMap<String, String>();
		e2.put("errorCode", "invalid_param_price");
		errors.add(e2);
    	expected_response.put("body", errors);

		assertEquals(httpClient.sendAccount(), expected_response);
	}

	private HTTPClient setupMock(String[] inputs) {
		HTTPClient httpClient = new HTTPClient(null, inputs);

		Class<? extends HTTPClient> c = httpClient.getClass();
		Field f;
		try {
			f = c.getDeclaredField("port");
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
}
