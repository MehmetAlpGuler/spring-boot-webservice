package se.maginteractive.test.module.transaction.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.exception.InsufficientBalanceException;
import se.maginteractive.test.exception.SmallAmountException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.TransactionProcessorDto;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.maginteractive.test.enums.TransactionType.WITHDRAW;

@DisplayName("Deposit Processor Test")
@ExtendWith(MockitoExtension.class)
class WithdrawProcessorTest {

    @InjectMocks
    private WithdrawProcessor service;

    private TransactionProcessorDto transactionProcessorDto;
    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .amount(BigDecimal.valueOf(300))
                .build();
    }

    @Test
    void apply() {
        //given

        //when
        Transaction savedTransaction = service.apply(transactionProcessorDto);

        //then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(300));
        assertThat(savedTransaction.getAccount().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(700));
    }

    @DisplayName("Withdraw Small Amount Exception")
    @Test
    void withdraw_small_amount_exception() {
        //given
        transactionProcessorDto.setAmount(ZERO);

        //when
        Exception exception = assertThrows(
                SmallAmountException.class,
                () -> service.apply(transactionProcessorDto));

        //then
        assertEquals("Amount can not be zero or less!", exception.getMessage());
    }

    @DisplayName("Withdraw Insufficient Balance Exception")
    @Test
    void withdraw_insufficient_balance_exception() {
        //given
        account.setBalance(BigDecimal.valueOf(200));

        //when //then
        assertThrows(
                InsufficientBalanceException.class,
                () -> service.apply(transactionProcessorDto));
    }

    @Test
    void getType() {
        assertEquals(WITHDRAW, service.getType());
    }
}