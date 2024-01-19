package com.example.Phone.Pay.management.service;

import com.example.Phone.Pay.management.entity.PullRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 19/01/24
 * @Time ➤➤➤ 9:43 am
 * @Project ➤➤➤ Phone-Pay-management
 */

public interface PullRequestService {


    String createPullRequest(String base, String head, String title, String body);
}


