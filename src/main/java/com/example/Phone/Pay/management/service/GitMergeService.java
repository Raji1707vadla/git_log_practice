package com.example.Phone.Pay.management.service;

import com.example.Phone.Pay.management.dto.GitCredentialsDto;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 18/01/24
 * @Time ➤➤➤ 10:41 am
 * @Project ➤➤➤ Phone-Pay-management
 */
public interface GitMergeService {
    String mergeBranches(GitCredentialsDto gitCredentialsDto);
}
