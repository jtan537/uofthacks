package fall2018.csc2017.GameCentre.InteracTransfer;

public class Friend {
    private String name;
    private String email;
    private double moneyAmount;

    public Friend(String name, String email) {
        this.email = email;
        this.name = name;
        this.moneyAmount = 0;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void increaseCost(double amount) {
        this.moneyAmount += amount;
    }
}
