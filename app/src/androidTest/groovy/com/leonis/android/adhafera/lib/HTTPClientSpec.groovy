package com.leonis.android.adhafera.lib

import android.support.test.runner.AndroidJUnit4
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static android.support.test.InstrumentationRegistry.getContext
import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail
import static org.mockito.Mockito.*
/**
 * Created by leonis on 2018/02/17.
 *
 */
@RunWith(AndroidJUnit4.class)
class HTTPClientSpec {
    static attributes = ["date", "content", "categories", "price", "payment_type"]
    def ret, con

    @Before
    void setUp() throws Exception {
        con = mock(HttpURLConnection.class)
        doNothing().when(con).connect()
    }

    @Test
    void createPayment_OK() {
        String[] inputs = ["2015-01-01", "for http client test", "test", "100", "expense"]
        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def payment = new JSONObject()
        try {
            payment.put("id", 1)
            payment.put("date", inputs[0])
            payment.put("content", inputs[1])
            def categories = new JSONArray()
            inputs[2].split(",").each { def category_name ->
                def category = new JSONObject()
                category.put("id", 1)
                category.put("name", category_name)
                category.put("description", null)
                categories.put(category)
            }
            payment.put("categories", categories)
            payment.put("price", Integer.parseInt(inputs[3]))
            payment.put("payment_type", inputs[4])
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 201, payment.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(201)

        try {
            def responseBody = new JSONObject(ret.get("body").toString())

            assertEquals(inputs[0], responseBody.getString(attributes[0]))
            assertEquals(inputs[1], responseBody.getString(attributes[1]))
            def categories = responseBody.getJSONArray("categories")
            for(int i=0;i<categories.length();i++) {
                assertEquals(inputs[2].split(",")[i], categories.getJSONObject(i).getString("name"))
            }
            assertEquals(Integer.parseInt(inputs[3]), responseBody.getInt(attributes[3]))
            assertEquals(inputs[4], responseBody.getString(attributes[4]))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void createPayment_OK_multipleCategories() {
        String[] inputs = ["2015-01-01", "for http client test", "test,test2", "100", "expense"]
        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def payment = new JSONObject()
        try {
            payment.put("date", inputs[0])
            payment.put("content", inputs[1])
            def categories = new JSONArray()
            def category_names = inputs[2].split(",")
            for (int i = 0;i < category_names.size();i++) {
                def category = new JSONObject()
                category.put("id", i+1)
                category.put("name", category_names[i])
                category.put("description", null)
                categories.put(category)
            }
            payment.put("categories", categories)
            payment.put("price", inputs[3])
            payment.put("payment_type", inputs[4])
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 201, payment.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(201)

        try {
            def responseBody = new JSONObject(ret.get("body").toString())

            assertEquals(inputs[0], responseBody.getString(attributes[0]))
            assertEquals(inputs[1], responseBody.getString(attributes[1]))
            def categories = responseBody.getJSONArray("categories")
            for(int i = 0;i < categories.length();i++) {
                assertEquals(inputs[2].split(",")[i], categories.getJSONObject(i).getString("name"))
            }
            assertEquals(Integer.parseInt(inputs[3]), responseBody.getInt(attributes[3]))
            assertEquals(inputs[4], responseBody.getString(attributes[4]))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void createPayment_NG_categoryIsAbsent() {
        String[] inputs = ["2015-01-01", "for http client test", null, "100", "expense"]
        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def responseBody = new JSONObject()
        try {
            def errors = new JSONArray()
            def error = new JSONObject()
            error.put("error_code", "absent_param_categories")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(400)
        assertErrorCode(["absent_param_categories"])
    }

    @Test
    void createPayment_NG_dateIsInvalid() {
        String[] inputs = ["invalid_date", "テスト用データ", "テスト", "100", "expense"]
        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def responseBody = new JSONObject()
        try {
            def errors = new JSONArray()
            def error = new JSONObject()
            error.put("error_code", "invalid_param_date")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(400)
        assertErrorCode(["invalid_param_date"])
    }

    @Test
    void createPayment_NG_multipleInputsIsAbsent() {
        String[] inputs = ["2015-01-01", null, "テスト", null, "expense"]

        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def responseBody = new JSONObject()
        try {
            def errors = new JSONArray()
            def error = new JSONObject()
            error.put("error_code", "absent_param_content")
            errors.put(error)
            error = new JSONObject()
            error.put("error_code", "absent_param_price")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(400)
        assertErrorCode(["absent_param_content", "absent_param_price"])
    }

    @Test
    void createPayment_NG_multipleInputsIsInvalid() {
        String[] inputs = ["invalid_date", "テスト用データ", "テスト", "-100", "expense"]

        def httpClient = new HTTPClient(getContext())
        httpClient.createPayment(inputs)
        def responseBody = new JSONObject()
        try {
            def errors = new JSONArray()
            def error = new JSONObject()
            error.put("error_code", "invalid_param_date")
            errors.put(error)
            error = new JSONObject()
            error.put("error_code", "invalid_param_price")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(400)
        assertErrorCode(["invalid_param_date", "invalid_param_price"])
    }

    @Test
    void getSettlements_OK() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getSettlements()
        def responseBody = new JSONObject()
        try {
            def settlements = new JSONArray()
            def settlement = new JSONObject()
            settlement.put("date", "2000-01")
            settlement.put("price", "1000")
            settlements.put(settlement)
            settlement = new JSONObject()
            settlement.put("date", "2000-02")
            settlement.put("price", "-100")
            settlements.put(settlement)
            responseBody.put("settlements", settlements)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 200, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(200)

        try {
            def body = new JSONObject(ret.get("body").toString())
            def settlements = body.getJSONArray("settlements")
            assertEquals("2000-01", settlements.getJSONObject(0).get("date"))
            assertEquals("1000", settlements.getJSONObject(0).get("price"))
            assertEquals("2000-02", settlements.getJSONObject(1).get("date"))
            assertEquals("-100", settlements.getJSONObject(1).get("price"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void getCategories_OK() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getCategories()
        def responseBody = new JSONObject()
        try {
            def categories = new JSONArray()
            def category = new JSONObject()
            category.put("id", "1")
            category.put("name", "test")
            category.put("description", "test category")
            categories.put(category)
            category = new JSONObject()
            category.put("id", "2")
            category.put("name", "test2")
            category.put("description", "test category")
            categories.put(category)
            responseBody.put("categories", categories)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 200, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(200)

        try {
            def body = new JSONObject(ret.get("body").toString())
            def categories = body.getJSONArray("categories")
            assertEquals("1", categories.getJSONObject(0).getString("id"))
            assertEquals("test", categories.getJSONObject(0).getString("name"))
            assertEquals("test category", categories.getJSONObject(0).getString("description"))
            assertEquals("2", categories.getJSONObject(1).getString("id"))
            assertEquals("test2", categories.getJSONObject(1).getString("name"))
            assertEquals("test category", categories.getJSONObject(1).getString("description"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void getPayments_OK() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getPayments(new HashMap<String, String>())
        def responseBody = new JSONObject()
        try {
            def payments = new JSONArray()
            def payment = new JSONObject()
            payment.put("id", "1")
            payment.put("payment_type", "income")
            payment.put("date", "1000-01-01")
            payment.put("content", "test")
            def categories = new JSONArray()
            def category = new JSONObject()
            category.put("id", "1")
            category.put("name", "test")
            category.put("description", "test category")
            categories.put(category)
            payment.put("categories", categories)
            payment.put("price", "1000")
            payments.put(payment)
            responseBody.put("payments", payments)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 200, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(200)

        try {
            def body = new JSONObject(ret.get("body").toString())
            def payments = body.getJSONArray("payments")
            assertEquals("1", payments.getJSONObject(0).getString("id"))
            assertEquals("income", payments.getJSONObject(0).getString("payment_type"))
            assertEquals("1000-01-01", payments.getJSONObject(0).getString("date"))
            assertEquals("test", payments.getJSONObject(0).getString("content"))
            assertEquals("1000", payments.getJSONObject(0).getString("price"))

            def categories = payments.getJSONObject(0).getJSONArray("categories")
            assertEquals("1", categories.getJSONObject(0).getString("id"))
            assertEquals("test", categories.getJSONObject(0).getString("name"))
            assertEquals("test category", categories.getJSONObject(0).getString("description"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void getDictionaries_OK() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getDictionaries("test")
        def responseBody = new JSONObject()
        try {
            def dictionaries = new JSONArray()
            def dictionary = new JSONObject()
            dictionary.put("id", "1")
            dictionary.put("phrase", "test")
            dictionary.put("condition", "equal")
            def categories = new JSONArray()
            def category = new JSONObject()
            category.put("id", "1")
            category.put("name", "test")
            category.put("description", "test category")
            categories.put(category)
            dictionary.put("categories", categories)
            dictionaries.put(dictionary)
            responseBody.put("dictionaries", dictionaries)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 200, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertStatusCode(200)

        try {
            def body = new JSONObject(ret.get("body").toString())
            def dictionaries = body.getJSONArray("dictionaries")
            assertEquals("1", dictionaries.getJSONObject(0).getString("id"))
            assertEquals("test", dictionaries.getJSONObject(0).getString("phrase"))
            assertEquals("equal", dictionaries.getJSONObject(0).getString("condition"))

            def categories = dictionaries.getJSONObject(0).getJSONArray("categories")
            assertEquals("1", categories.getJSONObject(0).getString("id"))
            assertEquals("test", categories.getJSONObject(0).getString("name"))
            assertEquals("test category", categories.getJSONObject(0).getString("description"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    private void assertStatusCode(def expectedCode) {
        def actualCode = Integer.parseInt(ret.get("statusCode").toString())
        assertEquals(expectedCode, actualCode)
    }

    private void assertErrorCode(def errorCodes) {
        try {
            def error
            def expectedErrors = new ArrayList<>()
            errorCodes.each { def errorCode ->
                error = new HashMap<>()
                error.put("errorCode", errorCode)
                expectedErrors.add(error)
            }

            def body = new JSONObject(ret.get("body").toString())
            def errors = body.getJSONArray("errors")
            def actualErrors = new ArrayList<>()
            for(int i = 0;i < errors.length();i++) {
                error = new HashMap<>()
                error.put("errorCode", errors.getJSONObject(i).getString("error_code"))
                actualErrors.add(error)
            }

            assertEquals(expectedErrors, actualErrors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    private void setupMock(def httpClient, def statusCode, final def responseBody) {
        try {
            when(con.getRequestMethod()).thenReturn("")
            when(con.getResponseCode()).thenReturn(statusCode)
            when(con.getInputStream()).thenReturn(new InputStream() {
                private int position = 0
                @Override
                int read() throws IOException {
                    return position < responseBody.length() ? responseBody.charAt(position++) : -1
                }

                @Override
                void close() throws IOException {}
            })

            Class<? extends HTTPClient> c = httpClient.getClass()
            def f = c.getDeclaredField("con")
            f.setAccessible(true)
            f.set(httpClient, con)
        } catch (NoSuchFieldException e) {
            e.printStackTrace()
        } catch (IllegalArgumentException e) {
            e.printStackTrace()
        } catch (IllegalAccessException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
}
