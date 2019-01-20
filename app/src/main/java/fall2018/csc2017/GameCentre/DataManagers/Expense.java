package fall2018.csc2017.GameCentre.DataManagers;

import java.util.List;

public class Expense {

    public String name;
    public double cost;
    public List<String> friends;

    public Expense(String name, double cost, List<String> friends) {
        this.name = name;
        this.cost = cost;
        this.friends = friends;
    }
}
