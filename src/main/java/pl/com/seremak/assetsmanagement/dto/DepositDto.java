package pl.com.seremak.assetsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import pl.com.seremak.assetsmanagement.model.Deposit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDto {

    @NotNull(message = "Deposit name cannot be null")
    private String name;

    @Positive(message = "Deposit value cannot be must be a positive number")
    private BigDecimal value;

    @NotNull(message = "Deposit name cannot be null")
    private Deposit.DepositType depositType;

    @Nullable
    private String bankName;

    @Nullable
    private Integer durationInMonths;

    private BigDecimal annualInterestRate = BigDecimal.ZERO;
}
