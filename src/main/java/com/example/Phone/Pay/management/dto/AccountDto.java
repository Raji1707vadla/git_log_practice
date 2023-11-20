package com.example.Phone.Pay.management.dto;

import com.example.Phone.Pay.management.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    Long id;
    String accountNumber;
    Long balance;
    String pin;
    UserDto user;
    String status;
    AccountType accountType;

}
