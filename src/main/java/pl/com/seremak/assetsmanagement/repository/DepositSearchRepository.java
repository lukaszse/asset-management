package pl.com.seremak.assetsmanagement.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pl.com.seremak.simplebills.commons.model.Deposit;
import pl.com.seremak.simplebills.commons.utils.MongoQueryHelper;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class DepositSearchRepository {

    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Deposit> updateDeposit(final Deposit deposit) {
        return reactiveMongoTemplate.findAndModify(
                prepareFindDepositQuery(deposit.getUsername(), deposit.getName()),
                MongoQueryHelper.preparePartialUpdateQuery(deposit, Deposit.class),
                new FindAndModifyOptions().returnNew(true),
                Deposit.class
        );
    }

    private static Query prepareFindDepositQuery(final String username, final String depositName) {
        return new Query()
                .addCriteria(Criteria.where("username").is(username))
                .addCriteria(Criteria.where("name").is(depositName));
    }
}
