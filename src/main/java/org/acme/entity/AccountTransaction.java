package org.acme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "account_transactions")
public class AccountTransaction {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_account_transactions")
	@SequenceGenerator(name = "seq_account_transactions", sequenceName = "seq_account_transaction_id", allocationSize = 1)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_account_transactions"))
	private Account account;

	@Column(name = "amount")
	private Integer amount;

	@Column(name = "transaction_type", length = 1)
	private String transactionType;

	@Column(name = "description", length = 10)
	private String description;

	@Column(name = "ocurred_at")
	private LocalDateTime occurredAt;

}
