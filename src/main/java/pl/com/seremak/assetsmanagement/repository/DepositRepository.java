package pl.com.seremak.assetsmanagement.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.seremak.assetsmanagement.model.Deposit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DepositRepository extends ReactiveCrudRepository<Deposit, String> {

    Flux<Deposit> findAllByUserName(final String username);
    Mono<Deposit> findByUserNameAndAndName(final String username, final String depositName);
}
