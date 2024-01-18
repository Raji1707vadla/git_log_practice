package com.example.Phone.Pay.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 22/11/23
 * @Time ➤➤➤ 1:01 pm
 * @Project ➤➤➤ Phone-Pay-management
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GitRequestDto {
    private String userName;
    private String password;
    private String repoUrl;
    private String branchName;
}
