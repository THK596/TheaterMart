package cn.gyk.orderserver.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGoodsDto {
    private String id;
    private String userId;
    private String totalPrice;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    private Integer quantity;
    private String orderPrice;

    private String goodName;
    private String category;
    private float price;
}
