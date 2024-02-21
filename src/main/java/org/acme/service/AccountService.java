package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.acme.dto.*;
import org.acme.entity.Account;
import org.acme.entity.AccountTransaction;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class AccountService {

	private final EntityManager em;

	@Transactional
	public NewBalanceDto performTransaction(int clientId, NewTransactionDto newTransactionDto) {
		NewBalanceDto newBalanceDto = updateBalance(clientId, newTransactionDto);

		saveTransaction(clientId, newTransactionDto);

		return newBalanceDto;
	}

	private void saveTransaction(int clientId, NewTransactionDto newTransactionDto) {
		Account account = new Account();
		account.setClientId(clientId);

		AccountTransaction accountTransaction = new AccountTransaction();
		accountTransaction.setAmount(newTransactionDto.amount());
		accountTransaction.setTransactionType(newTransactionDto.transactionType());
		accountTransaction.setDescription(newTransactionDto.description());
		accountTransaction.setAccount(account);
		accountTransaction.setOccurredAt(LocalDateTime.now());

		em.persist(accountTransaction);
	}

	private NewBalanceDto updateBalance(int clientId, NewTransactionDto newTransactionDto) {
		Account account = em.find(Account.class, clientId, LockModeType.PESSIMISTIC_WRITE);

		if (account == null) {
			throw new EntityNotFoundException("Cliente não encontrado");
		}

		account.setBalance(
			newTransactionDto.transactionType().equals("c")
				? account.getBalance() + newTransactionDto.amount()
				: account.getBalance() - newTransactionDto.amount()
		);

		em.persist(account);

		return new NewBalanceDto(account.getLimit(), account.getBalance());
	}

	public AccountStatementDto getStatement(int clientId) {
		Account account = em.find(Account.class, clientId);

		if (account == null) {
			throw new EntityNotFoundException("Cliente não encontrado");
		}

		String query = "SELECT t FROM AccountTransaction t WHERE t.account.clientId = ?1 ORDER BY t.occurredAt DESC";

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
