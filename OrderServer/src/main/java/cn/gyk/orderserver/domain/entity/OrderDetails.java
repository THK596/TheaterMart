package cn.gyk.orderserver.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName(value = "order_details")
public class OrderDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单详情ID
     */
    private String id;

    /**
     * 订单ID（外键）
     */
    private String orderId;

    /**
     * 商品ID
     */
    private String goodId;

    /**
     * 商品数量
     */
    private Double quantity;

    /**
     * 商品单价（购买时）
     */
    private Double orderPrice;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
