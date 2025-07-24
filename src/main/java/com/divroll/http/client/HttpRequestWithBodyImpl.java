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

import java.util.Map;
import java.util.Set;

/**
 * HTTP Request with Body implementation (for POST, PUT, etc.)
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

    public io.reactivex.Single<elemental2.dom.Blob> asBlob() {
        return io.reactivex.Single.create(emitter -> {
            // Implementation would go here - commented out as in original
            // Blob handling in GWT requires special consideration
        });
    }

    public io.reactivex.Single<HttpResponse<String>> asString() {
        return io.reactivex.Single.create(new io.reactivex.SingleOnSubscribe<HttpResponse<String>>() {
            @Override
            public void subscribe(io.reactivex.SingleEmitter<HttpResponse<String>> emitter) throws com.google.gwt.http.client.RequestException {
                String requestUrl = url;
                if (queryMap != null && !queryMap.isEmpty()) {
                    requestUrl = url + "?" + queries(queryMap);
                }
                com.google.gwt.http.client.RequestBuilder b = new com.google.gwt.http.client.RequestBuilder(method, requestUrl);
                b.setTimeoutMillis(TIMEOUT);

                if (headerMap != null) {
                    // Check first if Content-Type and accept headers are already set else set defaults
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

                if (payload != null) {
                    String requestBody = String.valueOf(payload);
                    if (payload instanceof java.io.InputStream) {
                        try {
                            com.google.gwt.user.client.Window.alert("Payload is InputStream");
                            java.io.InputStream is = (java.io.InputStream) payload;
                            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[1024];
                            while ((nRead = is.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            byte[] byteArray = buffer.toByteArray();
                            com.google.gwt.user.client.Window.alert("byteArray size=" + byteArray.length);
                            b.setHeader("Content-Type", "application/octet-stream");
                            requestBody = new String(byteArray, java.nio.charset.StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    } else {
                        requestBody = String.valueOf(payload);
                    }
                    b.sendRequest(requestBody, new com.google.gwt.http.client.RequestCallback() {
                        public void onResponseReceived(com.google.gwt.http.client.Request request, com.google.gwt.http.client.Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            emitter.onSuccess(new StringHttpResponse(statusCode, statusText, resp));
                        }
                        public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
                            emitter.onError(exception);
                        }
                    });
                } else {
                    b.sendRequest("", new com.google.gwt.http.client.RequestCallback() {
                        public void onResponseReceived(com.google.gwt.http.client.Request request, com.google.gwt.http.client.Response response) {
                            String resp = response.getText();
                            int statusCode = response.getStatusCode();
                            String statusText = response.getStatusText();
                            emitter.onSuccess(new StringHttpResponse(statusCode, statusText, resp));
                        }
                        public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
                            emitter.onError(exception);
                        }
                    });
                }
            }
        });
    }

    public io.reactivex.Single<HttpResponse<JsonNode>> asJson() {
        return io.reactivex.Single.create(emitter -> {
            String requestUrl = url;
            if (queryMap != null && !queryMap.isEmpty()) {
                requestUrl = url + "?" + queries(queryMap);
            }
            com.google.gwt.http.client.RequestBuilder b = new com.google.gwt.http.client.RequestBuilder(method, requestUrl);
            b.setTimeoutMillis(TIMEOUT);

            if (headerMap != null) {
                // Check first if Content-Type and accept headers are already set else set defaults
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

            if (payload != null) {
                String requestBody = String.valueOf(payload);
                if (payload instanceof java.io.InputStream) {
                    try {
                        com.google.gwt.user.client.Window.alert("Payload is InputStream");
                        java.io.InputStream is = (java.io.InputStream) payload;
                        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                        int nRead;
                        byte[] data = new byte[1024];
                        while ((nRead = is.read(data, 0, data.length)) != -1) {
                            buffer.write(data, 0, nRead);
                        }
                        buffer.flush();
                        byte[] byteArray = buffer.toByteArray();
                        com.google.gwt.user.client.Window.alert("byteArray size=" + byteArray.length);
                        b.setHeader("Content-Type", "application/octet-stream");
                        requestBody = new String(byteArray, java.nio.charset.StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                } else {
                    requestBody = String.valueOf(payload);
                }
                b.sendRequest(requestBody, new com.google.gwt.http.client.RequestCallback() {
                    public void onResponseReceived(com.google.gwt.http.client.Request request, com.google.gwt.http.client.Response response) {
                        String resp = response.getText();
                        int statusCode = response.getStatusCode();
                        String statusText = response.getStatusText();
                        emitter.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
                        emitter.onError(exception);
                    }
                });
            } else {
                b.sendRequest("", new com.google.gwt.http.client.RequestCallback() {
                    public void onResponseReceived(com.google.gwt.http.client.Request request, com.google.gwt.http.client.Response response) {
                        String resp = response.getText();
                        int statusCode = response.getStatusCode();
                        String statusText = response.getStatusText();
                        emitter.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
                        emitter.onError(exception);
                    }
                });
            }
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
