package cn.gyk.userserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParams implements Serializable {

    private String username;
    private String password;

}
