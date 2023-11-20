package com.example.Phone.Pay.management.usage_classes;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 29/05/23
 * @Time ➤➤➤ 9:49 am
 * @Project ➤➤➤ Phone-Pay-management
 */
@Setter
@Getter
public class ToMobileNumber {
    private String mobileNo;
    private Long   balance;
    private String pin;
    private String accountNumber;
}
