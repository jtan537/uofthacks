package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fall2018.csc2017.GameCentre.InteracTransfer.Friend;
import fall2018.csc2017.GameCentre.UserInterfaceElements.ExpenseAdder;
import fall2018.csc2017.GameCentre.UserInterfaceElements.UserProperty;


public class EventActivity extends AppCompatActivity {

    // JIMMY IMPORTS
    private static final String USER_EMAIL = "Email";
    private static final String KEY_FRIENDS = "Name and Email";
    private static final String TAG = "EventActivity";
    private UserProperty user = new UserProperty();
    private String userEmail;
    private FirebaseFirestore db ;
    private CollectionReference friendsRef;
    // JIMMY IMPORTS

    /**
     * The current logged in user
     */
    /**
     * What happens when the activity is loaded
     *
     * @param savedInstanceState Represents the current activity's last previous saved state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_starting);

        // jimmy IMPORTS
        //Fetches the current user
        userEmail = getIntent().getStringExtra("CurUser");

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        db = FirebaseFirestore.getInstance();

        friendsRef  = db.collection(String.format("Users/%s/Friends", userEmail));

        // Initialize collection with dummy email document to enable get friends
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_FRIENDS, "");
        friendsRef.add(user);
        // JIMMY IMPORTS

//        //Make general later
//
//        //Fetches the high scores
//        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, sliding tiles
//        checkIfEmpty(LeaderboardActivity.SLIDING_SAVE_FILENAME, false);
//        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, minesweeper
//        checkIfEmpty(LeaderboardActivity.MINESWEEPER_SAVE_FILENAME, false);
//        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, PowersPlus
//        checkIfEmpty(LeaderboardActivity.POWERS_SAVE_FILENAME, true);
//
//        //Reinitialize difficulty
//        difficulty = null;
//
        //Fetches the current user
        //Sets the view
        setContentView(R.layout.activity_event_starting);
//        //Adds the necessary buttons
        addCreateEventButtonListener();
        addLoadEventButtonListener();
        addAddFriendButtonListener();
        addSeeReceiptsButtonListener();
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addLoadEventButtonListener() {
        Button loadEvent = findViewById(R.id.loadEventButton);
        loadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToGame(ExpenseAdder.class);
                Toast.makeText(getApplicationContext(), userEmail, Toast.LENGTH_SHORT).show();;
            }
        });
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addAddFriendButtonListener() {
        Button addFriend = findViewById(R.id.addFriendButton);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the two text fields and get the friend name and email from them.
                EditText friendNameText = findViewById(R.id.friendNameText);
                EditText emailNameText = findViewById(R.id.friendEmailText);
                String friendName = friendNameText.getText().toString();
                String emailName = emailNameText.getText().toString();

                String emailPlusName = friendName +' '+ emailName;
                //switchToGame(ExpenseAdder.class);

                addFriends(emailPlusName);
//                for(String friend: friends){
//                    Toast.makeText(getApplicationContext(), friend, Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void addFriends(String emailName) {
        List<String> curFriends = getFriends();
        System.out.println(curFriends.toString());
        boolean hasEmail = false;
        for(String friend: curFriends){
            System.out.println(friend + " -friend");
            System.out.println(emailName+ " -emailname");
           if (friend.equals(emailName)){
               hasEmail = true;
               break;
           }
        }
        if(hasEmail == true){
            System.out.println("STOP IT HAS BAME");
            Toast.makeText(getApplicationContext(), "Friend email already registered!", Toast.LENGTH_SHORT).show();

        } else {
            System.out.println("GO NO NAME BABY");
            Map<String, Object> user = new HashMap<>();

            user.put(KEY_FRIENDS, emailName);


            friendsRef.add(user);

            Toast.makeText(getApplicationContext(),
                    emailName,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public List<Friend> getFriendObjects(){
        List<Friend> friendObjs = new ArrayList<>();
        List<String> friendNameMail = getFriends();

        List<String> names = new ArrayList<>();
        List<String> emails = new ArrayList<>();

        for (String namemail: friendNameMail){
            // Ignore the dummy empty name mail
            if (namemail.length() >= 2) {
                names.add(namemail.substring(0, namemail.indexOf(' ') - 1));
                emails.add(namemail.substring(namemail.indexOf(' ')));
            }
        }

        for (int i = 0; i != names.size(); i++){
            Friend curFriend = new Friend(names.get(i), emails.get(i));
            friendObjs.add(curFriend);
        }
        return friendObjs;
    }

    private List<String> getFriends(){
        final List<String> friends = new ArrayList<>();
        friendsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for(QueryDocumentSnapshot doc: task.getResult()){
                        friends.add((String)doc.get(KEY_FRIENDS)); // Will always be friend emails
                    }
                    //System.out.println(friends.toString());
                    Log.d(TAG, friends.toString());
                } else {
                    Log.d(TAG, "Error getting friend docs: ", task.getException());
                }
            }
        });
        return friends;
    }



    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addSeeReceiptsButtonListener() {
        Button seeReceipt = findViewById(R.id.receiptButton);
        seeReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToGame(ExpenseAdder.class);
                Toast.makeText(getApplicationContext(),
                        "No event to load!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addCreateEventButtonListener() {
        Button createEvent = findViewById(R.id.createEventButton);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(ExpenseAdder.class);
            }
        });
    }


    /**
     * Switches to game that was selected
     */
    private void switchToGame(Class nextActivity) {
        Intent tmp = new Intent(this, nextActivity);
        //Store the current user in the next activity
        startActivity(tmp);
    }
}
