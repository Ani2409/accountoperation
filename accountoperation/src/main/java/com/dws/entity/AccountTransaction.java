package com.dws.entity;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class AccountTransaction {
	
	@NotNull
	@NotEmpty
	private final String fromAccountId;
	@NotNull
	@NotEmpty
	private final String toAccountId;
	@NotNull
	@Min(value = 1, message = "Initial balance must be positive.")
	private final BigDecimal amount;
	
	@JsonCreator
	public AccountTransaction(@JsonProperty("fromAccountId")String fromAccountId, @JsonProperty("toAccountId")String toAccountId, @JsonProperty("amount")BigDecimal amount) {
		super();
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	
}
