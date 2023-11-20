package com.example.Phone.Pay.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 20/11/23
 * @Time ➤➤➤ 3:32 pm
 * @Project ➤➤➤ Phone-Pay-management
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GitDto {
    private String userName;
    private String commitMessage;
    private String commitTime;
}
