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

import com.google.gwt.http.client.RequestBuilder;

public class HttpClient {
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    public static HeadRequest head(String url) {
        return new HeadRequest(url);
    }

    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    public static PutRequest put(String url) {
        return new PutRequest(url);
    }

    public static DeleteRequest delete(String url) {
        return new DeleteRequest(url);
    }

    public static PatchRequest patch(String url) {
        return new PatchRequest(url);
    }
}
