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
