package com.daniel.GithubUserActivity.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class Command {

    @ShellMethod(key = "github-activity", value = "Execute this command to track GitHub activity")
    public String executeCommand(@ShellOption(defaultValue = "<username>") String username) {
        if(username.equals("<username>")) {
            return "github-activity " + username + "!";
        }

        return "Tracking activity of a user named: " + username;
    }
}
