package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.InteracTransfer.Event;
import fall2018.csc2017.GameCentre.InteracTransfer.Friend;
import fall2018.csc2017.GameCentre.R;


public class EventActivity extends AppCompatActivity {

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
        // TODO: Add input from the cloud.
        Friend f1 = new Friend("Zuhab Wasim", "z-wasim-786@live.com");
        Friend f2 = new Friend("Jimmy Tan", "jtan5372@gmail.com");
        Friend f3 = new Friend("Alex Quach", "alex.quach1234@gmail.com");
        Friend f4 = new Friend("Frederick Yao", "yuzhou.yao@mail.utoronto.ca");
        friends.add(f1);
        friends.add(f2);
        friends.add(f3);
        friends.add(f4);

        //Sets the view
        setContentView(R.layout.activity_event_starting);
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
                        //TODO: Add this new friend to the database
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
                Event currentEvent = new Event(eventName);
                //TODO: Save this event to be used with the expenses/activities.
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
