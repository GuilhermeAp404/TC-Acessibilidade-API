package tc.accessibility.market;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@RestController

	public class ApiHome{
		private static final String GITHUB_REPO_URL="https://github.com/GuilhermeAp404/TC-Acessibilidade-API";

		@RequestMapping("/**")
		public void method(HttpServletResponse httpServletResponse) {
			httpServletResponse.setHeader("Location", GITHUB_REPO_URL);
			httpServletResponse.setStatus(302);
		}
	}
}
