## 📖 Project Overview

**GithubUserActivity** is a command-line tool that fetches and displays a GitHub user’s recent public events—commits, issues, PRs, etc.—using the GitHub REST API. It also supports linking directly into specific repositories and event types for deeper inspection.

---

## 🚀 Features

- **`github-activity <username>`**  
  Quickly list a user’s latest public GitHub events (pushes, PRs, issues, comments).

- **`github-link`**  
  Generate direct GitHub URLs for a specific repo and event type (e.g., commits, issues).  
  - Short form: `github-link -r <repo> -t <type>`  
  - Long form: `github-link --repo <repo> --type <type>`

---

## 💾 Installation

1. **Clone the repo**  
   ```bash
   git clone https://github.com/your-username/GithubUserActivity.git
   cd GithubUserActivity
   mvn clean install
   mvn spring-boot:run
   ```

---

## 🎯 Usage Examples

### 1. List user activity
```bash
github-activity Daniel3886
```
```
🔍 Tracking activity for: Daniel3886
[2025-04-12 20:59:16] 📤 Pushed 1 commit to Daniel3886/GitHub-User-Activity
[2025-04-11 18:04:10] 📤 Pushed 1 commit to Daniel3886/GitHub-User-Activity
[2025-04-09 18:24:23] 📦 Created repository: Daniel3886/GitHub-User-Activity
```

### 2. Generate GitHub links

**Long form:**
```bash
github-link --repo GitHub-User-Activity --type commits
```
```
🔗 Link for Daniel3886/Github-User-Activity: https://github.com/Daniel3886/GitHub-User-Activity/commits
```

**Short form:**
```bash
github-link -r GitHub-User-Activity -t repo
```
```
🔗 Link for Daniel3886/Github-User-Activity: https://github.com/Daniel3886/GitHub-User-Activity
```

---

## 🚨 Error Handling

- **If the GitHub username is invalid or the API fails:**
  ```
  Error fetching activity for user: " + username + ". Please check the username and network connection.
  ```

- **If no username is provided:**
  ```
  github-activity -> Missing mandatory option --username, Github username.
  github-link -> Please run github-activity command first to set a username
  ```

- **If you type an activity link that isn't listed or is mistyped:**
  ```
  Invalid activity type. Available types: commits, issues, stars, forks, pulls, repo
  ```

---

## 🛠️ Technologies and Dependencies

| Category           | Library / Framework              | Version         |
|--------------------|----------------------------------|-----------------|
| Core               | Java                             | 17              |
| Web Client         | Spring Boot WebFlux              | 3.0.3           |
| CLI                | Spring Shell                     | 3.0.0           |
| Build & Packaging  | Maven                            | 4.0.0 / Native  |
| Testing            | Spring Boot Test, Reactor Test   | —               |

---
