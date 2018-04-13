package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.dws.config.MyMVCConfig;
import com.dws.entity.Account;
import com.dws.entity.AccountTransaction;
import com.dws.exception.DuplicateAccountIdException;
import com.dws.exception.InsufficientAmountException;
import com.dws.service.AccountsService;

import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {MyMVCConfig.class},webEnvironment=WebEnvironment.RANDOM_PORT)
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-001", new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-001")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  @Test
  public void testTransaction() throws Exception {
	  Account account = new Account("Id-002", new BigDecimal(1000));
	  this.accountsService.createAccount(account);
	  account = new Account("Id-003", new BigDecimal(1000));
	  this.accountsService.createAccount(account);
	  AccountTransaction AccountTransaction = new AccountTransaction("Id-002","Id-003", new BigDecimal(500));
	  this.accountsService.transaction(AccountTransaction);
	  assertThat(this.accountsService.getAccountsRepository().getAccount("Id-002").getBalance()).isEqualByComparingTo("500");
	  assertThat(this.accountsService.getAccountsRepository().getAccount("Id-003").getBalance()).isEqualByComparingTo("1500");
  }
  
  @Test
  public void testTransactionInsufficientFund() throws Exception {
	  Account drAccount=null;
	  Account crAccount=null;
  	  try {
  		  drAccount = new Account("Id-004", new BigDecimal(1000));
		  this.accountsService.createAccount(drAccount);
		  crAccount = new Account("Id-005", new BigDecimal(1000));
		  this.accountsService.createAccount(crAccount);
		  AccountTransaction AccountTransaction = new AccountTransaction("Id-004","Id-005", new BigDecimal(1500));
		  this.accountsService.transaction(AccountTransaction);
		  
  	  } catch (InsufficientAmountException ex) {
	      assertThat(ex.getMessage()).isEqualTo("Account id "+ drAccount.getAccountId() +" insufficiant amount!");
	    }
  }
  @Test
  public void transactionTestSuit() {
	  	Account account = new Account("Id-006", new BigDecimal(3000));
	    this.accountsService.createAccount(account);
	    account = new Account("Id-007", new BigDecimal(1500));
	    this.accountsService.createAccount(account);
		TestSuite suite = TestSuite.create("the_test_suite");
		suite.test("my_test_case_1", context -> {
			this.testParallelTransaction("Id-006","Id-007", new BigDecimal(500));
		});
		suite.test("my_test_case_2", context -> {
			this.testParallelTransaction("Id-006","Id-007", new BigDecimal(500));
		});
		suite.test("my_test_case_3", context -> {
			this.testParallelTransaction("Id-006","Id-007", new BigDecimal(500));
		});
		suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
		
		assertThat(this.accountsService.getAccountsRepository().getAccount("Id-006").getBalance()).isEqualByComparingTo("1500");
		assertThat(this.accountsService.getAccountsRepository().getAccount("Id-007").getBalance()).isEqualByComparingTo("3000");
		
  }
  private void testParallelTransaction(String source, String destination, BigDecimal amount) {
	  AccountTransaction AccountTransaction = new AccountTransaction(source,destination, amount);
	  this.accountsService.transaction(AccountTransaction);
  }
}
