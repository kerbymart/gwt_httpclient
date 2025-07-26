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

import com.divroll.http.client.exceptions.HttpStatusException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import elemental2.dom.Blob;
import elemental2.promise.Promise;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * GET Request implementation with Java 8 callbacks
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

    /**
     * Execute request and handle response as String
     * @return Promise that resolves to HttpResponse<String>
     */
    @Override
    public Promise<HttpResponse<String>> asString() {
        return new Promise<HttpResponse<String>>((resolve, reject) -> {
            String requestUrl = buildUrl();
            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, requestUrl);
            rb.setTimeoutMillis(TIMEOUT);

            setHeaders(rb);

            try {
                rb.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        handleStringResponseForPromise(response, resolve, reject);
                    }

                    @Override
                    public void onError(Request request, Throwable exception) {
                        reject.onInvoke(exception.getMessage());
                    }
                });
            } catch (RequestException e) {
                reject.onInvoke(e.getMessage());
            }
        });
    }

    @Override
    public Promise<HttpResponse<JavaScriptObject>> asJSO() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Promise<HttpResponse<Blob>> asBlob() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Promise<HttpResponse<InputStream>> asBinary() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Execute request and handle response as String (legacy callback version)
     * @param onSuccess callback for successful response
     * @param onError callback for error handling
     * @return Request object that can be used to cancel the request
     */
    public Request asString(Consumer<HttpResponse<String>> onSuccess, Consumer<Throwable> onError) {
        String requestUrl = buildUrl();
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, requestUrl);
        requestBuilder.setTimeoutMillis(TIMEOUT);

        setHeaders(requestBuilder);

        try {
            return requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    try {
                        handleStringResponse(response,
                                httpResponse -> onSuccess.accept(httpResponse),
                                throwable -> onError.accept(throwable));
                    } catch (Exception e) {
                        onError.accept(e);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    onError.accept(exception);
                }
            });
        } catch (RequestException e) {
            onError.accept(e);
            return null;
        }
    }

    @Override
    public Promise<HttpResponse<JsonNode>> asJson() {
        return new Promise<HttpResponse<JsonNode>>((resolve, reject) -> {
            String requestUrl = buildUrl();
            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, requestUrl);
            rb.setTimeoutMillis(TIMEOUT);

            setHeaders(rb);

            try {
                rb.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request req, Response res) {
                        handleJsonResponseForPromise(res, resolve, reject);
                    }

                    @Override
                    public void onError(Request req, Throwable ex) {
                        reject.onInvoke(ex.getMessage());
                    }
                });
            } catch (RequestException rex) {
                reject.onInvoke(rex.getMessage());
            }
        });
    }

    private String buildUrl() {
        String url = this.url;
        if (queryMap != null && !queryMap.isEmpty()) {
            url += "?" + queries(queryMap);
        }
        return url;
    }

    private void setHeaders(RequestBuilder rb) {
        rb.setHeader("Content-Type", "application/json");
        rb.setHeader("accept", "application/json");

        if (headerMap != null) {
            for (Map.Entry<String, String> e : headerMap.entries()) {
                if (e.getKey() != null && e.getValue() != null
                        && !e.getKey().isEmpty() && !e.getValue().isEmpty()) {
                    rb.setHeader(e.getKey(), e.getValue());
                }
            }
        }

        if (authorization != null) {
            rb.setHeader("Authorization", authorization);
        }
    }

    private void handleStringResponse(Response res,
                                      Consumer<HttpResponse<String>> resolve,
                                      Consumer<Throwable> reject) {
        int status = res.getStatusCode();
        String statusText = res.getStatusText();
        String raw = res.getText();

        if (status >= 200 && status < 300) {
            resolve.accept(new StringHttpResponse(status, statusText, raw));
        } else {
            reject.accept(new HttpStatusException(status, statusText, raw));
        }
    }

    private void handleStringResponseForPromise(Response res,
                                                Promise.PromiseExecutorCallbackFn.ResolveCallbackFn<HttpResponse<String>> resolve,
                                                Promise.PromiseExecutorCallbackFn.RejectCallbackFn reject) {
        int status = res.getStatusCode();
        String statusText = res.getStatusText();
        String raw = res.getText();

        if (status >= 200 && status < 300) {
            resolve.onInvoke(new StringHttpResponse(status, statusText, raw));
        } else {
            reject.onInvoke(new HttpStatusException(status, statusText, raw));
        }
    }

    private void handleJsonResponse(Response res,
                                    Consumer<HttpResponse<JsonNode>> resolve,
                                    Consumer<Throwable> reject) {
        int status = res.getStatusCode();
        String statusText = res.getStatusText();
        String raw = res.getText();

        if (status >= 200 && status < 300) {
            resolve.accept(new JsonHttpResponse(status, statusText, raw));
        } else {
            reject.accept(new HttpStatusException(status, statusText, raw));
        }
    }

    private void handleJsonResponseForPromise(Response res,
                                              Promise.PromiseExecutorCallbackFn.ResolveCallbackFn<HttpResponse<JsonNode>> resolve,
                                              Promise.PromiseExecutorCallbackFn.RejectCallbackFn reject) {
        int status = res.getStatusCode();
        String statusText = res.getStatusText();
        String raw = res.getText();

        if (status >= 200 && status < 300) {
            resolve.onInvoke(new JsonHttpResponse(status, statusText, raw));
        } else {
            reject.onInvoke(new HttpStatusException(status, statusText, raw));
        }
    }
}