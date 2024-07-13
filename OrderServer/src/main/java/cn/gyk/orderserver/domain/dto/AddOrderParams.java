package cn.gyk.orderserver.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderParams {
    private String userId;
    private String totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;


    private String id;
    private String orderId;
    private String goodId;
    private Integer quantity;
    private Double orderPrice;
}
