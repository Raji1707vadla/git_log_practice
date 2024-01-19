package com.example.Phone.Pay.management.service;

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
    private static final String REPO = "https://github.com/Raji1707vadla/git_log_practice.git";
    private static final String TOKEN = "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";
@Override
    public  String createPullRequest(String base, String head, String title, String body) {
        String apiUrl = String.format("%s/repos/%s/%s/pulls", GITHUB_API_URL, OWNER, REPO);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        RestTemplate restTemplate = new RestTemplate();
        PullRequest pullRequest = new PullRequest(base, head, title, body);
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
