package com.fintechledger.ledger.controller;

import com.fintechledger.ledger.entity.Account;
import com.fintechledger.ledger.entity.Transaction;
import com.fintechledger.ledger.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Create account
    @PostMapping("/create")
    public Account createAccount(@RequestParam String holderName) {
        return accountService.createAccount(holderName);
    }

    // Credit money
    @PostMapping("/{accountNumber}/credit")
    public Transaction credit(@PathVariable String accountNumber,
                              @RequestParam BigDecimal amount,
                              @RequestParam String description) {
        return accountService.credit(accountNumber, amount, description);
    }

    // Debit money
    @PostMapping("/{accountNumber}/debit")
    public Transaction debit(@PathVariable String accountNumber,
                             @RequestParam BigDecimal amount,
                             @RequestParam String description) {
        return accountService.debit(accountNumber, amount, description);
    }
}
