package com.daniel.GithubUserActivity.model;

import java.util.List;

public record UserActivityResponse(
        String type,
        Repo repo,
        Payload payload,
        String created_at
) {

    public record Repo(
            String name
    ) {}

    public record Payload(
            List<Commit> commits,
            Issue issue,
            Fork fork
    ) {}

    public record Fork(
            String name
    ) {}

    public record Commit(
            String message,
            Author author
    ) {}

    public record Author(
            String name
    ) {}

    public record Issue(
            String title
    ) {}

}