package cn.gyk.tmapi.client;

import cn.gyk.commonserver.utils.Result;
import cn.gyk.tmapi.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("UserServer")
public interface UserClient {
    @GetMapping("api/user/getUserById")
    UserDto getUserById(@RequestParam("id") String id);
}
