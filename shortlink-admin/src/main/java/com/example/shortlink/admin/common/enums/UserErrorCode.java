package com.example.shortlink.admin.common.enums;

import com.example.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum UserErrorCode implements IErrorCode {

    USER_NOT_EXIST("B002001", "用户不存在"),
    USER_NAME_EXIST("B002002", "用户名已存在"),
    USER_CREATE_ERROR("B002003", "用户创建失败"),
    USER_EXIST("B002004", "用户已存在");

    private final String code;

    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
