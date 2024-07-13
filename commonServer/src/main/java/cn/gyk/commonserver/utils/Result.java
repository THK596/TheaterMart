package cn.gyk.commonserver.utils;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回类型配置类（CUD）
 */
@Data
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;


    /**
     * CUD操作成功
     * @param data 数据
     * @return Result
     */
    public static Result success(Object data) {
        return new Result(Constants.CODE_200,"success",data);
    }

    /**
     * CUD操作失败
     * @param data 数据
     * @return Result
     */
    public static Result failed(Integer code,Object data) {
        return new Result(code,"failed",data);
    }


    /**
     * CUD权限不足
     * @return null
     */
    public static Result noAuthority() {
        return new Result(Constants.CODE_401,"权限不足",null);
    }

}
