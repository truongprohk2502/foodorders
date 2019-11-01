package code.service;

import code.model.Account;

public interface AccountService {

    public Account findAccountByUsername(String username);

    public void save(Account account);
}
