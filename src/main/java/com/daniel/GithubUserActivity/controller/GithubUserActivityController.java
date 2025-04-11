package com.daniel.GithubUserActivity.controller;

import com.daniel.GithubUserActivity.client.UserActivityClient;
import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commits")
public class GithubUserActivityController {
    private final UserActivityClient userActivityClient;


    public GithubUserActivityController(UserActivityClient userActivityClient) {
        this.userActivityClient = userActivityClient;
    }

    @GetMapping("/activity")
    public List<UserActivityResponse> getCommits(
            @RequestParam String ownerName, @RequestParam String repoName) {
        return userActivityClient.getGithubUserActivity(ownerName, repoName);
    }

}
