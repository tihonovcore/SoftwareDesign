package bank;

public class Account {
    private final int owner;
    private int amount;

    public Account(int owner, int amount) {
        this.owner = owner;
        this.amount = amount;
    }

    public int getOwner() {
        return owner;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "owner=" + owner +
                ", amount=" + amount +
                '}';
    }
}
