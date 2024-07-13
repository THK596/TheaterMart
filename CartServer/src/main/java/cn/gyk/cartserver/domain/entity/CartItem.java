package cn.gyk.cartserver.domain.entity;

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
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项ID
     */
    @Id
    @TableId
    private String id;

    /**
     * 购物车ID
     */
    private String cartId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 数量
     */
    private Integer quantity;

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
