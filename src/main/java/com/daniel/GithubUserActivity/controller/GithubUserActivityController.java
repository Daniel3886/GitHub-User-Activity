package com.daniel.GithubUserActivity.controller;

import com.daniel.GithubUserActivity.client.UserActivityClient;
import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class GithubUserActivityController {
    private final UserActivityClient userActivityClient;


    public GithubUserActivityController(UserActivityClient userActivityClient) {
        this.userActivityClient = userActivityClient;
    }
    @GetMapping("/name")
    public List<UserActivityResponse> getUserName(
            @RequestParam String username
    ){
        return userActivityClient.getUserEvents(username);
    }
}
