package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.acme.dto.NewTransactionDto;
import org.acme.dto.NewBalanceDto;
import org.acme.entity.Account;
import org.acme.entity.AccountTransaction;
import org.acme.exception.BalanceException;

import java.time.LocalDateTime;

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

}
