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

public class JsonHttpResponse implements HttpResponse<JsonNode> {

    private int status;
    private String statusText;
    private String rawBody;

    public JsonHttpResponse(int status, String statusText, String rawBody) {
        this.status = status;
        this.statusText = statusText;
        this.rawBody = rawBody;
    }

    @Override
    public JsonNode getBody() {
        if(rawBody != null && !rawBody.isEmpty()) {
            JsonNode jsonNode = new JsonNode(rawBody);
            return jsonNode;
        }
        return null;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return null;
    }
}
