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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

abstract class HttpRequest {
  protected static final Set<Header> EmptyHeaders = new HashSet<Header>();
  protected static final Map<String, String> EmptyParams = new HashMap<String, String>();

  protected String url;
  protected Multimap<String, String> headerMap;
  protected Map<String, String> queryMap;
  protected String authorization = null;
  protected int TIMEOUT = 60000;

  public HttpRequest(String url, Set<Header> headers, Map<String, String> queryParameters) {
    this.url = url;
    this.headerMap = ArrayListMultimap.create();
    this.queryMap = queryParameters != null ? new LinkedHashMap<String, String>(queryParameters) : null;

    // Convert headers to multimap
    if (headers != null) {
      for (Header header : headers) {
        this.headerMap.put(header.getName(), header.getValue());
      }
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Multimap<String, String> getHeaders() {
    return headerMap;
  }

  public Map<String, String> getQueryParameters() {
    return queryMap;
  }

  public void setTimeout(int timeout) {
    this.TIMEOUT = timeout;
  }

  protected String queries(Map<String, String> parmsRequest) {
    if (parmsRequest == null || parmsRequest.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (String k : parmsRequest.keySet()) {
      String vx = com.google.gwt.http.client.URL.encodeComponent(parmsRequest.get(k));
      if (sb.length() > 0) {
        sb.append("&");
      }
      sb.append(k).append("=").append(vx);
    }
    return sb.toString();
  }
}

