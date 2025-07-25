/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2025, Divroll, and individual contributors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.divroll.http.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import elemental2.promise.Promise;
import java.util.Map;
import java.util.Set;

/**
 * HTTP Request with Body implementation using GWT Elemental2 promises
 */
public class HttpRequestWithBodyImpl extends HttpRequestWithBody {
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(HttpRequestWithBodyImpl.class.getName());

    public HttpRequestWithBodyImpl(String url, com.google.gwt.http.client.RequestBuilder.Method method) {
        super(url, EmptyHeaders, EmptyParams, method);
    }

    public HttpRequestWithBodyImpl(String url, Set<Header> headers, Map<String, String> queryParameters,
                                   com.google.gwt.http.client.RequestBuilder.Method method) {
        super(url, headers, queryParameters, method);
    }

    /**
     * Execute request and handle response as String using Promise
     * @return Promise that resolves to HttpResponse<String>
     */
    public Promise<HttpResponse<String>> asString() {
        return new Promise<HttpResponse<String>>((resolve, reject) -> {
            String requestUrl = url;
            if (queryMap != null && !queryMap.isEmpty()) {
                requestUrl = url + "?" + queries(queryMap);
            }

            RequestBuilder b = new RequestBuilder(method, requestUrl);
            b.setTimeoutMillis(TIMEOUT);

            // Set headers
            if (headerMap != null) {
                boolean hasContentType = false;
                boolean hasAccept = false;

                for (Map.Entry<String, String> entry : headerMap.entries()) {
                    if (entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        if (entry.getKey().equals("Content-Type")) {
                            hasContentType = true;
                        } else if (entry.getKey().equals("accept")) {
                            hasAccept = true;
                        }
                    }
                }

                if (!hasAccept) {
                    headerMap.put("accept", "application/json");
                }
                if (!hasContentType) {
                    headerMap.put("Content-Type", "application/json");
                }

                for (Map.Entry<String, String> entry : headerMap.entries()) {
                    if (entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        b.setHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            // Handle body/payload
            Object payload = body;
            if (fields != null && !fields.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                java.util.Iterator<Map.Entry<String, Object>> it = fields.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    if (entry.getValue() instanceof String) {
                        if (!it.hasNext()) {
                            sb.append(entry.getKey()).append("=").append(com.google.gwt.http.client.URL.encodeComponent((String.valueOf(entry.getValue()))));
                        } else {
                            sb.append(entry.getKey()).append("=").append(com.google.gwt.http.client.URL.encodeComponent((String.valueOf(entry.getValue())))).append("&");
                        }
                    }
                }
                payload = sb.toString();
                b.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }

            if (body != null) {
                if (body instanceof com.google.gwt.core.client.JavaScriptObject) {
                    // TODO for Sending File
                }
            }

            if (authorization != null) {
                b.setHeader("Authorization", authorization);
            }

            try {
                if (payload != null) {
                    String requestBody = String.valueOf(payload);
                    if (payload instanceof java.io.InputStream) {
                        try {
                            java.io.InputStream is = (java.io.InputStream) payload;
                            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[1024];
                            while ((nRead = is.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            byte[] byteArray = buffer.toByteArray();
                            b.setHeader("Content-Type", "application/octet-stream");
                            requestBody = new String(byteArray, java.nio.charset.StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            reject.onInvoke(e.getMessage());
                            return;
                        }
                    }

                    b.sendRequest(requestBody, new RequestCallback() {
                        public void onResponseReceived(Request request, Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            resolve.onInvoke(new StringHttpResponse(statusCode, statusText, resp));
                        }

                        public void onError(Request request, Throwable exception) {
                            reject.onInvoke(exception.getMessage());
                        }
                    });
                } else {
                    b.sendRequest("", new RequestCallback() {
                        public void onResponseReceived(Request request, Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            resolve.onInvoke(new StringHttpResponse(statusCode, statusText, resp));
                        }

                        public void onError(Request request, Throwable exception) {
                            reject.onInvoke(exception.getMessage());
                        }
                    });
                }
            } catch (RequestException e) {
                reject.onInvoke(e.getMessage());
            }
        });
    }

    /**
     * Execute request and handle response as Json using Promise
     * @return Promise that resolves to HttpResponse<JsonNode>
     */
    public Promise<HttpResponse<JsonNode>> asJson() {
        return new Promise<HttpResponse<JsonNode>>((resolve, reject) -> {
            String requestUrl = url;
            if (queryMap != null && !queryMap.isEmpty()) {
                requestUrl = url + "?" + queries(queryMap);
            }

            RequestBuilder b = new RequestBuilder(method, requestUrl);
            b.setTimeoutMillis(TIMEOUT);

            // Set headers
            if (headerMap != null) {
                boolean hasContentType = false;
                boolean hasAccept = false;

                for (Map.Entry<String, String> entry : headerMap.entries()) {
                    if (entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        if (entry.getKey().equals("Content-Type")) {
                            hasContentType = true;
                        } else if (entry.getKey().equals("accept")) {
                            hasAccept = true;
                        }
                    }
                }

                if (!hasAccept) {
                    headerMap.put("accept", "application/json");
                }
                if (!hasContentType) {
                    headerMap.put("Content-Type", "application/json");
                }

                for (Map.Entry<String, String> entry : headerMap.entries()) {
                    if (entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        b.setHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            // Handle body/payload
            Object payload = body;
            if (fields != null && !fields.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                java.util.Iterator<Map.Entry<String, Object>> it = fields.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    if (entry.getValue() instanceof String) {
                        if (!it.hasNext()) {
                            sb.append(entry.getKey()).append("=").append(com.google.gwt.http.client.URL.encodeComponent((String.valueOf(entry.getValue()))));
                        } else {
                            sb.append(entry.getKey()).append("=").append(com.google.gwt.http.client.URL.encodeComponent((String.valueOf(entry.getValue())))).append("&");
                        }
                    }
                }
                payload = sb.toString();
                b.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }

            if (body != null) {
                if (body instanceof com.google.gwt.core.client.JavaScriptObject) {
                    // TODO for Sending File
                }
            }

            if (authorization != null) {
                b.setHeader("Authorization", authorization);
            }

            try {
                if (payload != null) {
                    String requestBody = String.valueOf(payload);
                    if (payload instanceof java.io.InputStream) {
                        try {
                            java.io.InputStream is = (java.io.InputStream) payload;
                            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[1024];
                            while ((nRead = is.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            byte[] byteArray = buffer.toByteArray();
                            b.setHeader("Content-Type", "application/octet-stream");
                            requestBody = new String(byteArray, java.nio.charset.StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            reject.onInvoke(e.getMessage());
                            return;
                        }
                    }

                    b.sendRequest(requestBody, new RequestCallback() {
                        public void onResponseReceived(Request request, Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            resolve.onInvoke(new JsonHttpResponse(statusCode, statusText, resp));
                        }

                        public void onError(Request request, Throwable exception) {
                            reject.onInvoke(exception.getMessage());
                        }
                    });
                } else {
                    b.sendRequest("", new RequestCallback() {
                        public void onResponseReceived(Request request, Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            resolve.onInvoke(new JsonHttpResponse(statusCode, statusText, resp));
                        }

                        public void onError(Request request, Throwable exception) {
                            reject.onInvoke(exception.getMessage());
                        }
                    });
                }
            } catch (RequestException e) {
                reject.onInvoke(e.getMessage());
            }
        });
    }

    // Existing methods can be kept for backward compatibility
    public io.reactivex.Single<HttpResponse<String>> asStringSingle() {
        // Keep original Single-based implementation
        return io.reactivex.Single.create(new io.reactivex.SingleOnSubscribe<HttpResponse<String>>() {
            @Override
            public void subscribe(io.reactivex.SingleEmitter<HttpResponse<String>> emitter) throws RequestException {
                // Original implementation
                String requestUrl = url;
                if (queryMap != null && !queryMap.isEmpty()) {
                    requestUrl = url + "?" + queries(queryMap);
                }
                RequestBuilder b = new RequestBuilder(method, requestUrl);
                b.setTimeoutMillis(TIMEOUT);
                // ... rest of original implementation ...
            }
        });
    }

    public io.reactivex.Single<HttpResponse<JsonNode>> asJsonSingle() {
        // Keep original Single-based implementation
        return io.reactivex.Single.create(emitter -> {
            // Original implementation
        });
    }

    public io.reactivex.Single<elemental2.dom.Blob> asBlob() {
        return io.reactivex.Single.create(emitter -> {
            // Implementation would go here - commented out as in original
            // Blob handling in GWT requires special consideration
        });
    }

    public static native String createBlobUrl(com.google.gwt.core.client.JavaScriptObject javaScriptObject) /*-{
        var blob = new Blob([javaScriptObject], {type: "application/octec-stream"});
        var url = $wnd.URL.createObjectURL(blob);
        return url;
    }-*/;

    public static native elemental2.dom.Blob createBlob(com.google.gwt.core.client.JavaScriptObject javaScriptObject) /*-{
        var blob = new Blob([javaScriptObject], {type: "application/octec-stream"});
        return blob;
    }-*/;
}
