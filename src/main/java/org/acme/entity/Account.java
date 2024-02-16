package org.acme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.acme.exception.BalanceException;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {

	@Id
	@Column(name = "client_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_accounts")
	@SequenceGenerator(name = "seq_accounts", sequenceName = "seq_account_id", allocationSize = 1)
	private Integer clientId;

	@Column(name = "balance")
	private Integer balance;

	@Column(name = "account_limit")
	private Integer limit;

	public void setBalance(Integer newBalance) {
		if ((newBalance + limit) < 0) {
			throw new BalanceException(String.format(
				"O saldo de %s ultrapassa o limite de %s centavos",
				newBalance, limit
			));
		}

		this.balance = newBalance;
	}

}
