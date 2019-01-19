package fall2018.csc2017.GameCentre.DataManagers;

import java.text.NumberFormat;
import java.util.Map;

public class Event {
    String eventName;
    public Map<String,Double> friendCosts;
    String receipt = "";

    public Event(String eventName, Map<String,Double> friendCosts) {
        this.eventName = eventName;
        this.friendCosts = friendCosts;
        receipt = eventName + ":\n";
    }

    public void addExpense(Expense expense) {
        String expenseReceipt = "\n";
        expenseReceipt += padLeft(expense.name,30) + " Cost ($)  \n";
        double perFriendCost = expense.cost / expense.friends.size();
        for (String friend : expense.friends) {
            expenseReceipt += padLeft(friend,30) + padRight(toCurrency(perFriendCost), 5);//" Cost\n";
            friendCosts.put(friend, friendCosts.get(friend) + perFriendCost);
        }
        receipt += expenseReceipt;
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    private static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    private static String toCurrency(double money){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(money);
    }
}
