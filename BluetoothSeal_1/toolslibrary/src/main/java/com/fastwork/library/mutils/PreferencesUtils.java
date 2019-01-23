package com.fastwork.library.mutils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by lenovo on 2018/12/25.
 * SharePreferences
 */

public class PreferencesUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";
    private static SharedPreferences.Editor editor = null;
    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences.Editor getEditor(Context context) {
        if (editor == null)
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return editor;
    }

    private static SharedPreferences getSharedPre(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * 保存数据的方法
     */
    public static void put(Context context, String key, Object object) {
        if (object == null) {
            getEditor(context).putString(key, "");
        } else if (object instanceof String) {
            getEditor(context).putString(key, (String) object);
        } else if (object instanceof Integer) {
            getEditor(context).putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            getEditor(context).putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            getEditor(context).putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            getEditor(context).putLong(key, (Long) object);
        } else {
            getEditor(context).putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法
     */
    public static Object get(Context context, String key, Object defaultObject) {
        if (defaultObject == null) {
            return "";
        } else if (defaultObject instanceof String) {
            return getSharedPre(context).getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return getSharedPre(context).getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return getSharedPre(context).getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return getSharedPre(context).getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return getSharedPre(context).getLong(key, (Long) defaultObject);
        }
        return "";
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        getEditor(context).remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        getEditor(context).clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        return getSharedPre(context).contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        return getSharedPre(context).getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}
