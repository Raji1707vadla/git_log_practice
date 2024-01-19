package com.example.Phone.Pay.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 19/01/24
 * @Time ➤➤➤ 9:36 am
 * @Project ➤➤➤ Phone-Pay-management
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PullRequest {
    private String base;
    private String head;
    private String title;
    private String body;
}
