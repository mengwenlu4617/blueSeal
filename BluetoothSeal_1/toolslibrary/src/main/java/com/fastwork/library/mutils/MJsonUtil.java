package com.fastwork.library.mutils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2018/12/3.
 * Json 封装
 */

public class MJsonUtil {

    /**
     * 对象转json
     *
     * @param object
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        try {
            Gson gson = new Gson();
            gsonString = gson.toJson(object);
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gsonString;
    }

    /**
     * 转成bean
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(gsonString, cls);
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return t;
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成list中有map的
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        try {
            Gson gson = new Gson();
            list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * 转成map的
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        try {
            Gson gson = new Gson();
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * 根据key返回value
     */
    public static String getJsonValueByKey(JSONObject obj, String key) {
        String str = "";
        try {
            if (obj.has(key)) {
                str = obj.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * json字符串生成JSONObject对象
     *
     * @param json
     * @return
     */
    public static JSONObject string2JSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            JSONTokener jsonParser = new JSONTokener(json);
            jsonObject = (JSONObject) jsonParser.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * List集合转换为Json
     *
     * @param list
     * @return
     */
    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(String.valueOf(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * 对象数组转换为Json
     *
     * @param array
     * @return
     */
    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(String.valueOf(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * Set集合转为Json
     *
     * @param set
     * @return
     */
    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(String.valueOf(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * 字符数组转List集合
     *
     * @param array String[] array = new String[] {"zhu", "wen", "tao"};
     * @return List
     */
    public static List<String> array2List(String[] array) {
        // String数组转List集合
        return Arrays.asList(array);
    }

    /**
     * @param list ArrayList<String> list = new ArrayList<>();
     * @return String[]
     */
    public static String[] list2Array(List<String> list) {
        String[] array = list.toArray(new String[list.size()]);
        for (String anArray : array) {
            System.out.print(anArray + "  ");
        }
        return array;
    }

    /**
     * Map集合转换为Json
     *
     * @param map
     * @return
     */
    public static String hashMapToJson(Map<?, ?> map) {
        if (map == null) return "";
        StringBuilder string = new StringBuilder();
        string.append("{");
        for (Object obj : map.entrySet()) {
            Map.Entry e = (Map.Entry) obj;
            string.append("\"").append(e.getKey()).append("\":");
            string.append("\"").append(e.getValue()).append("\",");
        }
        string = new StringBuilder(string.substring(0, string.lastIndexOf(",")));
        string.append("}");
        return string.toString();
    }

    /**
     * 将map参数转成json
     */
    public static String getMd5Data(HashMap newMap, String method) {
        if (newMap == null || newMap.size() < 1) return "";
        StringBuilder result = new StringBuilder();
        Object[] objKey = newMap.keySet().toArray();
        Arrays.sort(objKey);
        for (Object aKey : objKey) {
            if (aKey != null & newMap.get(aKey) != null) {
                try {
                    result.append(newMap.get(aKey).toString()).append(method);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        return MD5Util.getMD5UpperString(result.toString().trim()).toUpperCase();
    }
}
