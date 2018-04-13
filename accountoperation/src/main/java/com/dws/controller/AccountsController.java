package com.dws.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.entity.Account;
import com.dws.entity.AccountTransaction;
import com.dws.exception.DuplicateAccountIdException;
import com.dws.exception.InsufficientAmountException;
import com.dws.exception.TransactionException;
import com.dws.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountsController.class);
	private final AccountsService accountsService;

	@Autowired
	public AccountsController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	/**
	 * http://localhost:18080/dws/bank/v1/accounts
	 * 
	 * @param account
	 * @return
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		LOGGER.info("Creating account {}", account);

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	/**
	 * http://localhost:18080/dws/bank/v1/accounts/{accountId}
	 * @param accountId
	 * @return
	 */
	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		LOGGER.info("Retrieving account for id {}", accountId);
		return this.accountsService.getAccount(accountId);
	}
	/**
	 * http://localhost:18080/dws/bank/v1/accounts/transaction
	 * @param accountTransaction
	 * @return
	 */
	@PostMapping(path = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object>  transaction(@RequestBody @Valid AccountTransaction accountTransaction) {
		LOGGER.info("Transaction start {}", accountTransaction);
		
		try {
			accountsService.transaction(accountTransaction);
		}catch (InsufficientAmountException re) {
			return new ResponseEntity<>(re.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (TransactionException te) {
			return new ResponseEntity<>(te.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
