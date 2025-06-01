package com.example.shortlink.admin.controller;

import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.common.convention.result.Results;
import com.example.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.example.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.example.shortlink.admin.dto.resp.UserRespDTO;
import com.example.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    @GetMapping("/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    @PostMapping()
    public Result<Void> register(@RequestBody UserRegisterReqDTO userRegisterReqDTO) {
        userService.register(userRegisterReqDTO);
        return Results.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody UserUpdateReqDTO userUpdateReqDTO) {
        userService.update(userUpdateReqDTO);
        return Results.success();
    }
}
