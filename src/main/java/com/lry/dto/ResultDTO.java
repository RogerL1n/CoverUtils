package com.lry.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class ResultDTO extends HashMap<String, Object> {
    private static final long serialVersionUID = 98756469874569L;

    private final Map<String, Object> dataMap = new HashMap<String, Object>();

    public ResultDTO() {
        put("code", 200);
        put("msg", "success");
    }

    public static ResultDTO error(String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.put("code", 100);
        resultDTO.put("msg", msg);
        return resultDTO;
    }

    public static ResultDTO error(String code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.put("code", code);
        resultDTO.put("msg", msg);
        return resultDTO;
    }

    public static ResultDTO ok(String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.put("msg", msg);
        return resultDTO;
    }

    public static ResultDTO ok(Map<String, Object> map) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.putAll(map);
        return resultDTO;
    }

    public static ResultDTO ok() {
        return new ResultDTO();
    }

    public ResultDTO put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String getCode() {
        return (String) this.get("code");
    }

    // /**
    //  * 设置数据（只可调用一次）。注意：不能与 <code>setData(String key, Object value)</code> 同时使用
    //  * @param data
    //  * @return
    //  */
    // public ResultDTO setData(Object data){
    //     put("data", data);
    //     return this;
    // }

    /**
     * 设置数据（可多次调用）。注意：不能与 <code>setData(Object data)</code> 同时使用
     * @param key
     * @param value
     * @return
     */
    public ResultDTO setData(String key, Object value) {
        put("data", dataMap);
        dataMap.put(key, value);
        return this;
    }

    public ResultDTO setTotals(long totals) {
        put("data", dataMap);
        dataMap.put("count", totals);
        return this;
    }

    /**
     * 获取数据map
     * @return
     */
    public Map<String, Object> getData() {
        return dataMap;
    }

    /**
     * 获取数据json对象
     * @return
     */
    public JSONObject getDataJson() {
        return (JSONObject) JSON.toJSON(dataMap);
    }

    public boolean isFail() {
        return !this.getCode().equals(200);
    }




}
