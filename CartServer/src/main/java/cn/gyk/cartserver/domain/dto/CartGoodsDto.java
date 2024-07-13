package cn.gyk.cartserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartGoodsDto {
    private String id;
    private String userId;
    private String productId;
    private String quantity;

    private String goodName;
    private float price;
}
