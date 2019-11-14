package code.service;

import code.model.Account;

public interface AccountService {

    Account findAccountByUsername(String username);

    Account findAccountByEmail(String email);

    void save(Account account);
}
