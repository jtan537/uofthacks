package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import fall2018.csc2017.GameCentre.InteracTransfer.Event;
import fall2018.csc2017.GameCentre.InteracTransfer.Friend;
import fall2018.csc2017.GameCentre.R;


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
     * The friend list from the cloud.
     */
    List<Friend> friends = new ArrayList<>();
    /**
     * The GridView to display all score elements.
     */
    private GridView friendGridView;

    /**
     * Constant for the number of columns in the friend board.
     */
    private int NUM_COLUMNS = 2;

    /**
     * Constant for the number of rows based on the list.
     */
    private int NUM_ROWS = 10;//friends.size();

    /**
     * Each TextView representing each item to display in the GridView
     */
    private ArrayList<View> friendItems = new ArrayList<>();

    /**
     * What happens when the activity is loaded
     *
     * @param savedInstanceState Represents the current activity's last previous saved state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fetches the current user

        // THIS IS WHERE I GET THE FRIENDS LIST FROM THE CLOUD
          // Initialize friend list.

//        Friend f1 = new Friend("Zuhab Wasim", "z-wasim-786@live.com");
//        Friend f2 = new Friend("Jimmy Tan", "jtan5372@gmail.com");
//        Friend f3 = new Friend("Alex Quach", "alex.quach1234@gmail.com");
//        Friend f4 = new Friend("Frederick Yao", "yuzhou.yao@mail.utoronto.ca");
//        friends.add(f1);
//        friends.add(f2);
//        friends.add(f3);
//        friends.add(f4);

        //Sets the view
        setContentView(R.layout.activity_event_starting);

        // jimmy IMPORTS
        //Fetches the current user
        userEmail = getIntent().getStringExtra("userEmail");
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        db = FirebaseFirestore.getInstance();
        friendsRef  = db.collection("Users").document(userEmail).collection("Friends");
        // Initialize collection with dummy email document to enable get friends
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_FRIENDS, "");
        friendsRef.add(user);
        // JIMMY IMPORTS

        friends = getFriendObjects();
        //Adds the necessary buttons
        addCreateEventButtonListener();
        addLoadEventButtonListener();
        addAddFriendButtonListener();
        addSeeReceiptsButtonListener();
        addGridListenerOnSpinnerSelection();

        // Adds output of scoreboard's GridView
        friendGridView = findViewById(R.id.friendsGridView);
        friendGridView.setNumColumns(NUM_COLUMNS);

        // Generates the friend grid.
        generateFriendGrid(friends);
    }

    /**
     * Selects the game to display the leader board for and updates the values every time
     * the user changes the game selected in the Spinner.
     * <p>
     * Also regenerates the GridView board if the user selects a different game.
     */
    public void addGridListenerOnSpinnerSelection() {
        final Spinner gameSpinner = findViewById(R.id.gridViewSpinner);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                generateFriendGrid(friends);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
                Toast.makeText(getApplicationContext(),
                        "No event to load!",
                        Toast.LENGTH_SHORT).show();
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

    public  List<Friend> getFriendObjects(){
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
        System.out.println(userEmail);
        db.collection("Users").document("brandon@gmail.com").collection("Friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.out.println("***************");
                    System.out.println("DIDNT LISTEN");
                    System.out.println("***************");
                }
                for(DocumentSnapshot doc: queryDocumentSnapshots){
                    friends.add((String)doc.get(KEY_FRIENDS)); // Will always be friend emails
                }
            }
        });
        return friends;
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
                String friendName = friendNameText.getText().toString().trim().toLowerCase();
                String emailName = emailNameText.getText().toString().trim().toLowerCase();

                //jimmy imports
                String emailPlusName = friendName +' '+ emailName;

                //jimmy imports

                //switchToGame(ExpenseAdder.class);
                if (!friendName.equals("") && !emailName.equals("")) {
                    boolean found = false;
                    for (Friend f : friends) {
                        if (f.getEmail().trim().toLowerCase().equals(emailName)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        Toast.makeText(getApplicationContext(),"New friend Added!", Toast.LENGTH_SHORT).show();
                        friends.add(new Friend(friendName, emailName));
                        addFriends(emailPlusName);
                        generateFriendGrid(friends);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Email is already registered!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please have both fields filled!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                EditText eventNameText = findViewById(R.id.eventNameText);
                String eventName = eventNameText.getText().toString().trim().toLowerCase();
                //Event currentEvent = new Event(eventName);
                //TODO: Save this event to be used with the expenses/activities.

                switchToGame(eventName);
            }
        });
    }
    /**
     * Switches to game that was selected
     */
    private void switchToGame(String eventName) {
        Intent tmp = new Intent(this, ExpenseAdder.class);
        Event event = new Event(eventName);
        tmp.putExtra("userEmail", userEmail);
        tmp.putExtra("currentEvent", event);
        //Store the current user in the next activity
        startActivity(tmp);
    }

    /**
     * Generates the actual leader board based on specified inputs. This method is executed on
     * default values to ensure some generation of the board on activity start up.
     */
    private void generateFriendGrid(List<Friend> friends) {

        initializeScores(this);
        updateScores(friends);
        display();
    }

    /**
     * Creates and sets all the scores in scoreItems as null-strings.
     *
     * @param context: The given context of the class activity.
     */
    public void initializeScores(Context context) {
        friendItems.clear();
        for (int row = 0; row < (friends.size() + 1) * NUM_COLUMNS; row++) {
            TextView temp = new TextView(context);
            temp.setText("");
            this.friendItems.add(temp);
        }
    }

    /**
     * Displays the scoreboard to the activity's GridView.
     */
    public void display() {
        friendGridView.setAdapter(new FriendGridAdapter(friendItems,
                friendGridView.getColumnWidth(),
                friendGridView.getHeight() / (NUM_ROWS + 1)));
    }

    /**
     * Updates the elements of the scoreboard given what the user wants in terms of
     * game type, game difficulty, and which users to show.
     */
    public void updateScores(List<Friend> friends) {

        // Initial header of Rank, User, Score, and possibly more.
        updateItemText(0, "Friend");
        updateItemText(1, "E-Mail");

        for (int i = 1; i < (friends.size() + 1); i++) {
            // Change the rank, username, and score
            updateItemText(NUM_COLUMNS * i, friends.get(i - 1).getName());
            updateItemText(NUM_COLUMNS * i + 1, friends.get(i - 1).getEmail());
        }
    }

    /**
     * Helper method that updates the score item for the grid
     * with new values at the given position.
     *
     * @param index: The position of the item to update.
     * @param text:  The new text the item will get.
     */
    private void updateItemText(int index, String text) {
        TextView temp;
        temp = (TextView) friendItems.get(index);
        temp.setText(text);
        friendItems.set(index, temp);
    }

}
