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

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonNode {

    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private boolean array;

    public JsonNode(String json) {
        if (json == null || "".equals(json.trim())) {
            jsonObject = new JSONObject(JSONParser.parseStrict(json).isObject());
        } else {
            try {
                jsonObject = new JSONObject(JSONParser.parseStrict(json).isObject());
            } catch (JSONException e) {
                // It may be an array
                try {
                    jsonArray = new JSONArray(JSONParser.parseStrict(json).isArray());
                    array = true;
                } catch (JSONException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    public JSONObject getObject() {
        return this.jsonObject;
    }

    public JSONArray getArray() {
        JSONArray result = this.jsonArray;
        if (array == false) {
            result = new JSONArray();
            result.put(this.jsonObject);
        }
        return result;
    }

    public boolean isArray() {
        return this.array;
    }

    @Override
    public String toString() {
        if (isArray()) {
            if (jsonArray == null)
                return null;
            return jsonArray.toString();
        }
        if (jsonObject == null)
            return null;
        return jsonObject.toString();
    }

}
