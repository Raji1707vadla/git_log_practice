package com.example.Phone.Pay.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 19/01/24
 * @Time ➤➤➤ 10:24 am
 * @Project ➤➤➤ Phone-Pay-management
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PullRequestDto {
    private String head;
    private String base;
    private String title;
    private String  body;
}
