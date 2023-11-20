package com.example.Phone.Pay.management.dto;


import com.example.Phone.Pay.management.entity.User;
import com.example.Phone.Pay.management.enums.RoleType;
import lombok.Data;

@Data
public class SignInResponseDto {

    private Long id;

    private String mobile;

    private RoleType roleType;

    private String name;

    private String token;


    public SignInResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.mobile = user.getMobile();
        this.roleType = user.getRoleType();
    }

    public SignInResponseDto() {
        super();
    }

}
