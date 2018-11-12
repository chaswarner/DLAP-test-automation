package com.prft.cif.test.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;
import java.util.regex.Pattern;

/**
 *
 */
public class CIFConvert {

    /**
     *
     */
    private CIFConvert(){}

    /**
     *
     * @param map
     * @return
     * @throws JSONException
     */
    public static JSONObject toJson(Map<String, Object> map) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for (String key : map.keySet()) {

            Object obj = map.get(key);
            if (obj instanceof Map) {
                jsonObject.put(key, toJson((Map) obj));
            } else if (obj instanceof List) {
                jsonObject.put(key, toJson((List) obj));
            } else {
                jsonObject.put(key, map.get(key));
            }
        }


        return jsonObject;
    }

    /**
     *
     * @param list
     * @return
     * @throws JSONException
     */
    public static JSONArray toJson(List<Object> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(toJson((Map) obj));
            } else if (obj instanceof List) {
                jsonArray.put(toJson((List) obj));
            } else {
                jsonArray.put(obj);
            }
        }

        return jsonArray;
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> fromJson(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            Object obj = jsonObject.get(key);

            if (obj instanceof JSONObject) {
                map.put(key, fromJson((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                map.put(key, fromJson((JSONArray) obj));
            } else {
                map.put(key, obj);
            }

        }

        return map;
    }

    /**
     *
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    public static List<Object> fromJson(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<Object>();

        for (int i = 0; i < jsonArray.length(); i++) {

            Object obj = jsonArray.get(i);

            if (obj instanceof JSONObject) {
                list.add(fromJson((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                list.add(fromJson((JSONArray) obj));
            } else {
                list.add(obj);
            }
        }

        return list;
    }

    public static Pattern DATE_PATTERN =
            Pattern.compile (
                    "(?:Sun|Mon|Tue|Wed|Thu|Fri|Sat) " +
                            "(?:Jan|Feb|Mar|Apr|May|June?|July?|Aug|Sept?|Oct|Nov|Dec) " +
                            "\\d\\d \\d\\d:\\d\\d:\\d\\d \\S+ \\d\\d\\d\\d");

    public static Pattern DOUBLE_PATTERN =
            Pattern.compile (
                    "[\\+\\-]?\\d+\\.\\d+(?:[eE][\\+\\-]?\\d+)?");

    public static Pattern INTEGER_PATTERN =
            Pattern.compile (
                    "[\\+\\-]?\\d+");

    public static Object stringToObject (String string)
    {
        if (DATE_PATTERN.matcher (string).matches ())
            return string;
        else if (DOUBLE_PATTERN.matcher (string).matches ())
            return Double.valueOf (string);
        else if (INTEGER_PATTERN.matcher (string).matches ())
            return Integer.valueOf (string);
        else return string;
    }


}