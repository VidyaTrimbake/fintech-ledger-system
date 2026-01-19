package com.fintechledger.ledger.service;

import com.fintechledger.ledger.entity.Account;
import com.fintechledger.ledger.entity.Transaction;
import com.fintechledger.ledger.repository.AccountRepository;
import com.fintechledger.ledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    //Create Account
    public Account createAccount(String holderName) {
        Account account = Account.builder()
                .accountNumber("ACC" + UUID.randomUUID().toString().substring(0, 6))
                .holderName(holderName)
                .balance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

        return accountRepository.save(account);
    }

    //Credit Money
    public Transaction credit(String accountNumber, BigDecimal amount, String description) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction txn = Transaction.builder()
                .transactionRef("TXN" + UUID.randomUUID().toString().substring(0, 8))
                .accountNumber(accountNumber)
                .type("CREDIT")
                .amount(amount)
                .description(description)
                .build();

        return transactionRepository.save(txn);
    }

    //Debit Money
    public Transaction debit(String accountNumber, BigDecimal amount, String description) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction txn = Transaction.builder()
                .transactionRef("TXN" + UUID.randomUUID().toString().substring(0, 8))
                .accountNumber(accountNumber)
                .type("DEBIT")
                .amount(amount)
                .description(description)
                .build();

        return transactionRepository.save(txn);
    }
}
