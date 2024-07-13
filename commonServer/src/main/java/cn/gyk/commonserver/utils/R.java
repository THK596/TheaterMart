package cn.gyk.commonserver.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回类型配置类（分页）
 */

@Data
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T data; // 泛型类型 T

    // 静态方法，创建成功的响应

    /**
     * 分页查询成功
     * @param msg
     * @param data
     * @return R
     * @param <T>
     */
    public static <T> R<T> success(String msg, T data) {
        return new R<>(Constants.CODE_200, msg, data);
    }

    /**
     * 分页查询失败
     * @param msg
     * @return R
     * @param <T>
     */
    // 失败的响应
    public static <T> R<T> error(String msg) {
        return new R<>(Constants.CODE_401, msg, null);
    }

}
