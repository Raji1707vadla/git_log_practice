package com.example.Phone.Pay.management.dto;

import com.example.Phone.Pay.management.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    Long id;
    String name;
    Integer age;
    RoleType roleType;
    String mobile;
    List<AccountDto> accountDtoList;
}
