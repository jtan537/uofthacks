package fall2018.csc2017.GameCentre.InteracTransfer;

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //MoneyRequest.generateRequest("Was", "zuhab.wasim@gmail.com", "1000", "This is a testing");
        Event dinner = new Event("Dinner");
        Friend was = new Friend("was", "zuhab.wasim@gmail.com");
        Friend was2 = new Friend("was2", "yuzhou.yao1234@beta.inter.ac");
        dinner.addFriend(was);
        dinner.addFriend(was2);
        List<Friend> food = new ArrayList<>();
        List<Friend> bbt = new ArrayList<>();
        food.add(was);
        bbt.add(was);
        bbt.add(was2);
        dinner.newActivity(2000, food, true);
        dinner.newActivity(100, bbt, false);
        dinner.endOfEvent();
    }
}

