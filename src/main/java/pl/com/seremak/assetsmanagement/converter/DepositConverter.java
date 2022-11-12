package pl.com.seremak.assetsmanagement.converter;

import pl.com.seremak.assetsmanagement.dto.DepositDto;
import pl.com.seremak.assetsmanagement.model.Deposit;

public class DepositConverter {

    public static Deposit toDeposit(final String username, final DepositDto depositDto) {
        final Deposit deposit = new Deposit();
        deposit.setUsername(username);
        deposit.setName(depositDto.getName());
        deposit.setValue(depositDto.getValue());
        deposit.setDepositType(depositDto.getDepositType());
        deposit.setBankName(depositDto.getBankName());
        deposit.setAnnualInterestRate(depositDto.getAnnualInterestRate());
        return deposit;
    }

    public static DepositDto toDepositDto(final Deposit deposit) {
        final DepositDto depositDto = new DepositDto();
        depositDto.setName(deposit.getName());
        depositDto.setValue(deposit.getValue());
        depositDto.setDepositType(deposit.getDepositType());
        depositDto.setBankName(deposit.getBankName());
        depositDto.setAnnualInterestRate(deposit.getAnnualInterestRate());
        return depositDto;
    }
}
