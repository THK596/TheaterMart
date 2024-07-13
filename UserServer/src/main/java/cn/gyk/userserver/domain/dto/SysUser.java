package cn.gyk.userserver.domain.dto;



import cn.gyk.userserver.domain.entity.DBUser;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


@EqualsAndHashCode(callSuper = true)
//继承父类的子类，如果callSuper = true，则表示继承的属性也要一致才能返回true
//callSuper = false ,则表示继承的属性可以不一致
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
public class SysUser extends DBUser implements UserDetails {

    private String username;
    private String password;
    private String userAvatar;
    private String id;
    private Integer status;


    public SysUser(DBUser dbUser) {
        this.username = dbUser.getUsername();
        this.password = dbUser.getPassword();
        this.userAvatar = dbUser.getUserAvatar();
        this.id = dbUser.getId();
        this.status = dbUser.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回一个空的权限列表
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == 1;
//        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == 1;
//        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == 1;
//        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
//        return true;
    }
}
