package cn.gyk.userserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改用户密码参数
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeParameters {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String currentPassword;

    /**
     * 用户新密码
     */
    private String newPassword;

//    /**
//     * 邮箱
//     */
//    private String email;
//
//    /**
//     * 手机号码
//     */
//    private String phoneNumber;
}
