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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fall2018.csc2017.GameCentre.UserInterfaceElements.ExpenseAdder;


public class EventActivity extends AppCompatActivity {

    private static final String USER_EMAIL = "Email";
    private static final String KEY_FRIENDS = "FRIEND EMAIL";
    private static final String TAG = "EventActivity";

    private UserProperty user = new UserProperty();

    private String userEmail;

    private FirebaseFirestore db ;
    private CollectionReference friendsRef;
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

        //Fetches the current user
        userEmail = getIntent().getStringExtra("CurUser");

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);


        db = FirebaseFirestore.getInstance();
        friendsRef  = db.collection(String.format("Users/%s/Friends", userEmail));
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
                //switchToGame(ExpenseAdder.class);

                addFriends(emailName);
                List<String> friends = getFriends();
                for(String friend: friends){
                    Toast.makeText(getApplicationContext(), friend, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addFriends(String emailName) {

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_FRIENDS, emailName);

        friendsRef.add(user);

        Toast.makeText(getApplicationContext(),
                emailName,
                Toast.LENGTH_SHORT).show();
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
                    System.out.println(friends.toString());
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
