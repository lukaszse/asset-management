package pl.com.seremak.assetsmanagement.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.com.seremak.assetsmanagement.integration.client.BillsPlanClient;
import pl.com.seremak.assetsmanagement.messageQueue.MessagePublisher;
import pl.com.seremak.assetsmanagement.repository.DepositRepository;
import pl.com.seremak.assetsmanagement.repository.DepositSearchRepository;
import pl.com.seremak.simplebills.commons.dto.http.CategoryDto;
import pl.com.seremak.simplebills.commons.dto.http.DepositDto;
import pl.com.seremak.simplebills.commons.dto.http.TransactionDto;
import pl.com.seremak.simplebills.commons.model.Balance;
import pl.com.seremak.simplebills.commons.model.Deposit;
import pl.com.seremak.simplebills.commons.utils.JwtExtractionHelper;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static pl.com.seremak.simplebills.commons.constants.StandardCategories.ASSETS;
import static pl.com.seremak.simplebills.commons.converter.DepositConverter.toDeposit;
import static pl.com.seremak.simplebills.commons.model.Transaction.Type.EXPENSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositSearchRepository depositSearchRepository;
    private final BillsPlanClient billsPlanClient;
    private final MessagePublisher messagePublisher;


    public Mono<List<Deposit>> findAllDeposits(final String username) {
        return depositRepository.findAllByUsername(username)
                .collectList()
                .doOnNext(deposits -> log.info("{} deposits for userName={} found.", deposits.size(), username));
    }

    public Mono<Deposit> findDepositByName(final String username, final String depositName) {
        return depositRepository.findByUsernameAndName(username, depositName)
                .doOnNext(deposit ->
                        log.info("Deposit with name={} and username={}  found", deposit.getUsername(), deposit.getName()));
    }

    public Mono<Deposit> createDeposit(final JwtAuthenticationToken principal, final DepositDto depositDto) {
        final String username = JwtExtractionHelper.extractUsername(principal);
        final Jwt token = principal.getToken();
        return billsPlanClient.getBalance(token)
                .filter(balance -> validateBalance(balance, depositDto.getValue()))
                .then(billsPlanClient.getCategory(token, ASSETS.toString())
                        .switchIfEmpty(billsPlanClient.createCategory(token, prepareAssetsCategory(depositDto))))
                .then(depositRepository.save(toDeposit(username, depositDto)))
                .doOnNext(deposit -> messagePublisher.sentTransactionCreationRequest(toTransactionDto(deposit)))
                .doOnNext(createdDeposit ->
                        log.info("Deposit with name={} and username={} created.", createdDeposit.getName(), createdDeposit.getUsername()));
    }

    public Mono<Deposit> updateDeposit(final String username, final String depositName, final DepositDto depositDto) {
        final Deposit deposit = toDeposit(username, depositName, depositDto);
        return depositSearchRepository.updateDeposit(deposit)
                .doOnNext(updatedDeposit ->
                        log.info("Deposit with name={} and username={} updated.", updatedDeposit.getName(), updatedDeposit.getUsername()));
    }

    public Mono<Deposit> deleteDeposit(final String username, final String depositName) {
        return depositRepository.deleteByUsernameAndName(username, depositName)
                .doOnNext(deletedDeposit ->
                        log.info("Deposit with name={} and username={} deleted.", deletedDeposit.getName(), deletedDeposit.getUsername()));
    }

    private static boolean validateBalance(final Balance balance, final BigDecimal expense) {
        if (balance.getBalance().compareTo(expense) > 0) {
            log.info("User have enough money to perform transaction");
            return true;
        } else {
            log.info("User don't enough money to perform transaction");
            return false;
        }
    }

    private static TransactionDto toTransactionDto(final Deposit deposit) {
        return TransactionDto.builder()
                .user(deposit.getUsername())
                .category(ASSETS.toString())
                .type(EXPENSE.toString())
                .amount(deposit.getValue())
                .date(LocalDate.now())
                .description(prepareDescription(deposit))
                .build();
    }

    private static String prepareDescription(final Deposit deposit) {
        final String description = StringUtils.isBlank(deposit.getBankName()) ?
                "%s deposit".formatted(deposit.getName()) :
                "%s deposit in %s".formatted(deposit.getName(), deposit.getBankName());
        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    private static CategoryDto prepareAssetsCategory(final DepositDto depositDto) {
        return CategoryDto.builder()
                .name(depositDto.getName())
                .transactionType(ASSETS.name())
                .build();
    }
}
