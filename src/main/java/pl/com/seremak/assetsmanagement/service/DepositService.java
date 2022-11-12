package pl.com.seremak.assetsmanagement.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.seremak.assetsmanagement.dto.DepositDto;
import pl.com.seremak.assetsmanagement.model.Deposit;
import pl.com.seremak.assetsmanagement.repository.DepositRepository;
import reactor.core.publisher.Mono;

import java.util.List;

import static pl.com.seremak.assetsmanagement.converter.DepositConverter.toDeposit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;


    public Mono<List<Deposit>> findAllDeposits(final String username) {
        return depositRepository.findAllByUsername(username)
                .collectList()
                .doOnNext(deposits -> log.info("{} deposits for userName={} found.", deposits.size(), username));
    }

    public Mono<Deposit> findDepositByName(final String username, final String depositName) {
        return depositRepository.findByUsernameAndName(username, depositName)
                .doOnNext(deposit -> log.info("Deposit with name={} and username={}  found", deposit.getUsername(), deposit.getName()));
    }

    public Mono<Deposit> createDeposit(final DepositDto depositDto) {
        return depositRepository.save(toDeposit(depositDto))
                .doOnNext(createdDeposit -> log.info("Deposit with name={} and username={} created.", createdDeposit.getName(), createdDeposit.getUsername()));
    }
}
