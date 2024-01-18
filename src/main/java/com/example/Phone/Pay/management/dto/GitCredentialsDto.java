package com.example.Phone.Pay.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 18/01/24
 * @Time ➤➤➤ 10:37 am
 * @Project ➤➤➤ Phone-Pay-management
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GitCredentialsDto {
    private String repository;
    private String fromBranch;
    private String toBranch;
    private String userName;
    private String password;
}
