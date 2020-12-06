package bank;

public class Bank {
    void getMoney(User user, Account account, int value) {
        checkRights(user, account);
        checkAmount(account, value);

        account.setAmount(account.getAmount() - value);
    }

    void addMoney(User user, Account account, int value) {
        checkRights(user, account);
        account.setAmount(account.getAmount() + value);
    }

    void passMoney(User user, Account from, Account to, int value) {
        checkRights(user, from);
        checkAmount(from, value);

        from.setAmount(from.getAmount() - value);
        to.setAmount(to.getAmount() + value);
    }
    
    private void checkRights(User user, Account account) {
        if (user.getId() != account.getOwner()) {
            throw new IllegalArgumentException(user.getName() + " isn't owner");
        }
    }

    private void checkAmount(Account account, int minimum) {
        if (account.getAmount() < minimum) {
            throw new IllegalArgumentException("Not enough money");
        }
    }
}
