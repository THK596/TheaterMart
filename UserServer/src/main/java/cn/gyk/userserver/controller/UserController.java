package cn.gyk.userserver.controller;

import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.userserver.domain.dto.PasswordChangeParameters;
import cn.gyk.userserver.service.impl.DBUserServiceImpl;
import cn.gyk.userserver.service.impl.UserDtoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录管理")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private DBUserServiceImpl dbUserService;
    @Resource
    private UserDtoServiceImpl userDtoService;

    @Operation(summary = "修改用户密码")
    @PostMapping("/changePwd1")
    public Result changePassword(@RequestBody PasswordChangeParameters passwordChangeParameters) {
        try {
            // 调用服务层方法来验证和修改密码
            dbUserService.changePassword(passwordChangeParameters);
            return Result.success(null);
        } catch (Exception e) {
            // 处理异常，如密码不正确、用户不存在等
            return Result.failed(Constants.CODE_400, e.getMessage());
        }
    }

    @Operation(summary = "ID查询用户信息")
    @GetMapping("/getUserById")
    public Result getUserById(@RequestParam("id") String id) {
        return userDtoService.getUserById(id);
    }
}
