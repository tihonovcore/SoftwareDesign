package bank;

public class RunBank {
    public static void main(String[] args) {
        Bank bank = new Bank();

        User vlad = new User(1, "vlad");
        User petr = new User(2, "petr");

        Account vladAccount = new Account(1, 100);
        Account petrAccount = new Account(2, 100);

        System.out.println("vlad: " + vladAccount);
        System.out.println("petr: " + petrAccount);

        bank.addMoney(vlad, vladAccount, 100);
        System.out.println("vlad: " + vladAccount.getAmount());
        System.out.println("petr: " + petrAccount.getAmount());

        bank.getMoney(petr, petrAccount, 100);
        System.out.println("vlad: " + vladAccount.getAmount());
        System.out.println("petr: " + petrAccount.getAmount());

        bank.passMoney(petr, vladAccount, petrAccount, 100);
        System.out.println("vlad: " + vladAccount.getAmount());
        System.out.println("petr: " + petrAccount.getAmount());

        //todo: add long tests
        //todo: add tests with much calls
    }
}
