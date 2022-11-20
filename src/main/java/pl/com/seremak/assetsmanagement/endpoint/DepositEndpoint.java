package pl.com.seremak.assetsmanagement.endpoint;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.com.seremak.assetsmanagement.service.DepositService;
import pl.com.seremak.simplebills.commons.dto.http.DepositDto;
import pl.com.seremak.simplebills.commons.model.Deposit;
import pl.com.seremak.simplebills.commons.utils.JwtExtractionHelper;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.com.seremak.simplebills.commons.utils.EndpointUtils.decodeUriParam;
import static pl.com.seremak.simplebills.commons.utils.EndpointUtils.prepareCreatedResponse;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositEndpoint {

    public static final String DEPOSITS_URI_PATTERN = "/deposits/%s";

    private final DepositService depositService;

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Deposit>> createDeposit(final JwtAuthenticationToken principal,
                                                       @Valid @RequestBody final DepositDto depositDto) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        log.info("Deposit creation request received for username={} and depositName={}", username, depositDto.getName());
        return depositService.createDeposit(principal, depositDto)
                .doOnSuccess(createdDeposit -> log.info("Deposit with name={} and username={} created.", createdDeposit.getName(), createdDeposit.getUsername()))
                .map(deposit -> prepareCreatedResponse(DEPOSITS_URI_PATTERN, deposit.getName(), deposit));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Deposit>>> findDeposits(final JwtAuthenticationToken principal) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        return depositService.findAllDeposits(username)
                .doOnSuccess(deposits -> log.info("{} Deposit for username={} found.", deposits.size(), username))
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "{depositName}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Deposit>> findDeposit(final JwtAuthenticationToken principal,
                                                     @PathVariable final String depositName) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        return depositService.findDepositByName(username, decodeUriParam(depositName))
                .doOnSuccess(deposit -> log.info("Deposit with name={} and username={} found.", deposit.getName(), deposit.getUsername()))
                .map(ResponseEntity::ok);
    }

    @PatchMapping(value = "{depositName}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Deposit>> updateDeposit(final JwtAuthenticationToken principal,
                                                       @Valid @RequestBody final DepositDto depositDto,
                                                       @PathVariable final String depositName) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        log.info("Deposit update request received for username={} and depositName={}", username, depositDto.getName());
        return depositService.updateDeposit(principal, decodeUriParam(depositName), depositDto)
                .doOnSuccess(updatedDeposit ->
                        log.info("Deposit with name={} and username={} updated.", updatedDeposit.getName(), updatedDeposit.getUsername()))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping(value = "{depositName}")
    public Mono<ResponseEntity<Void>> deleteDeposit(final JwtAuthenticationToken principal,
                                                    @PathVariable final String depositName) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        log.info("Deposit deletion request received for username={} and depositName={}", username, depositName);
        return depositService.deleteDeposit(principal, decodeUriParam(depositName))
                .doOnSuccess(updatedDeposit ->
                        log.info("Deposit with name={} and username={} updated.", updatedDeposit.getName(), updatedDeposit.getUsername()))
                .map(__ -> ResponseEntity.noContent().build());
    }
}
