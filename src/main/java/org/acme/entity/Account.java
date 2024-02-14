package org.acme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
