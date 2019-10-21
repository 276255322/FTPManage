package com.example.ftpmanage.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    /**
     * 全局Jackso操作对象
     */
    public static ObjectMapper omap = new ObjectMapper();

    /**
     * 判断指定的字符串是否是json
     *
     * @param json
     * @return
     */
    public static boolean isJson(String json) {
        try {
            if (json == null) {
                return false;
            }
            if (json.trim().equals("null")) {
                return false;
            }
            omap.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据字符串返回json对象
     *
     * @return
     */
    public static JsonNode getJsonNode(String json) {
        try {
            JsonNode jn = omap.readTree(json);
            if (jn == null) {
                return null;
            }
            if (jn.toString().trim().equals("null")) {
                return null;
            }
            return jn;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 根据字符串返回json数组
     *
     * @return
     */
    public static ArrayNode getArrayNode(String json) {
        try {
            return (ArrayNode) omap.readTree(json);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 读取JsonNode路径的第一个值并执行Escape解码,值为null或JsonNode路径不存在则返回空字符串
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static String getUnOnlyVals(JsonNode node, String tagPath) {
        return getUnOnlyVals(node, tagPath, "");
    }

    /**
     * 读取JsonNode路径的第一个值并执行Escape解码,值为null或JsonNode路径不存在则返回默认值
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @param defval  默认值
     * @return
     */
    public static String getUnOnlyVals(JsonNode node, String tagPath, String defval) {
        String s = getUnOnlyVal(node, tagPath);
        if (s == null) {
            return defval;
        }
        return s;
    }

    /**
     * 读取JsonNode路径的第一个值并执行Escape解码
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static String getUnOnlyVal(JsonNode node, String tagPath) {
        String s = getOnlyVal(node, tagPath);
        if (s != null) {
            return AppUtil.getUnescape(s);
        }
        return null;
    }

    /**
     * 读取JsonNode路径的第一个值
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static String getOnlyVal(JsonNode node, String tagPath) {
        List<String> values = new ArrayList();
        String[] path = tagPath.split("\\.");
        getJsonValue(node, path, values, 1, true);
        if (values.size() > 0) {
            return values.get(0);
        }
        return null;
    }

    /**
     * 读取JsonNode路径的第一个值,值为空则返回空字符串
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static String getOnlyVals(JsonNode node, String tagPath) {
        String s = getOnlyVal(node, tagPath);
        if (s != null) {
            return s;
        }
        return "";
    }

    /**
     * 读取JsonNode路径的所有值列表并执行Escape解码
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static List<String> getUnVals(JsonNode node, String tagPath) {
        List<String> vlist = getVals(node, tagPath);
        for (int i = 0; i < vlist.size(); i++) {
            String s = vlist.get(i);
            if (s != null) {
                vlist.set(i, AppUtil.getUnescape(s));
            }
        }
        return vlist;
    }

    /**
     * 读取JsonNode路径的所有值列表
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static List<String> getVals(JsonNode node, String tagPath) {
        List<String> values = new ArrayList();
        String[] path = tagPath.split("\\.");
        getJsonValue(node, path, values, 1, false);
        return values;
    }

    /**
     * 递归读取JsonNode路径的所有值列表
     *
     * @param node      JsonNode
     * @param path      路径
     * @param values    值列表
     * @param nextIndex 开始的节点层级，从1开始
     * @param onlyval   只读取JsonNode路径的第一个值
     */
    public static void getJsonValue(JsonNode node, String[] path, List<String> values, int nextIndex, boolean onlyval) {
        if (onlyval && values.size() > 0) {
            return;
        }
        if (AppUtil.objIsEmpty(node)) {
            return;
        }
        // 是路径的最后就直接取值
        if (nextIndex == path.length) {
            if (node.isArray()) {
                for (int i = 0; i < node.size(); i++) {
                    JsonNode child = node.get(i).get(path[nextIndex - 1]);
                    if (AppUtil.objIsEmpty(child)) {
                        continue;
                    }
                    if (onlyval && values.size() > 0) {
                        return;
                    }
                    values.add(child.asText());
                }
            } else {
                JsonNode child = node.get(path[nextIndex - 1]);
                if (!AppUtil.objIsEmpty(child)) {
                    if (onlyval && values.size() > 0) {
                        return;
                    }
                    values.add(child.asText());
                }
            }
            return;
        }
        // 判断是Node下是集合还是一个节点
        node = node.get(path[nextIndex - 1]);
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                getJsonValue(node.get(i), path, values, nextIndex + 1, onlyval);
            }
        } else {
            getJsonValue(node, path, values, nextIndex + 1, onlyval);
        }
    }

    /**
     * 读取JsonNode路径的第一个JsonNode
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static JsonNode getOnlyNodes(JsonNode node, String tagPath) {
        List<JsonNode> values = new ArrayList();
        String[] path = tagPath.split("\\.");
        getJsonNodes(node, path, values, 1, true);
        if (values.size() > 0) {
            JsonNode jn = values.get(0);
            if (jn == null) {
                return null;
            }
            if (jn.toString().trim().equals("null")) {
                return null;
            }
            return jn;
        }
        return null;
    }

    /**
     * 读取JsonNode路径的所有JsonNode列表
     *
     * @param node    JsonNode
     * @param tagPath 路径，层级之间使用.分隔
     * @return
     */
    public static List<JsonNode> getNodes(JsonNode node, String tagPath) {
        List<JsonNode> values = new ArrayList();
        String[] path = tagPath.split("\\.");
        getJsonNodes(node, path, values, 1, false);
        return values;
    }

    /**
     * 递归读取JsonNode路径的所有JsonNode列表
     *
     * @param node      JsonNode
     * @param path      路径
     * @param values    JsonNode列表
     * @param nextIndex 开始的节点层级，从1开始
     * @param onlyval   只读取JsonNode路径的第一个值
     */
    public static void getJsonNodes(JsonNode node, String[] path, List<JsonNode> values, int nextIndex, boolean onlyval) {
        if (onlyval && values.size() > 0) {
            return;
        }
        if (AppUtil.objIsEmpty(node)) {
            return;
        }
        // 是路径的最后就直接取值
        if (nextIndex == path.length) {
            if (node.isArray()) {
                for (int i = 0; i < node.size(); i++) {
                    JsonNode child = node.get(i).get(path[nextIndex - 1]);
                    if (AppUtil.objIsEmpty(child)) {
                        continue;
                    }
                    if (onlyval && values.size() > 0) {
                        return;
                    }
                    if (!child.toString().trim().equals("null")) {
                        values.add(child);
                    }
                }
            } else {
                JsonNode child = node.get(path[nextIndex - 1]);
                if (!AppUtil.objIsEmpty(child)) {
                    if (onlyval && values.size() > 0) {
                        return;
                    }
                    if (!child.toString().trim().equals("null")) {
                        values.add(child);
                    }
                }
            }
            return;
        }
        // 判断是Node下是集合还是一个节点
        node = node.get(path[nextIndex - 1]);
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                getJsonNodes(node.get(i), path, values, nextIndex + 1, onlyval);
            }
        } else {
            getJsonNodes(node, path, values, nextIndex + 1, onlyval);
        }
    }
}
