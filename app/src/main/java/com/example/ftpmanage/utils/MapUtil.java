package com.example.ftpmanage.utils;

import java.util.HashMap;

public class MapUtil {
    /**
     * 返回HashMap键值对指定键值并移除左右空白，如果值为空白则返回空字符串
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static String getHMapValue(HashMap<String, Object> hmap, String keyname) {
        return getHMapValue(hmap, keyname, "");
    }

    /**
     * 返回HashMap键值对指定键值并移除左右空白，如果值为空白则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static String getHMapValue(HashMap<String, Object> hmap, String keyname, String defval) {
        if (hmap != null && hmap.containsKey(keyname)) {
            String s = AppUtil.getS(hmap.get(keyname));
            if (AppUtil.isNotEmpty(s)) {
                return s.trim();
            }
        }
        return defval;
    }

    /**
     * 返回HashMap键值对指定键值，如果值为空白则返回空字符串
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static String getHMapVal(HashMap<String, Object> hmap, String keyname) {
        return getHMapVal(hmap, keyname, "");
    }

    /**
     * 返回HashMap键值对指定键值，如果值为空白则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static String getHMapVal(HashMap<String, Object> hmap, String keyname, String defval) {
        if (hmap != null && hmap.containsKey(keyname)) {
            String s = AppUtil.getS(hmap.get(keyname));
            if (AppUtil.isNotEmpty(s)) {
                return s;
            }
        }
        return defval;
    }

    /**
     * 返回HashMap键值对指定键整数值，如果值为空白或转换失败则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static Integer getHMapInt(HashMap<String, Object> hmap, String keyname, Integer defval) {
        String s = getHMapVal(hmap, keyname, null);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    /**
     * 返回HashMap键值对指定键整数值，如果值为空白或转换失败则返回0
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static Integer getHMapInt(HashMap<String, Object> hmap, String keyname) {
        return getHMapInt(hmap, keyname, 0);
    }

    /**
     * 返回HashMap键值对指定键长整数值，如果值为空白或转换失败则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static Long getHMapLong(HashMap<String, Object> hmap, String keyname, Long defval) {
        String s = getHMapVal(hmap, keyname, null);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    /**
     * 返回HashMap键值对指定键长整数值，如果值为空白或转换失败则返回0
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static Long getHMapLong(HashMap<String, Object> hmap, String keyname) {
        return getHMapLong(hmap, keyname, (long)0);
    }

    /**
     * 返回HashMap键值对指定键双精度数值，如果值为空白或转换失败则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static Double getHMapDouble(HashMap<String, Object> hmap, String keyname, Double defval) {
        String s = getHMapVal(hmap, keyname, null);
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    /**
     * 返回HashMap键值对指定键双精度数值，如果值为空白或转换失败则返回0
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static Double getHMapDouble(HashMap<String, Object> hmap, String keyname) {
        return getHMapDouble(hmap, keyname, 0.0);
    }

    /**
     * 返回HashMap键值对指定键编码值，如果值为空白则返回空字符串
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @return
     */
    public static String getHMapEscape(HashMap<String, Object> hmap, String keyname) {
        return AppEscape.urlEn(getHMapVal(hmap, keyname, ""));
    }

    /**
     * 返回HashMap键值对指定键Escape编码值，如果值为空白则返回默认值
     *
     * @param hmap    键值对对象
     * @param keyname 键
     * @param defval  默认值
     * @return
     */
    public static String getHMapEscape(HashMap<String, Object> hmap, String keyname, String defval) {
        return AppEscape.urlEn(getHMapVal(hmap, keyname, defval));
    }
}
