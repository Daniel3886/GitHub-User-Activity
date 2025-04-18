package com.daniel.GithubUserActivity.client;

import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("repos/{owner}/{repo}/commits?page={page}&per_page={perPage}")
public interface UserActivityClient {
    @GetExchange
    List<UserActivityResponse> getGithubUserActivity(
            @PathVariable("owner") String ownerName,
            @PathVariable("repo") String repoName,
            @PathVariable("page") int page,
            @PathVariable("perPage") int perPage
    );

}
