package com.dws.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.dws.entity.Account;
import com.dws.entity.AccountTransaction;
import com.dws.exception.DuplicateAccountIdException;
import com.dws.exception.InsufficientAmountException;
import com.dws.exception.TransactionException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountsRepositoryInMemory.class);
	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}
	@Override
	public void clearAccounts() {
		accounts.clear();
	}
	
	@Override
	public boolean transcation(AccountTransaction accountTransaction) throws RuntimeException {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			LOGGER.info("Account operation started!");
			Account fromAccount = getAccount(accountTransaction.getFromAccountId());
			Account toAccount = getAccount(accountTransaction.getToAccountId());
			if (fromAccount == null) {
				throw new TransactionException("Source Account id " + fromAccount.getAccountId() + " does not exists!");
			}
			if (toAccount == null) {
				throw new TransactionException("Source Account id " + toAccount.getAccountId() + " does not exists!");
			}
			if(fromAccount.getBalance().compareTo(accountTransaction.getAmount()) < 0) {
				throw new InsufficientAmountException("Account id " + fromAccount.getAccountId() + " insufficiant amount!");
			}
			BigDecimal drAccountBalance = fromAccount.getBalance().subtract(accountTransaction.getAmount());
			BigDecimal crAccountBalance = toAccount.getBalance().add(accountTransaction.getAmount());
			accounts.replace(fromAccount.getAccountId(), new Account(fromAccount.getAccountId(),drAccountBalance));
			accounts.replace(toAccount.getAccountId(), new Account(toAccount.getAccountId(),crAccountBalance));
			LOGGER.info("Transaction completed with no error!");
			return true;
		}catch(RuntimeException ex) {
			LOGGER.info("Transaction Interupted!");
			throw ex;
		}finally {
			writeLock.unlock();
		}
		
	}

}
