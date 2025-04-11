package com.daniel.GithubUserActivity.model;

public record UserActivityResponse(
        Commit commit
) {

    public record Author(
            String name,
            String date
    ) {}

    public record Commit(
            Author author,
            String message
    ) {}

}
