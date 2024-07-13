package cn.gyk.userserver.controller;

import cn.gyk.commonserver.utils.LoginR;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.userserver.domain.dto.LoginParams;
import cn.gyk.userserver.domain.dto.PasswordChangeParameters;
import cn.gyk.userserver.service.DBUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final DBUserService dbUserService;

    public AuthController(DBUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    /**
     * 登录方法：返回一个令牌
     * 用户再次访问时，在请求头 header：携带token
     */
    @Operation(summary = "用户登录验证")
    @PostMapping("/toLogin")
    public LoginR UserLogin(@RequestBody LoginParams loginParams) {
        return dbUserService.UserLogin(loginParams);
    }
}
