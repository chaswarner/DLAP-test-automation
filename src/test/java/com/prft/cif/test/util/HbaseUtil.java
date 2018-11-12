package com.prft.cif.test.util;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

public class HbaseUtil {
    public static String getHBaseString(NavigableMap<byte[],byte[]> resultMap, String key){
       return Bytes.toString(resultMap.get(key.getBytes()));
    }

    public static Integer getHBaseInteger(NavigableMap<byte[],byte[]> resultMap, String key){
        try {
            return Bytes.toInt(resultMap.get(key.getBytes()));
        }catch(IllegalArgumentException iae){
            return null;
        }

    }

    public static Long getHBaseLong(NavigableMap<byte[],byte[]> resultMap, String key){
        try {
            return Bytes.toLong(resultMap.get(key.getBytes()));
        }catch(IllegalArgumentException iae){
            return null;
        }

    }

    public static Double getHBaseDouble(NavigableMap<byte[],byte[]> resultMap, String key){
        try {
            return Bytes.toDouble(resultMap.get(key.getBytes()));
        }catch(IllegalArgumentException iae){
            return null;
        }

    }

    public static Float getHBaseFloat(NavigableMap<byte[],byte[]> resultMap, String key){
        try {
            return Bytes.toFloat(resultMap.get(key.getBytes()));
        }catch(IllegalArgumentException iae){
            return null;
        }

    }

    public static Boolean getHBaseBoolean(NavigableMap<byte[],byte[]> resultMap, String key){
        try {
            return Bytes.toBoolean(resultMap.get(key.getBytes()));
        }catch(IllegalArgumentException iae){
            return null;
        }
    }

    public static List<String> getSortedList(NavigableMap<byte[],byte[]> resultMap, String qualifier){

        byte[] keyToGet = qualifier.getBytes();
        byte[] k = resultMap.higherKey(keyToGet);
        String key = (k == null ? null : Bytes.toString(k));

        SortedMap<Integer,String> sm = new TreeMap<>();
        while (key != null && key.startsWith(qualifier)) {
            sm.put(Bytes.toInt(resultMap.get(k)),key.substring(qualifier.length()));
            k = resultMap.higherKey(k);
            key = (k == null ? null : Bytes.toString(k));
        }

        return new ArrayList<>(sm.values());

    }

    public static Map<String,String> getMap(NavigableMap<byte[],byte[]> resultMap, String qualifier){

        byte[] keyToGet = qualifier.getBytes();
        byte[] k = resultMap.higherKey(keyToGet);
        String key = (k == null ? null : Bytes.toString(k));

        Map<String,String> sm = new HashMap<>();
        while (key != null && key.startsWith(qualifier)) {
            sm.put(key.substring(qualifier.length()),Bytes.toString(resultMap.get(k)));
            k = resultMap.higherKey(k);
            key = (k == null ? null : Bytes.toString(k));
        }

        return sm;

    }

}
