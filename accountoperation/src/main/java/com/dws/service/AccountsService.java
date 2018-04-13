package com.dws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.entity.Account;
import com.dws.entity.AccountTransaction;
import com.dws.exception.TransactionException;
import com.dws.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}
	public boolean transaction(AccountTransaction accountTransaction) throws RuntimeException {
		if(accountTransaction.getFromAccountId().equals(accountTransaction.getToAccountId())) {
			throw new TransactionException("Source and Destination account id is same!");
		}
		if(accountsRepository.transcation(accountTransaction)) {
			notificationService.notifyAboutTransfer(getAccount(accountTransaction.getFromAccountId()), accountTransaction.getAmount()+" transferred");
			notificationService.notifyAboutTransfer(getAccount(accountTransaction.getToAccountId()), accountTransaction.getAmount()+" transferred");
		}
		return true;
	}

	public AccountsRepository getAccountsRepository() {
		return accountsRepository;
	}
	
}
