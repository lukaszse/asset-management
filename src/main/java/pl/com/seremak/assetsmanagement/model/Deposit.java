package pl.com.seremak.assetsmanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Getter
@Setter
public class Deposit extends Asset {

    public enum DepositType {
        PERPETUAL, TERM
    }

    private DepositType depositType;

    private String bankName;

    private Integer durationInMonths;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal annualInterestRate;
}
