package br.com.ecromaneli.githubscraper;

import br.com.ecromaneli.githubscraper.utils.HttpRequest.HttpRequest;
import br.com.ecromaneli.githubscraper.utils.XMLQuery.MLQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GithubScraperApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testGETRequest() {
		HttpRequest req = new HttpRequest();
		req.get("https://postman-echo.com/get");
	}

	@Test
	void testMLQuerySelector() throws Exception {
		MLQuery mlQuery = MLQuery.from("<xml><tag class='class' id='id' attr='value'>abcd</tag></xml>");
		throw new Exception("teste");
	}

}
