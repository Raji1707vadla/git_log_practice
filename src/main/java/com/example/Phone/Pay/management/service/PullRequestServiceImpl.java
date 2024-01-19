package com.example.Phone.Pay.management.service;

import com.example.Phone.Pay.management.dto.PullRequestDto;
import com.example.Phone.Pay.management.entity.PullRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 19/01/24
 * @Time ➤➤➤ 9:43 am
 * @Project ➤➤➤ Phone-Pay-management
 */
@Service
public class PullRequestServiceImpl implements PullRequestService{
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String OWNER = "Raji1707vadla";
    private static final String REPO = "git_log_practice";
    private static final String TOKEN = "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";
@Override
    public  String createPullRequest(PullRequestDto dto) {
        String apiUrl = String.format("%s/repos/%s/%s/pulls", GITHUB_API_URL, OWNER, REPO);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        RestTemplate restTemplate = new RestTemplate();
        PullRequest pullRequest = new PullRequest(dto.getBase(), dto.getHead(), dto.getTitle(), dto.getBody());
        HttpEntity<PullRequest> requestEntity = new HttpEntity<>(pullRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Pull request created successfully!");
            return "Pull request created successfully!";
        } else {
            System.err.println("Failed to create pull request. Status code: " + response.getStatusCode());
            return "Failed to create pull request. Status code: " + response.getStatusCode();
        }
    }
}
