package br.com.ecromaneli.githubscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class GithubScraperApplication {

	private void teste() {

	}

	public void mano() {}

	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");
//		System.getProperties().forEach((key, value) -> {
//			System.out.println(key + "=" + value);
//		});
		SpringApplication.run(GithubScraperApplication.class, args);
	}

}
