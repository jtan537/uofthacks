package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fall2018.csc2017.GameCentre.UserInterfaceElements.EventActivity;
import fall2018.csc2017.GameCentre.R;



/**
 * The initial activity for the game centre.
 */
public class LaunchCentre extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "LaunchCentre";


    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private boolean goodToGo = false;

    private String userEmail;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    /**
     * The onCreate for this activity
     *
     * @param savedInstanceState Represents the current activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_launcher);

        // Views
        mStatusTextView = findViewById(R.id.txtStatus);
        mDetailTextView = findViewById(R.id.txtDetail);
        mEmailField = findViewById(R.id.emailText);
        mPasswordField = findViewById(R.id.passwordText);

        // Buttons
        findViewById(R.id.enterButton).setOnClickListener(this);
        findViewById(R.id.buttonCreate).setOnClickListener(this);
        findViewById(R.id.buttonSignOut).setOnClickListener(this);
//        findViewById(R.id.buttonVerify).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        userEmail = email;
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goodToGo = true;
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            goodToGo = false;
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LaunchCentre.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        userEmail = email;
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goodToGo = true;
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            goodToGo = false;
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LaunchCentre.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
//            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

//            findViewById(R.id.buttonVerify).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.buttonCreate) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            if (goodToGo){
                switchToEvents();
            }

        } else if (i == R.id.enterButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            if (goodToGo){
                switchToEvents();
            }

        } else if (i == R.id.buttonSignOut) {
            signOut();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        signOut();
    }


    /**
     * Switch to the Game selector view to select what game to play.
     */
    private void switchToEvents() {
        Intent tmp = new Intent(this, EventActivity.class);

        //Store the current user in the next activity
        //Store the current user in the next activity
        tmp.putExtra("userEmail", userEmail);

//        Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();

        startActivity(tmp);
    }
}
