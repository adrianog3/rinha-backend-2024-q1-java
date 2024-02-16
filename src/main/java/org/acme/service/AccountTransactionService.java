package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.acme.dto.*;
import org.acme.entity.Account;
import org.acme.entity.AccountTransaction;
import org.acme.exception.BalanceException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AccountTransactionService {

	private final EntityManager em;

	@Inject
	public AccountTransactionService(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public NewBalanceDto performTransaction(int clientId, NewTransactionDto transactionDto) {
		Account account = em.find(Account.class, clientId);

		if (account == null) {
			throw new EntityNotFoundException(String.format("Cliente com id %s não encontrado", clientId));
		}

		int newBalance = account.getBalance() - transactionDto.valor();
		int diff = account.getLimit() - Math.abs(newBalance);

		if (diff < 0) {
			throw new BalanceException(String.format("A transação ultrapassou o limite de %s centavos", account.getLimit()));
		}

		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.setAmount(transactionDto.valor());
		accountTransaction.setTransactionType(transactionDto.tipo());
		accountTransaction.setDescription(transactionDto.descricao());
		accountTransaction.setAccount(account);
		accountTransaction.setOccurredAt(LocalDateTime.now());

		account.setBalance(newBalance);

		em.persist(accountTransaction);
		em.persist(account);

		return new NewBalanceDto(account.getLimit(), newBalance);
	}

	public AccountStatementDto getAccountStatement(int clientId) {
		Account account = em.find(Account.class, clientId);

		if (account == null) {
			throw new EntityNotFoundException(String.format("Cliente com id %s não encontrado", clientId));
		}

		String query = "SELECT t FROM AccountTransaction t WHERE t.account.clientId = ?1";

		List<AccountTransactionDto> transactions = em.createQuery(query, AccountTransaction.class)
			.setParameter(1, clientId)
			.setMaxResults(10)
			.getResultStream()
			.map(accountTransaction -> new AccountTransactionDto(
				accountTransaction.getAmount(),
				accountTransaction.getTransactionType(),
				accountTransaction.getDescription(),
				accountTransaction.getOccurredAt()
			))
			.toList();

		BalanceDto balanceDto = new BalanceDto(account.getBalance(), LocalDateTime.now(), account.getLimit());

		return new AccountStatementDto(balanceDto, transactions);
	}

}
