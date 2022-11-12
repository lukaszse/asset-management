package pl.com.seremak.assetsmanagement.integration.client;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.seremak.assetsmanagement.model.Balance;
import reactor.core.publisher.Mono;

@Component
public class BillsPlanClient {

    @Value("${custom-properties.billsPlanUrl}")
    private String billsPlanUrl;

    private final WebClient balanceClient = WebClient.create(billsPlanUrl);


    public Mono<Balance> getBalance(final JwtAuthenticationToken token) {
        return balanceClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token.getToken()))
                .retrieve()
                .bodyToMono(Balance.class);
    }

    public Mono<Balance> updateBalance(final JwtAuthenticationToken token, final Balance balance) {
        return balanceClient.patch()
                .uri(balance.getUsername())
                .body(balance, Balance.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token.getToken()))
                .retrieve()
                .bodyToMono(Balance.class);
    }
}
