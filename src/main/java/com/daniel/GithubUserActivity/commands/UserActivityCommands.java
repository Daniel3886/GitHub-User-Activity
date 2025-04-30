package com.daniel.GithubUserActivity.commands;

import com.daniel.GithubUserActivity.client.UserActivityClient;
import com.daniel.GithubUserActivity.model.UserActivityResponse;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
public class UserActivityCommands {

    private final UserActivityClient userActivityClient;
    private String currentUsername;

    public UserActivityCommands(UserActivityClient userActivityClient) {
        this.userActivityClient = userActivityClient;
    }

    @ShellMethod(key = "github-activity", value = "Track GitHub repository activity")
    public String UserActivity(
            @ShellOption(help = "Github username") String username
    ) {

        if ("<username>".equals(username)) {
            return "Please provide a valid GitHub username.";
        }

        List<UserActivityResponse> events;
        try {
            events = userActivityClient.getUserEvents(username).stream().distinct().toList();
        } catch (Exception e) {
            return "Error fetching activity for user: " + username + ". Please check the username and network connection.";
        }

        this.currentUsername = username;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;

        if (events.isEmpty()) {
            return "No activity found for user: " + username;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("🔍 Tracking activity for: ").append(username).append("\n\n");

        for (UserActivityResponse event : events) {
            String type = event.type();
            String repoName = event.repo().name();

            LocalDateTime time = LocalDateTime.parse(event.created_at(), isoFormatter);
            String formattedTime = time.format(formatter);

            sb.append("[").append(formattedTime).append("] ");

            switch (type) {
                case "PushEvent":
                    if (event.payload().commits() != null) {
                        int commitCount = event.payload().commits().size();
                        String commitWord = commitCount == 1 ? "commit" : "commits";
                        sb.append(String.format("📤 Pushed %d %s to %s\n", commitCount, commitWord, repoName));
                    }
                    break;
                case "IssuesEvent":
                    if (event.payload().issue() != null) {
                        sb.append("❗Opened a new issue in: ").append(repoName)
                                .append(", Title: ").append(event.payload().issue().title()).append("\n");
                    }
                    break;
                case "WatchEvent":
                    sb.append("✨ Starred repository: ").append(repoName).append("\n");
                    break;
                case "ForkEvent":
                    sb.append("🍴 Forked repository: ").append(repoName).append("\n");
                    break;
                case "CreateEvent":
                    sb.append("📦 Created repository: ").append(repoName).append("\n");
                    break;
                case "DeleteEvent":
                    sb.append("🗑️ Deleted repository: ").append(repoName).append("\n");
                    break;
                case "PublicEvent":
                    sb.append("🌐 Made repository public: ").append(repoName).append("\n");
                    break;
                default:
                    sb.append("🔄 ").append(type).append(" on ").append(repoName).append("\n");
                    break;
            }
        }

        sb.append("\n🔗 GitHub Activity Links Navigation for user: ").append(this.currentUsername).append("\n");
        sb.append("To see activity links, use one of these commands:\n");
        sb.append("'github-link --repo <repository-name> -type commits' - for commit links\n");
        sb.append("'github-link --repo <repository-name> -type issues' - for issue links\n");
        sb.append("'github-link --repo <repository-name> --type stars' - for star links\n");
        sb.append("'github-link --repo <repository-name> --type forks' - for fork links\n");
        sb.append("'github-link --repo <repository-name> --type pulls' - for pull request links\n");
        sb.append("'github-link --repo <repository-name> --type repo' - for repository links\n");

        return sb.toString();
    }

    @ShellMethod(key = "github-link", value = "Get GitHub activity specific links")
    public String getActivityLink(
            @ShellOption(value = {"-r", "--repo"}, help = "Repository name (without username, use github-activity!)") String repoName,
            @ShellOption(value = {"-t", "--type"}, help = "Activity type") String type
    ) {
        if (this.currentUsername == null) {
            return "Please run github-activity command first to set a username";
        }

        if (repoName == null || repoName.isEmpty()) {
            return "Please provide a repository name using --repo option";
        }

        if (type == null || type.isEmpty()) {
            return "Please provide an activity type using --type option.\n" +
                    "Available types: commits, issues, stars, forks, pulls, repo";
        }

        StringBuilder link = new StringBuilder("https://github.com/")
                .append(this.currentUsername)
                .append("/")
                .append(repoName);

        String activityType = type.toLowerCase();

        switch (activityType) {
            case "commits":
                link.append("/commits");
                break;
            case "issues":
                link.append("/issues");
                break;
            case "stars":
                link.append("/stargazers");
                break;
            case "forks":
                link.append("/network/members");
                break;
            case "pulls":
                link.append("/pulls");
                break;
            case "repo":
                link = new StringBuilder("https://github.com/")
                        .append(this.currentUsername)
                        .append("/")
                        .append(repoName);
                break;
            default:
                return "Invalid activity type. Available types: commits, issues, stars, forks, pulls, repo";
        }

        try {
            List<UserActivityResponse> events = userActivityClient.getUserEvents(this.currentUsername);

            boolean hasActivity = events.stream()
                    .anyMatch(event -> {
                        String fullRepoName = event.repo().name();
                        String repoNameOnly = fullRepoName.contains("/") ?
                                fullRepoName.substring(fullRepoName.indexOf('/') + 1) : fullRepoName;
                        return repoNameOnly.equalsIgnoreCase(repoName) &&
                                isMatchingActivityType(event.type(), activityType);
                    });

            if (!hasActivity) {
                return "No " + activityType + " activity found for repository: " + repoName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "🔗 Link for " + this.currentUsername + "/" + repoName + ": " + link;
    }

    private boolean isMatchingActivityType(String eventType, String requestedType) {
        return switch (requestedType) {
            case "commits" -> eventType.equals("PushEvent");
            case "issues" -> eventType.equals("IssuesEvent");
            case "stars" -> eventType.equals("WatchEvent");
            case "forks" -> eventType.equals("ForkEvent");
            case "pulls" -> eventType.equals("PullRequestEvent");
            case "repo" -> true;
            default -> false;
        };
    }
}