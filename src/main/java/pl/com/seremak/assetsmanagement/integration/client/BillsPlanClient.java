package pl.com.seremak.assetsmanagement.integration.client;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.seremak.simplebills.commons.model.Balance;
import reactor.core.publisher.Mono;

@Component
public class BillsPlanClient {

    private static final String URL_PATTERN = "%s%s";
    @Value("${custom-properties.billsPlanUrl}")
    private String billsPlanUrl;

    @Value("${custom-properties.simpleBillsUrl}")
    private String simpleBillsUrl;

    private final WebClient balanceClient = createClient(billsPlanUrl, "/blance");

    private final WebClient transactionClient = createClient(simpleBillsUrl, "/transactions");


    public Mono<Balance> getBalance(final JwtAuthenticationToken token) {
        return balanceClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token.getToken()))
                .retrieve()
                .bodyToMono(Balance.class);
    }

//    public Mono<Balance> updateBalance(final JwtAuthenticationToken token, final Balance balance) {
//        return balanceClient.
//    }

    private static WebClient createClient(final String baseUrl, final String enpoint) {
        return WebClient.create(URL_PATTERN.formatted(baseUrl, enpoint));
    }
}
