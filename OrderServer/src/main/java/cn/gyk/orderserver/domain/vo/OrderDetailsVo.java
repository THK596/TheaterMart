package cn.gyk.orderserver.domain.vo;


import com.baomidou.mybatisplus.annotation.TableId;
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
public class OrderDetailsVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 总价
     */
    private Double totalPrice;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    ///////---订单详情

    /**
     * 商品ID
     */
    private String goodId;

    /**
     * 商品数量
     */
    private String quantity;

    /**
     * 商品单价（购买时）
     */
    private Double orderPrice;
}
