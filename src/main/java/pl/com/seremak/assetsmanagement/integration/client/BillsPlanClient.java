package pl.com.seremak.assetsmanagement.integration.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.seremak.simplebills.commons.dto.http.CategoryDto;
import pl.com.seremak.simplebills.commons.model.Balance;
import pl.com.seremak.simplebills.commons.model.Category;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BillsPlanClient {

    private static final String URL_PATTERN = "%s%s";
    public static final String BEARER_TOKEN_PATTERN = "Bearer %s";
    @Value("${custom-properties.billsPlanUrl}")
    private String billsPlanUrl;

    private final WebClient balanceClient = createClient(billsPlanUrl, "/blance");
    private final WebClient categoryClient = createClient(billsPlanUrl, "/categories");


    public Mono<Balance> getBalance(final Jwt token) {
        return balanceClient.get()
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PATTERN.formatted(token))
                .retrieve()
                .bodyToMono(Balance.class)
                .doOnNext(retrievedBalance -> log.info("Balance for username={} retrieved.", retrievedBalance.getUsername()));
    }

    public Mono<Category> getCategory(final Jwt token, final String categoryName) {
        return categoryClient.get()
                .uri(categoryName)
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PATTERN.formatted(token))
                .retrieve()
                .bodyToMono(Category.class)
                .doOnNext(retrievedCategory -> log.info("Category with username={} and name={} retrieved.",
                        retrievedCategory.getUsername(), retrievedCategory.getName()));
    }

    public Mono<Category> createCategory(final Jwt token, final CategoryDto categoryDto) {
        return categoryClient.post()
                .uri(categoryDto.getName())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PATTERN.formatted(token))
                .body(Mono.just(categoryDto), CategoryDto.class)
                .retrieve()
                .bodyToMono(Category.class)
                .doOnNext(createdCategory -> log.info("Category with username={} and name={} created.",
                        createdCategory.getUsername(), createdCategory.getName()));
    }

    private static WebClient createClient(final String baseUrl, final String enpoint) {
        return WebClient.create(URL_PATTERN.formatted(baseUrl, enpoint));
    }
}
