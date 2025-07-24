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

public class JavaScriptObjecttHttpResponse implements HttpResponse<JavaScriptObject> {

    private int status;
    private String statusText;
    private JavaScriptObject javaScriptObject;

    public JavaScriptObjecttHttpResponse(int status, String statusText , JavaScriptObject javaScriptObject) {
        this.status = status;
        this.statusText = statusText;
        this.javaScriptObject = javaScriptObject;
    }

    @Override
    public JavaScriptObject getBody() {
        return javaScriptObject;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
