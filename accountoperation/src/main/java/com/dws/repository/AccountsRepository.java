package com.dws.repository;

import com.dws.entity.Account;
import com.dws.entity.AccountTransaction;
import com.dws.exception.DuplicateAccountIdException;

public interface AccountsRepository {

	void createAccount(Account account) throws DuplicateAccountIdException;

	Account getAccount(String accountId);
	
	boolean transcation(AccountTransaction accountTransaction) throws RuntimeException;

	void clearAccounts();
}
