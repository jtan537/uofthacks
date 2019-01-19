package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.User;
import fall2018.csc2017.GameCentre.DataManagers.UserManager;


/**
 * The initial activity for the game centre.
 */
public class LaunchCentre extends AppCompatActivity {

//    /**
//     * The list of games implemented so far
//     */
    //private final String gamesList[] = {"SlidingTiles", "MineSweeper", "PowersPlus"};

    /**
     * The main save for users.
     */
    public static final String USER_SAVE_FILENAME = "user_file.ser";

    /**
     * The user database
     */
    private UserManager userManager = new UserManager();

    /**
     * Handles the file operations for the user database
     */
    private FileManager<UserManager> userFileManager = new FileManager<>();

    /**
     * The onCreate for this activity
     *
     * @param savedInstanceState Represents the current activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks to see if the user database file is empty
        UserManager potentialNewManager;
        potentialNewManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, this);
        if (potentialNewManager == null) {
            //This creates a new file since one did not exist yet
            potentialNewManager = new UserManager();
            userFileManager.saveToFile(USER_SAVE_FILENAME, this, potentialNewManager);
        }

        //Change the view and create the buttons
        setContentView(R.layout.activity_email_launcher);
        //addSignUpButtonListener();
        //addLoginButtonListener();
        addEnterButtonListener();
    }

//    /**
//     * Activate the Sign up button.
//     */
//    private void addSignUpButtonListener() {
//        Button signUpButton = findViewById(R.id.SignUpButton);
//        //This handles the click of the button
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Create the two text fields and get the username and password from them
//                EditText passwordText = findViewById(R.id.passwordText);
//                EditText signUpText = findViewById(R.id.userNameText);
//                String password = passwordText.getText().toString();
//                String username = signUpText.getText().toString();
//
//                //Create the potential new user
//                User potentialUser = new User(username, password);
//
//                //Load the database from file
//                Activity host = (Activity) v.getContext();
//                userManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, host);
//
//                //Check if this user has already signed up
//                if (!userManager.hasUser(potentialUser) && !username.isEmpty() && !password.isEmpty()) {
//                    //Create a new saved games file for this new user
//                    potentialUser.createSavedGames();
//                    userManager.addUser(potentialUser);
//                    switchToGameSelector(potentialUser);
//                } else {
//                    //Display errors
//                    if (username.isEmpty()){
//                        //Blank username
//                        Toast.makeText(getApplicationContext(),
//                                "Please enter an email!",
//                                Toast.LENGTH_SHORT).show();
////                    }else if (password.isEmpty()){
////                        //Blank password
////                        makeToastBlankPassword();
//                    }else{
//                        //Displays an error when signing up if the account already exists
//                        String message = "This user already exists!";
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                //Save the updated database to file
//                userFileManager.saveToFile(USER_SAVE_FILENAME, host, userManager);
//            }
//        });
//    }

    /**
     * Activate the login button.
     */
    private void addEnterButtonListener() {
        Button enterButton = findViewById(R.id.enterButton);
        //This handles the click of the button
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the two text fields and get the username and password from them
                //EditText passwordText = findViewById(R.id.passwordText);
                EditText signUpText = findViewById(R.id.emailText);
                String username = signUpText.getText().toString();
                //String password = passwordText.getText().toString();

                //Create the potential logged in user
                User potentialLogin = new User(username, "");

                //Load the database
                Activity host = (Activity) v.getContext();
                userManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, host);

                if (userManager.hasUser(potentialLogin) && !username.isEmpty()) {

                    //Switches to the game selector if the password entered is correct
                    //The user already exists so no need to create a new saved games file
                    User curUser = userManager.getUser(potentialLogin);

                    //Adds new games to the user if they signed up before those new games were added
                    //fixOldUser(curUser);

                    switchToGameSelector(curUser);

                } else {
                    Pattern pattern = Pattern.compile("^[A-Za-z0-9.]*@[A-Za-z0-9.]*\\.[A-Za-z0-9]*$");
                    Matcher m = pattern.matcher(username);
                    //Display errors
                    if (username.isEmpty()){
                        //Blank username
                        Toast.makeText(getApplicationContext(),
                                "Please enter an email!",
                                Toast.LENGTH_SHORT).show();
                    }else if (!m.find()){
                        String message = "Invalid email!";
                        Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_SHORT).show();
                    }else{
                        userManager.addUser(potentialLogin);
                        switchToGameSelector(potentialLogin);
                        //Save the updated database to file
                        userFileManager.saveToFile(USER_SAVE_FILENAME, host, userManager);
                    }


                }


            }
        });

    }

//    /**
//     * Adds new games to an old user
//     *
//     * @param curUser The current logged in user
//     */
//    private void fixOldUser(User curUser) {
//        //Checks to see what games are missing if new games are added
//        if (curUser.userSavedGames.size() < gamesList.length){
//            for (String gameType : gamesList){
//                if (!curUser.userSavedGames.containsKey(gameType)){
//                    curUser.addSave(gameType);
//                }
//            }
//        }
//    }


//    /**
//     * Compares the password of the current field and the password stored in the database
//     * @param user the current user trying to log in
//     * @return True or False, depending on if the password matches or not
//     */
//    private boolean comparePasswords(User user) {
//        for (User u : userManager) {
//            if (u.getUsername().equals(user.getUsername())) {
//                return u.getPassword().equals(user.getPassword());
//            }
//        }
//        return false;
//    }

//    /**
//     * Display an sign up error
//     */
//    private void makeToastSignUpErrorText(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }

//    /**
//     * Display a blank username message
//     */
//    private void makeToastBlankUsername() {
//        Toast.makeText(this, "Please enter a username!", Toast.LENGTH_SHORT).show();
//    }

//    /**
//     * Display a blank password message
//     */
//    private void makeToastBlankPassword() {
//        Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show();
//    }

    /**
     * Switch to the Game selector view to select what game to play.
     */
    private void switchToGameSelector(User currentUser) {
        Intent tmp = new Intent(this, EventActivity.class);

        //Store the current user in the next activity
        tmp.putExtra("CurUser", currentUser);

        startActivity(tmp);
    }
}
