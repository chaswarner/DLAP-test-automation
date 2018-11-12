package com.prft.cif.test.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

public class Convert {
    public static JSONObject toJson(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();

        for (String key : map.keySet()) {
            try {
                Object obj = map.get(key);
                if (obj instanceof Map) {
                    jsonObject.put(key, toJson((Map) obj));
                }
                else if (obj instanceof List) {
                    jsonObject.put(key, toJson((List) obj));
                }
                else {
                    jsonObject.put(key, map.get(key));
                }
            }
            catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }

        return jsonObject;
    }

    public static JSONArray toJson(List<Object> list) {
        JSONArray jsonArray = new JSONArray();

        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(toJson((Map) obj));
            }
            else if (obj instanceof List) {
                jsonArray.put(toJson((List) obj));
            }
            else {
                jsonArray.put(obj);
            }
        }

        return jsonArray;
    }

    public static Map<String, Object> fromJson(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            try {
                Object obj = jsonObject.get(key);

                if (obj instanceof JSONObject) {
                    map.put(key, fromJson((JSONObject) obj));
                }
                else if (obj instanceof JSONArray) {
                    map.put(key, fromJson((JSONArray) obj));
                }
                else {
                    map.put(key, obj);
                }
            }
            catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }

        return map;
    }

    public static List<Object> fromJson(JSONArray jsonArray) {
        List<Object> list = new ArrayList<Object>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object obj = jsonArray.get(i);

                if (obj instanceof JSONObject) {
                    list.add(fromJson((JSONObject) obj));
                }
                else if (obj instanceof JSONArray) {
                    list.add(fromJson((JSONArray) obj));
                }
                else {
                    list.add(obj);
                }
            }
            catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }
        return list;
    }
}