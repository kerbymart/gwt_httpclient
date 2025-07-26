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

public class HeadRequest extends HttpRequestWithoutBody {
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(HeadRequest.class.getName());

    public HeadRequest(String url, Set<Header> headers, Map<String, String> queryParameters) {
        super(url, headers, queryParameters);
    }

    public HeadRequest(String url, Set<Header> headers) {
        super(url, headers, EmptyParams);
    }

    public HeadRequest(String url) {
        super(url, EmptyHeaders, EmptyParams);
    }

    @Override
    public Promise<HttpResponse<String>> asString() {
        return new Promise<HttpResponse<String>>((resolve, reject) -> {
            String requestUrl = buildUrl();
            RequestBuilder rb = new RequestBuilder(RequestBuilder.HEAD, requestUrl);
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

    @Override
    public Promise<HttpResponse<JsonNode>> asJson() {
        return new Promise<HttpResponse<JsonNode>>((resolve, reject) -> {
            String requestUrl = buildUrl();
            RequestBuilder rb = new RequestBuilder(RequestBuilder.HEAD, requestUrl);
            rb.setTimeoutMillis(TIMEOUT);
            setHeaders(rb);
            try {
                rb.sendRequest(null, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        handleJsonResponseForPromise(response, resolve, reject);
                    }
                    @Override
                    public void onError(Request request, Throwable exception) {
                        reject.onInvoke(exception.getMessage());
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