package cn.gyk.commonserver.utils;


import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpStatus;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;


public class LoginR extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginR setData(Object data) {
        put("data",data);
        return this;
    }

    //利用fastjson进行反序列化
    public <T> T getData(TypeReference<T> typeReference) {
        Object data = get("data");	//默认是map
        String jsonString = JSON.toJSONString(data);
        return JSON.parseObject(jsonString, typeReference);
    }

    public LoginR() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    public static LoginR error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static LoginR error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static LoginR error(int code, String msg) {
        LoginR r = new LoginR();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static LoginR ok(String msg) {
        LoginR r = new LoginR();
        r.put("msg", msg);
        return r;
    }

    public static LoginR ok(Map<String, Object> map) {
        LoginR r = new LoginR();
        r.putAll(map);
        return r;
    }

    public static LoginR ok() {
        return new LoginR();
    }

    public LoginR put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    public  Integer getCode() {

        return (Integer) this.get("code");
    }
}
