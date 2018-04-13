package com.dws.service;

import com.dws.entity.Account;

public interface NotificationService {

	void notifyAboutTransfer(Account account, String transferDescription);
}
