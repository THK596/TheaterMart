package cn.gyk.cartserver.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /////////////////
    //---购物车模块---

    /**
     * 购物车ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 购物车商品数量
     */
    private Integer quantity;

    /**
     * 商品加入购物车给时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 购物车商品更新信息
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /////////////////
    //---商品信息模块---

//    /**
//     * 商品名称
//     */
//    private String goodName;
//
//    /**
//     * 商品分类
//     */
//    private String category;
//
//    /**
//     * 商品价格
//     */
//    private double price;

}
