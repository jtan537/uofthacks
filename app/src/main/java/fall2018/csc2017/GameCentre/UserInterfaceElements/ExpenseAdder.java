package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import fall2018.csc2017.GameCentre.InteracTransfer.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fall2018.csc2017.GameCentre.InteracTransfer.Friend;
import fall2018.csc2017.GameCentre.R;

public class ExpenseAdder extends AppCompatActivity {

    /**
     * The GridView to display all score elements.
     */
    private GridView checkGridView;

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
     * Constant for the number of columns in the friend board.
     */
    private int NUM_COLUMNS = 3;

    /**
     * Constant for the number of rows based on the list.
     */
    private int NUM_ROWS = 10;//friends.size();

    /**
     * Each TextView representing each item to display in the GridView
     */
    private ArrayList<View> checkItems = new ArrayList<>();



    /**
     * The friend list from the cloud.
     */
    List<Friend> friends = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_adder);

        // jimmy IMPORTS
        //Fetches the current user
        userEmail = getIntent().getStringExtra("userEmail");

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        db = FirebaseFirestore.getInstance();
        friendsRef  = db.collection("Users").document(userEmail).collection("Friends");
        // JIMMY IMPORTS

        // THIS IS WHERE I GET THE FRIENDS LIST FROM THE CLOUD

        //CheckBox chkbox = new CheckBox();

        //Adds the necessary buttons
        addNextExpenseButtonListener();
        addDoneEventButtonListener();
        addUpdateButtonListener();

        // Adds output of scoreboard's GridView
        checkGridView = findViewById(R.id.checkGridView);
        checkGridView.setNumColumns(NUM_COLUMNS);

        addCheckGridListenerOnSpinnerSelection();

        getFriends();
        // Generates the friend grid.
        generateCheckGrid(friends);


    }
    /**
     * Selects the game to display the leader board for and updates the values every time
     * the user changes the game selected in the Spinner.
     * <p>
     * Also regenerates the GridView board if the user selects a different game.
     */
    public void addCheckGridListenerOnSpinnerSelection() {
        final Spinner gameSpinner = findViewById(R.id.checkGridSpinner);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                generateCheckGrid(friends);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void getFriends(){

        db.collection("Users").document(userEmail).collection("Friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //System.out.println(document.getData());
                                //{Name and Email=yooyo eee@r.s}
                                String st = document.getData().toString();

                                st = st.substring(st.indexOf("="));
                                if (!st.equals("=}")) {
                                    st.substring(1,st.length() - 1);

                                    String items[] = st.trim().split(" ");
                                    items[0] = items[0].substring(1);
                                    items[1] = items[1].substring(0,items[1].length() - 1);

                                    //System.out.println("[" + items[0] + ", " + items[1] + "]");
                                    Log.d(TAG, "[" + items[0] + ", " + items[1] + "]");
                                    Friend curr = new Friend(items[0].trim(), items[1].trim());
                                    friends.add(curr);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addNextExpenseButtonListener() {
        Button nextExpense = findViewById(R.id.nextExpenseButton);
        nextExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToGame(ExpenseAdder.class);
                Toast.makeText(getApplicationContext(),
                        "Only one expense sorry!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addUpdateButtonListener() {
        Button nextExpense = findViewById(R.id.updateButton);
        nextExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switchToGame(ExpenseAdder.class);
                Toast.makeText(getApplicationContext(),
                        "Only one expense sorry!",
                        Toast.LENGTH_SHORT).show();
                getFriends();
                generateCheckGrid(friends);
            }
        });
    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addDoneEventButtonListener() {
        Button doneEvent = findViewById(R.id.doneEventButton);
        doneEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Friend> participants = new ArrayList<>();
                int counter = 0;

                int first = checkGridView.getFirstVisiblePosition();
                int last = checkGridView.getLastVisiblePosition();

                boolean owner = false;
                CheckBox yourself = (CheckBox) checkGridView.getItemAtPosition(first);
                if (yourself.isChecked()) {
                    owner = true;
                }
                String st2 = "";
                for(int i=first + 1; i<=last; ++i){
                    CheckBox item = (CheckBox) checkGridView.getItemAtPosition(i);
                    st2 += (item.isChecked()) ? "1" : "0";
                    if (item.isChecked()) {
                        participants.add(friends.get(counter));
                    }
                    counter++;
                }
                Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();

                EditText eventText = findViewById(R.id.expenseNameText);
                EditText costText = findViewById(R.id.expenseCostText);
                String event = eventText.getText().toString().trim().toLowerCase();
                String costST = costText.getText().toString().trim().toLowerCase();
                double cost = Double.parseDouble(costST);
//                if (participants.size() > 0 && participants.get(0).get)

                Event currentEvent = (Event) getIntent().getSerializableExtra("currentEvent");
                System.out.println(currentEvent);
                // all the friends who came to EVENT
                for (Friend f: friends) {
                    currentEvent.addFriend(f);
                }

                currentEvent.newActivity(cost, participants, owner); //and owner after pull

                try {
                    currentEvent.endOfEvent();
                    Toast.makeText(getApplicationContext(), "Requests Sent!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    System.out.println("Something went wrong!");
                }
//                // all the people who came that have to pay for this ACTIVITY
//                for (Friend participant: participants) {
//                    currentEvent.addFriend(participant);
//                }

                //st += st2 + " " + event + " " + cost;
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Generates the actual leader board based on specified inputs. This method is executed on
     * default values to ensure some generation of the board on activity start up.
     */
    private void generateCheckGrid(List<Friend> friends) {
        //initializeScores(this);
        updateScores(friends);
        display();
    }

    /**
     * Displays the scoreboard to the activity's GridView.
     */
    public void display() {
        checkGridView.setAdapter(new CheckGridAdapter(checkItems,
                checkGridView.getColumnWidth(),
                checkGridView.getHeight() / (NUM_ROWS + 1)));
    }

    /**
     * Updates the elements of the scoreboard given what the user wants in terms of
     * game type, game difficulty, and which users to show.
     */
    public void updateScores(List<Friend> friends) {

        checkItems.clear();
        CheckBox yourself = new CheckBox(this);
        yourself.setText("Yourself");
        checkItems.add(yourself);
        for (int i = 0; i < friends.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(friends.get(i).getName());
            checkItems.add(checkBox);
        }
    }



//    /**
//     * Helper method that updates the score item for the grid
//     * with new values at the given position.
//     *
//     * @param index: The position of the item to update.
//     * @param text:  The new text the item will get.
//     */
//    private void updateItemText(int index, String text) {
//        TextView temp;
//        temp = (TextView) checkItems.get(index);
//        temp.setText(text);
//        checkItems.set(index, temp);
//    }
}
