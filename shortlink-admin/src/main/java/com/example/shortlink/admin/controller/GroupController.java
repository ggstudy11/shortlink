package com.example.shortlink.admin.controller;

import com.example.shortlink.admin.common.convention.result.Result;
import com.example.shortlink.admin.common.convention.result.Results;
import com.example.shortlink.admin.dto.req.GroupAddReqDTO;
import com.example.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/short-link/v1/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public Result<Void> addGroup(@RequestBody GroupAddReqDTO groupAddReqDTO) {
        groupService.save(groupAddReqDTO);
        return Results.success();
    }
}
