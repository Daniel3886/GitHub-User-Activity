package com.daniel.GithubUserActivity.client;

import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "/users", accept = "application/vnd.github+json")
public interface UserActivityClient {

    @GetExchange("/{username}/events")
    List<UserActivityResponse> getUserEvents(@PathVariable String username);
}