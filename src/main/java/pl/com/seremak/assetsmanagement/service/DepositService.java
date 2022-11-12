package pl.com.seremak.assetsmanagement.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.seremak.assetsmanagement.converter.DepositConverter;
import pl.com.seremak.assetsmanagement.dto.DepositDto;
import pl.com.seremak.assetsmanagement.model.Deposit;
import pl.com.seremak.assetsmanagement.repository.DepositRepository;
import pl.com.seremak.assetsmanagement.repository.DepositSearchRepository;
import reactor.core.publisher.Mono;

import java.util.List;

import static pl.com.seremak.assetsmanagement.converter.DepositConverter.toDeposit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositSearchRepository depositSearchRepository;


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

    public Mono<Deposit> createDeposit(final String username, final DepositDto depositDto) {
        return depositRepository.save(toDeposit(username, depositDto))
                .doOnNext(createdDeposit ->
                        log.info("Deposit with name={} and username={} created.", createdDeposit.getName(), createdDeposit.getUsername()));
    }

    public Mono<Deposit> updateDeposit(final String username, final DepositDto depositDto) {
        final Deposit deposit = DepositConverter.toDeposit(username, depositDto);
        return depositSearchRepository.updateDeposit(deposit)
                .doOnNext(updatedDeposit ->
                        log.info("Deposit with name={} and username={} updated.", updatedDeposit.getName(), updatedDeposit.getUsername()));
    }

    public Mono<Deposit> deleteDeposit(final String username, final String depositName) {
        return depositRepository.deleteByUsernameAndName(username, depositName)
                .doOnNext(deletedDeposit ->
                        log.info("Deposit with name={} and username={} deleted.", deletedDeposit.getName(), deletedDeposit.getUsername()));
    }
}
