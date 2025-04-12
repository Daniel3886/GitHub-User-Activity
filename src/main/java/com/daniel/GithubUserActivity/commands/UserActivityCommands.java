package com.daniel.GithubUserActivity.commands;

import com.daniel.GithubUserActivity.client.UserActivityClient;
import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class UserActivityCommands {

    private final UserActivityClient userActivityClient;

    public UserActivityCommands(UserActivityClient userActivityClient) {
        this.userActivityClient = userActivityClient;
    }

    @ShellMethod(key = "github-activity", value = "Track GitHub repository activity")
    public String getUserActivity(
            String ownerName,
            String repoName,
            @ShellOption(defaultValue = "1", help = "Page number to display") int page,
            @ShellOption(defaultValue = "30", help = "Number of commits per page (max 30)") int perPage) {

        if ("<username>".equals(ownerName)) {
            return "Please provide a valid  GitHub username.";
        }
        if ("<repo>".equals(repoName)) {
            return "Please provide a valid GitHub repository.";
        }

        List<UserActivityResponse> allCommits = userActivityClient.getGithubUserActivity(
                ownerName,
                repoName,
                page,
                Math.min(perPage, 100)
        );

        int totalCommits = allCommits.size();
        if (totalCommits == 0) {
            return String.format("No commits found for %s/%s", ownerName, repoName);
        }

        int totalPages = (int) Math.ceil((double) totalCommits / perPage);
        page = Math.max(1, Math.min(page, totalPages));

        int fromIndex = (page - 1) * perPage;
        int toIndex = Math.min(fromIndex + perPage, totalCommits);
        List<UserActivityResponse> pageCommits = allCommits.subList(fromIndex, toIndex);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Tracking activity for %s/%s (Page %d, %d total commits): " + perPage + "\n\n",
                ownerName, repoName, page, totalPages, totalCommits));

        pageCommits.forEach(response -> {
            var commit = response.commit();
            var author = commit.author();
            sb.append("🔹 Author: ").append(author.name()).append("\n")
                    .append("🕒 Date: ").append(author.date()).append("\n")
                    .append("💬 Message: ").append(commit.message()).append("\n")
                    .append("--------------------------\n");
        });

        sb.append("\nNavigation options:");
        sb.append("\nUse 'github-activity ").append(ownerName).append(" ")
                .append(repoName).append(" --page ").append(page + 1).append("' for next page");

        if (page > 1) {
            sb.append("\nUse 'github-activity ").append(ownerName).append(" ")
                    .append(repoName).append(" --page ").append(page - 1).append("' for previous page");
        }

            sb.append("\nUse 'github-activity ").append(ownerName).append(" ")
                    .append(repoName).append(" --page ").append(page).append(" --perPage {").append(perPage).append("}' to change the amount of commits per page (current: ")
                    .append(perPage).append(")");

        return sb.toString();
    }
}
