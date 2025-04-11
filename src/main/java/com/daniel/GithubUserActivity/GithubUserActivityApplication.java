package com.daniel.GithubUserActivity;

import com.daniel.GithubUserActivity.client.UserActivityClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@SpringBootApplication
public class GithubUserActivityApplication {
	public static void main(String[] args) {
		SpringApplication.run(GithubUserActivityApplication.class, args);
	}

	@Bean
	public UserActivityClient userActivityClient() {
		WebClient webClient = WebClient.builder()
				.baseUrl("https://api.github.com")
				.defaultHeader("Accept", "application/vnd.github.v3+json")
				.clientConnector(new ReactorClientHttpConnector(
						HttpClient.create()
								.responseTimeout(Duration.ofSeconds(10))
				))
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
				.build();

		return factory.createClient(UserActivityClient.class);
	}
}

