package pl.com.seremak.assetsmanagement.endpoint;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.com.seremak.assetsmanagement.dto.DepositDto;
import pl.com.seremak.assetsmanagement.model.Deposit;
import pl.com.seremak.assetsmanagement.service.DepositService;
import pl.com.seremak.assetsmanagement.utils.EndpointUtils;
import pl.com.seremak.assetsmanagement.utils.JwtExtractionHelper;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositEndpoint {

    public static final String DEPOSITS_URI_PATTERN = "/deposits/%s";

    private final DepositService depositService;
    private final JwtExtractionHelper jwtExtractionHelper;


    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> createDeposit(final JwtAuthenticationToken principal,
                                                      @Valid final DepositDto depositDto) {
        final String username = jwtExtractionHelper.extractUsername(principal);
        log.info("Deposit creation request received for username={} and depositName={}", username, depositDto.getName());
        return depositService.createDeposit(depositDto)
                .doOnSuccess(createdDeposit -> log.info("Deposit with name={} and username={} created.", createdDeposit.getName(), createdDeposit.getUsername()))
                .map(deposit -> EndpointUtils.prepareCreatedResponse(DEPOSITS_URI_PATTERN, deposit.getName()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Deposit>>> findDeposits(final JwtAuthenticationToken principal) {
        final String username = jwtExtractionHelper.extractUsername(principal);
        return depositService.findAllDeposits(username)
                .doOnSuccess(deposits -> log.info("({} Deposit for username={} found.", deposits.size(), username))
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "{depositName}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Deposit>> findDeposit(final JwtAuthenticationToken principal, @PathVariable final String depositName) {
        final String username = jwtExtractionHelper.extractUsername(principal);
        return depositService.findDepositByName(username, depositName)
                .doOnSuccess(deposit -> log.info("Deposit with name={} and username={} found.", deposit.getName(), deposit.getUsername()))
                .map(ResponseEntity::ok);
    }
}
