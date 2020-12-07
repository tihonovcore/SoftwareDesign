package bank;

public class RunBank {
    public static void main(String[] args) {
        Bank bank = new Bank();

        User vlad = new User(1, "vlad");
        User petr = new User(2, "petr");

        Account vladAccount = new Account(1, 100);
        Account petrAccount = new Account(2, 100);

        for (int i = 0; i < 20; i++) {
            bank.addMoney(vlad, vladAccount, 100);
            bank.passMoney(vlad, vladAccount, petrAccount, 1);
        }

        longGetting(bank, petr, petrAccount);
    }

    private static void longGetting(Bank bank, User user, Account account) {
        try {
            Thread.sleep(2345L);
        } catch (InterruptedException ignore) {}

        bank.getMoney(user, account, 100);
    }
}
