package cn.gyk.tmapi.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.eternalstone.annotation.SensitiveBye;
import io.github.eternalstone.enums.SensitiveType;
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
@TableName(value = "user")
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @TableId
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户状态
     */
    private Integer status;

//    /**
//     * 用户密码
//     */
//    @SensitiveBye(strategy = SensitiveType.PASSWORD)// 密码数据脱敏
//    private String password;

    /**
     * 邮箱
     */
    @SensitiveBye(strategy = SensitiveType.EMAIL)// 邮箱数据脱敏
    private String email;

    /**
     * 手机号码
     */
    @SensitiveBye(strategy = SensitiveType.MOBILE)// 手机号脱敏
    private String phoneNumber;

    /**
     * 用户地址
     */
    @SensitiveBye(strategy = SensitiveType.ADDRESS)// 地址数据脱敏
    private String address;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
