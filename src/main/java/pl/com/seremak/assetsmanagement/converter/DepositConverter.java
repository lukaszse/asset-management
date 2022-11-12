package pl.com.seremak.assetsmanagement.converter;

import pl.com.seremak.assetsmanagement.dto.DepositDto;
import pl.com.seremak.assetsmanagement.model.Deposit;

public class DepositConverter {

    public static Deposit toDeposit(final DepositDto depositDto) {
        final Deposit deposit = new Deposit();
        deposit.setUsername(deposit.getUsername());
        deposit.setName(deposit.getName());
        deposit.setValue(depositDto.getValue());
        deposit.setDepositType(depositDto.getDepositType());
        deposit.setBankName(depositDto.getBankName());
        deposit.setAnnualInterestRate(depositDto.getAnnualInterestRate());
        return deposit;
    }
}
