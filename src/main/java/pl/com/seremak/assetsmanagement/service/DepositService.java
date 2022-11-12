package pl.com.seremak.assetsmanagement.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.seremak.assetsmanagement.model.Deposit;
import pl.com.seremak.assetsmanagement.repository.DepositRepository;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;


    public Mono<List<Deposit>> findAllDeposits(final String username) {
        return depositRepository.findAllByUserName(username)
                .collectList()
                .doOnSuccess(deposits -> log.info("{} deposits for userName={} found.", deposits.size(), username));
    }
}
