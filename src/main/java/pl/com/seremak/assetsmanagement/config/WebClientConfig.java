package pl.com.seremak.assetsmanagement.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String URL_PATTERN = "%s%s";
    @Value("${custom-properties.billsPlanUrl}")
    private String billsPlanUrl;

    @Value("${custom-properties.simpleBillsUrl}")
    private String simpleBillsUrl;


    @Bean
    public WebClient balanceClient() {
        return createClient(billsPlanUrl, "/balance");
    }

    @Bean
    public WebClient categoryClient() {
        return createClient(billsPlanUrl, "/categories");
    }

    @Bean
    public WebClient transactionClient() {
        return createClient(simpleBillsUrl, "/transactions");
    }

    private static WebClient createClient(final String baseUrl, final String endpoint) {
        return WebClient.create(URL_PATTERN.formatted(baseUrl, endpoint));
    }
}
