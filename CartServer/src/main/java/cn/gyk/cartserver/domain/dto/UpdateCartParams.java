package cn.gyk.cartserver.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cart_item")
public class UpdateCartParams implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * cartItem的ID
     */
    @Id
    @TableId
    private String id;

    /**
     * 用户ID
     */
//    @TableField(exist = false) // 忽略映射到数据库表
    private String userId;

    /**
     * 购物车ID(外键)
     */
    private String cartId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 购物车产品ID
     */
    private String productId;

    /**
     * 购物车商品创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 购物车商品更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
