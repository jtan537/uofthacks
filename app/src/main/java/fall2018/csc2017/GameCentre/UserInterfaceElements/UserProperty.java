package fall2018.csc2017.GameCentre.UserInterfaceElements;

import java.util.ArrayList;
import java.util.List;

public class UserProperty {

    private String email;
    private List<String> friends;

    public UserProperty(){};

    public String getEmail() {
        return email;
    }

    public UserProperty(String email, List<String> friends){
        this.email = email;
        this.friends = friends;
    }

    public List<String> getFriends() { return friends; }

    public void addFriend(String email){
        this.friends.add(email);
    }
}
