package pl.com.seremak.simplebills.assetmanagement.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String URL_PATTERN = "%s%s";
    @Value("${custom-properties.planning-url}")
    private String planningUrl;

    @Value("${custom-properties.transaction-url}")
    private String transactionUrl;


    @Bean
    public WebClient balanceClient() {
        return createClient(planningUrl, "/balance");
    }

    @Bean
    public WebClient categoryClient() {
        return createClient(planningUrl, "/categories");
    }

    @Bean
    public WebClient transactionClient() {
        return createClient(transactionUrl, "/transactions");
    }

    private static WebClient createClient(final String baseUrl, final String endpoint) {
        return WebClient.create(URL_PATTERN.formatted(baseUrl, endpoint));
    }
}
