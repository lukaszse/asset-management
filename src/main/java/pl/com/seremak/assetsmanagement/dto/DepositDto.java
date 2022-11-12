package pl.com.seremak.assetsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.seremak.assetsmanagement.model.Deposit;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDto {

    private String username;
    private String name;
    private BigDecimal value;
    private Deposit.DepositType depositType;
    private String bankName;
    private Integer durationInMonths;
    private BigDecimal annualInterestRate;
}
