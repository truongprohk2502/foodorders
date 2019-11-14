package code.service;

import code.model.Account;
import code.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmailAndRoleIsNot(email, "ROLE_GUEST");
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
