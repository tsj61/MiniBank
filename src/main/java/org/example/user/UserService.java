package org.example.user;

import org.example.account.Account;
import org.example.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.example.helpers.ValidatorHelper.*;

@Component
public class UserService {

    private final Map<Integer, User> userMap;
    private final AccountService accountService;
    private final Set<String> allLogins;
    private int idCounter;

    @Autowired
    public UserService(AccountService accountService) {
        this.userMap = new HashMap<>();
        this.accountService = accountService;
        this.allLogins = new HashSet<>();
        this.idCounter = 0;
    }

    public User createUser(String login) {
        login = validateLogin(login);

        return createUserWithDefaultAccount(login);
    }

    public List<User> findAllUsers() {
        return userMap.values().stream().toList();
    }

    public User findUserById(Integer id) {
        validatePositiveParam(id, "User id");

        User user = userMap.get(id);
        validateUserNotNull(user, id);

        return user;
    }

    private User createUserWithDefaultAccount(String login) {
        idCounter++;
        User user = new User(idCounter, login, new ArrayList<>());
        Account account = accountService.createAccount(user);
        user.getAccountList().add(account);

        userMap.put(idCounter, user);
        allLogins.add(login);

        return user;
    }

    private String validateLogin(String login) {
        validateLoginNotNullAndBlank(login);

        login = login.trim();

        if (allLogins.contains(login)) {
            throw new IllegalArgumentException(
                    "User already exists with this login %s".formatted(login));
        }

        return login;
    }

}
