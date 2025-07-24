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

import java.util.Map;
import java.util.Set;

/**
 * GET Request implementation
 */
public class GetRequest extends HttpRequestWithoutBody {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(GetRequest.class.getName());

    public GetRequest(String url, Set<Header> headers, Map<String, String> queryParameters) {
        super(url, headers, queryParameters);
    }

    public GetRequest(String url, Set<Header> headers) {
        super(url, headers, EmptyParams);
    }

    public GetRequest(String url) {
        super(url, EmptyHeaders, EmptyParams);
    }

    public io.reactivex.Single<HttpResponse<String>> asString() {
        return io.reactivex.Single.create(new io.reactivex.SingleOnSubscribe<HttpResponse<String>>() {
            @Override
            public void subscribe(final io.reactivex.SingleEmitter<HttpResponse<String>> emitter) throws Exception {
                String requestUrl = url;
                if (queryMap != null && !queryMap.isEmpty()) {
                    requestUrl = url + "?" + queries(queryMap);
                }
                com.google.gwt.http.client.RequestBuilder requestBuilder =
                        new com.google.gwt.http.client.RequestBuilder(com.google.gwt.http.client.RequestBuilder.GET, requestUrl);
                requestBuilder.setTimeoutMillis(TIMEOUT);

                if (headerMap != null) {
                    // Set default first
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("accept", "application/json");
                    for (Map.Entry<String, String> entry : headerMap.entries()) {
                        if (entry.getKey() != null && entry.getValue() != null
                                && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                            requestBuilder.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if (authorization != null) {
                    requestBuilder.setHeader("Authorization", authorization);
                }
                requestBuilder.sendRequest(null, new com.google.gwt.http.client.RequestCallback() {
                    public void onResponseReceived(com.google.gwt.http.client.Request request,
                                                   com.google.gwt.http.client.Response response) {
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
        });
    }

    public io.reactivex.Single<HttpResponse<java.io.InputStream>> asBinary() {
        return io.reactivex.Single.create(emitter -> {
            // Implementation would go here - commented out as in original
            // Binary handling in GWT requires special consideration
        });
    }

    public io.reactivex.Single<HttpResponse<JsonNode>> asJson() {
        return io.reactivex.Single.create(new io.reactivex.SingleOnSubscribe<HttpResponse<JsonNode>>() {
            @Override
            public void subscribe(io.reactivex.SingleEmitter<HttpResponse<JsonNode>> e) throws com.google.gwt.http.client.RequestException {
                String requestUrl = url;
                if (queryMap != null && !queryMap.isEmpty()) {
                    requestUrl = url + "?" + queries(queryMap);
                }
                com.google.gwt.http.client.RequestBuilder requestBuilder =
                        new com.google.gwt.http.client.RequestBuilder(com.google.gwt.http.client.RequestBuilder.GET, requestUrl);
                requestBuilder.setTimeoutMillis(TIMEOUT);

                if (headerMap != null) {
                    // Set default first
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("accept", "application/json");
                    for (Map.Entry<String, String> entry : headerMap.entries()) {
                        if (entry.getKey() != null && entry.getValue() != null
                                && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                            requestBuilder.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if (authorization != null) {
                    requestBuilder.setHeader("Authorization", authorization);
                }
                requestBuilder.setCallback(new com.google.gwt.http.client.RequestCallback() {
                    @Override
                    public void onResponseReceived(com.google.gwt.http.client.Request req, com.google.gwt.http.client.Response res) {
                        int statusCode = res.getStatusCode();
                        String statusText = res.getStatusText();
                        String resp = res.getText();
                        e.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    @Override
                    public void onError(com.google.gwt.http.client.Request req, Throwable ex) {
                        e.onError(ex);
                    }
                });
                com.google.gwt.http.client.Request request = requestBuilder.send();
                e.setCancellable(request::cancel);
            }
        });
    }
}