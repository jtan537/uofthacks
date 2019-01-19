package fall2018.csc2017.GameCentre.InteracTransfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private List<Friend> participants;

    private String eventName;

    public Event(String name) {
        this.eventName = name;
        this.participants = new ArrayList<>();
    }

    public void addFriend(Friend friend) {
        participants.add(friend);
    }

    public void newActivity(double cost, List<Friend> participant) {
        double personalCost = cost / participant.size();
        for (Friend f : participant) {
            f.increaseCost(personalCost);
        }
    }

    public void endOfEvent() throws IOException {
        MoneyRequest request = new MoneyRequest(this);
        request.sendRequest();
    }

    public List<Friend> getParticipants() {
        return participants;
    }

    public String getEventName() {
        return eventName;
    }
}

//package fall2018.csc2017.GameCentre.DataManagers;
//
//        import java.text.NumberFormat;
//        import java.util.Map;
//
//public class Event {
//    String eventName;
//    public Map<String,Double> friendCosts;
//    String receipt = "";
//
//    public Event(String eventName, Map<String,Double> friendCosts) {
//        this.eventName = eventName;
//        this.friendCosts = friendCosts;
//        receipt = eventName + ":\n";
//    }
//
//    public void addExpense(Expense expense) {
//        String expenseReceipt = "\n";
//        expenseReceipt += padLeft(expense.name,30) + " Cost ($)  \n";
//        double perFriendCost = expense.cost / expense.friends.size();
//        for (String friend : expense.friends) {
//            expenseReceipt += padLeft(friend,30) + padRight(toCurrency(perFriendCost), 5);//" Cost\n";
//            friendCosts.put(friend, friendCosts.get(friend) + perFriendCost);
//        }
//        receipt += expenseReceipt;
//    }
//
//    private static String padRight(String s, int n) {
//        return String.format("%1$-" + n + "s", s);
//    }
//
//    private static String padLeft(String s, int n) {
//        return String.format("%1$" + n + "s", s);
//    }
//
//    private static String toCurrency(double money){
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        return formatter.format(money);
//    }
//}