/*
 *  Divroll, Platform for Hosting Static Sites
 *  Copyright 2025, Divroll, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.divroll.http.client;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.logging.Level;
import java.util.logging.Logger;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HttpClientTest extends GWTTestCase {
    static final Logger logger = Logger.getLogger(HttpClientTest.class.getName());
    @Override
    public String getModuleName() {
        return "com.divroll.http.HttpClient";
    }
    public void testGet() {
        delayTestFinish(5000);
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        HttpClient.get(url)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .asJson()
                .then(response -> {
                    try {
                        if (response == null) {
                            logger.severe("Test failed with null response");
                            fail("Null response received");
                            finishTest();
                            return null;
                        }
                        logger.info("Status: " + response.getStatus());
                        logger.info("Status Text: " + response.getStatusText());

                        if (response.getStatus() != 200) {
                            logger.severe("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            fail("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            finishTest();
                            return null;
                        }

                        JsonNode jsonNode = response.getBody();
                        if (jsonNode == null) {
                            logger.severe("Test failed with null body");
                            fail("Null response body");
                            finishTest();
                            return null;
                        }

                        if (jsonNode.isArray()) {
                            logger.severe("Expected JSON object, got array: " + jsonNode.toString());
                            fail("Expected JSON object, got array");
                            finishTest();
                            return null;
                        }

                        JSONObject jsonObject = jsonNode.getObject();
                        if (jsonObject == null) {
                            logger.severe("Expected JSONObject but got null");
                            fail("Expected JSONObject but got null");
                            finishTest();
                            return null;
                        }

                        logger.info("Response body: " + jsonObject.toString());
                        assertTrue(jsonObject.has("userId"));
                        assertTrue(jsonObject.has("id"));
                        assertTrue(jsonObject.has("title"));
                        assertTrue(jsonObject.has("body"));

                        assertEquals(1L, jsonObject.getLong("id"));
                        assertEquals(1L, jsonObject.getLong("userId"));
                        assertTrue(jsonObject.getString("title").length() > 0);
                        assertTrue(jsonObject.getString("body").length() > 0);

                        logger.info("Test completed successfully");
                        finishTest();
                        return null;
                    } catch (Exception e) {
                        logger.log(Level.INFO, "Error processing response: " + e.getMessage(), e);
                        fail("Response processing failed: " + e.getMessage());
                        finishTest();
                        return null;
                    }
                }, error -> {
                    String errorMsg = error != null ? error.toString() : "Unknown error";
                    logger.severe("HTTP Request failed: " + errorMsg);
                    fail("Request failed: " + errorMsg);
                    finishTest();
                    return null;
                });
    }
    public void testPostJson() {
        delayTestFinish(10000);

        JSONObject payload = new JSONObject();
        payload.put("title", "foo");
        payload.put("body", "bar");
        payload.put("userId", 1);

        HttpClient.post("https://jsonplaceholder.typicode.com/posts")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(payload.toString())
                .asJson()
                .then(response -> {
                    try {
                        if (response == null) {
                            logger.severe("Test failed with null response");
                            fail("Null response received");
                            finishTest();
                            return null;
                        }

                        logger.info("Status: " + response.getStatus());
                        logger.info("Status Text: " + response.getStatusText());

                        if (response.getStatus() != 201) {
                            logger.severe("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            fail("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            finishTest();
                            return null;
                        }

                        JsonNode jsonNode = response.getBody();
                        if (jsonNode == null) {
                            logger.severe("Test failed with null body");
                            fail("Null response body");
                            finishTest();
                            return null;
                        }

                        if (jsonNode.isArray()) {
                            logger.severe("Expected JSON object, got array");
                            fail("Expected JSON object, got array");
                            finishTest();
                            return null;
                        }

                        JSONObject jsonObject = jsonNode.getObject();
                        if (jsonObject == null) {
                            logger.severe("Expected JSONObject but got null");
                            fail("Expected JSONObject but got null");
                            finishTest();
                            return null;
                        }

                        logger.info("Response body: " + jsonObject.toString());

                        assertNotNull(jsonObject.get("id"));
                        assertEquals(1L, jsonObject.getLong("userId"));
                        assertEquals("foo", jsonObject.getString("title"));
                        assertEquals("bar", jsonObject.getString("body"));

                        logger.info("Test completed successfully");
                        finishTest();
                        return null;
                    } catch (Exception e) {
                        logger.log(Level.INFO, "Error processing response: " + e.getMessage(), e);
                        fail("Response processing failed: " + e.getMessage());
                        finishTest();
                        return null;
                    }
                }, error -> {
                    String errorMsg = error != null ? error.toString() : "Unknown error";
                    logger.severe("HTTP Request failed: " + errorMsg);
                    fail("Request failed: " + errorMsg);
                    finishTest();
                    return null;
                });
    }
    public void testPut() {
        delayTestFinish(10000);

        JSONObject payload = new JSONObject();
        payload.put("id", 1);
        payload.put("title", "foo");
        payload.put("body", "bar");
        payload.put("userId", 1);

        HttpClient.put("https://jsonplaceholder.typicode.com/posts/1")
                .body(payload.toString())
                .asJson()
                .then(response -> {
                    try {
                        if (response == null) {
                            logger.severe("Test failed with null response");
                            fail("Null response received");
                            finishTest();
                            return null;
                        }

                        logger.info("Status: " + response.getStatus());
                        logger.info("Status Text: " + response.getStatusText());

                        if (response.getStatus() != 200) {
                            logger.severe("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            fail("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            finishTest();
                            return null;
                        }

                        JsonNode jsonNode = response.getBody();
                        if (jsonNode == null) {
                            logger.severe("Test failed with null body");
                            fail("Null response body");
                            finishTest();
                            return null;
                        }

                        if (jsonNode.isArray()) {
                            logger.severe("Expected JSON object, got array");
                            fail("Expected JSON object, got array");
                            finishTest();
                            return null;
                        }

                        JSONObject jsonObject = jsonNode.getObject();
                        if (jsonObject == null) {
                            logger.severe("Expected JSONObject but got null");
                            fail("Expected JSONObject but got null");
                            finishTest();
                            return null;
                        }

                        logger.info("Response body: " + jsonObject.toString());

                        assertEquals(1L, jsonObject.getLong("id"));
                        assertEquals(1L, jsonObject.getLong("userId"));
                        assertEquals("foo", jsonObject.getString("title"));
                        assertEquals("bar", jsonObject.getString("body"));

                        logger.info("Test completed successfully");
                        finishTest();
                        return null;
                    } catch (Exception e) {
                        logger.log(Level.INFO, "Error processing response: " + e.getMessage(), e);
                        fail("Response processing failed: " + e.getMessage());
                        finishTest();
                        return null;
                    }
                }, error -> {
                    String errorMsg = error != null ? error.toString() : "Unknown error";
                    logger.severe("HTTP Request failed: " + errorMsg);
                    fail("Request failed: " + errorMsg);
                    finishTest();
                    return null;
                });
    }
    public void testDelete() {
        delayTestFinish(10000);

        HttpClient.delete("https://jsonplaceholder.typicode.com/posts/1")
                .asJson()
                .then(response -> {
                    try {
                        if (response == null) {
                            logger.severe("Test failed with null response");
                            fail("Null response received");
                            finishTest();
                            return null;
                        }

                        logger.info("Status: " + response.getStatus());
                        logger.info("Status Text: " + response.getStatusText());

                        if (response.getStatus() != 200) {
                            logger.severe("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            fail("HTTP error: " + response.getStatus() + " " + response.getStatusText());
                            finishTest();
                            return null;
                        }

                        JsonNode jsonNode = response.getBody();
                        if (jsonNode == null) {
                            logger.severe("Test failed with null body");
                            fail("Null response body");
                            finishTest();
                            return null;
                        }

                        // For DELETE, jsonplaceholder returns an empty object {}
                        if (jsonNode.isArray()) {
                            logger.severe("Expected empty object, got array");
                            fail("Expected empty object, got array");
                            finishTest();
                            return null;
                        }

                        JSONObject jsonObject = jsonNode.getObject();
                        if (jsonObject == null) {
                            logger.severe("Expected empty JSONObject but got null");
                            fail("Expected empty JSONObject but got null");
                            finishTest();
                            return null;
                        }

                        logger.info("Response body: " + jsonObject.toString());
                        assertTrue(jsonObject.keySet().isEmpty());

                        logger.info("Test completed successfully");
                        finishTest();
                        return null;
                    } catch (Exception e) {
                        logger.log(Level.INFO, "Error processing response: " + e.getMessage(), e);
                        fail("Response processing failed: " + e.getMessage());
                        finishTest();
                        return null;
                    }
                }, error -> {
                    String errorMsg = error != null ? error.toString() : "Unknown error";
                    logger.severe("HTTP Request failed: " + errorMsg);
                    fail("Request failed: " + errorMsg);
                    finishTest();
                    return null;
                });
    }
    public void testClientError() {
        delayTestFinish(5000);

        HttpClient.get("https://jsonplaceholder.typicode.com/invalid-endpoint")
                .asJson()
                .then(response -> {
                    fail("Expected error response but got success");
                    finishTest();
                    return null;
                }, error -> {
                    logger.info("Received expected error");
                    finishTest();
                    return null;
                });
    }
    public void testServerError() {
        delayTestFinish(5000);

        HttpClient.get("https://jsonplaceholder.typicode.com/posts/999999999")
                .asJson()
                .then(response -> {
                    fail("Expected server error but got response: " + response.getStatus());
                    finishTest();
                    return null;
                }, error -> {
                    logger.info("Received expected error: " + error);
                    finishTest();
                    return null;
                });
    }
    /*
     * Keeping these tests commented out as they either don't fit well with the jsonplaceholder API
     * or are for functionality we're not currently testing (like binary responses)
     */
    /*
    public void testPostFormField() throws Exception{
        // This test doesn't directly translate to jsonplaceholder's API
        // Since jsonplaceholder expects proper JSON objects for POST, not form fields
    }
    public void testBlockingGet() {
        // Blocking operations aren't supported in the promise-based API
    }
    public void testBinaryGet() {
        // Requires a binary endpoint which jsonplaceholder doesn't provide
    }
    public void testBinaryPost() {
        // Requires a binary endpoint which jsonplaceholder doesn't provide
    }
    */
    public static native String createBlobUrl(JavaScriptObject blob) /*-{
        var url = $wnd.URL.createObjectURL(blob);
        return url;
    }-*/;
}
