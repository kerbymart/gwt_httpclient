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

import com.google.common.collect.ArrayListMultimap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

abstract class HttpRequestWithoutBody extends HttpRequest {
  public HttpRequestWithoutBody(String url, Set<Header> headers, Map<String, String> queryParameters) {
    super(url, headers, queryParameters);
  }

  public HttpRequestWithoutBody header(String header, String value) {
    if (headerMap == null) {
      headerMap = ArrayListMultimap.create();
    }
    if (value != null) {
      headerMap.put(header, value);
    }
    return this;
  }

  public HttpRequestWithoutBody queryString(String name, String value) {
    if (queryMap == null) {
      queryMap = new LinkedHashMap<String, String>();
    }
    queryMap.put(name, value);
    return this;
  }

  public HttpRequestWithoutBody basicAuth(String username, String password) {
    authorization = "Basic " + Base64.btoa(username + ":" + password);
    return this;
  }

  // Abstract methods to be implemented by concrete classes
  public abstract io.reactivex.Single<HttpResponse<String>> asString();
  public abstract io.reactivex.Single<HttpResponse<JsonNode>> asJson();
}
