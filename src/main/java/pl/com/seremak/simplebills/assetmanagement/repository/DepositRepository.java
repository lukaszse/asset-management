package pl.com.seremak.simplebills.assetmanagement.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.seremak.simplebills.commons.model.Deposit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DepositRepository extends ReactiveCrudRepository<Deposit, String> {

    Flux<Deposit> findAllByUsername(final String username);
    Mono<Deposit> findByUsernameAndName(final String username, final String name);
    Mono<Deposit> deleteByUsernameAndName(final String username, final String name);

    Mono<Deposit> deleteByUsernameAndTransactionNumber(final String username, final Integer transactionNumber);
}
