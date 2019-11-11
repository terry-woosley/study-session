package com.example.study_session.ui.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;

/**
 * Performs tha main logic for the Login activity
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);
    public static final int REGISTER_USER = 101;
    public static final int SUCCESSFUL_REGISTRATION = 102;
    public static final int LOGIN_SUCCESS = 103;
    public static final int LOGIN_ACTIVITY = 104;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText userEmailText, passwordText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private String email,password;
    private DocumentSnapshot document;
    private CheckBox checkBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailText = findViewById(R.id.emailView);
        passwordText = findViewById(R.id.passwordView);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        loginButton = findViewById(R.id.login);
        checkBox = findViewById(R.id.rememberCheck);

//        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
//        if (sp.contains("isChecked")){
//            if (sp.getBoolean("isChecked", false))
//            restoreSharedPreferences(findViewById(R.id.container));
//            logUserIn(email, password);
//        }

        userEmailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(TextUtils.isEmpty(passwordText.getText()))){
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(passwordText.getText()) || TextUtils.isEmpty(userEmailText.getText()) ){
                    loginButton.setEnabled(false);
                }
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(TextUtils.isEmpty(userEmailText.getText()))){
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(passwordText.getText()) || TextUtils.isEmpty(userEmailText.getText()) ){
                    loginButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * Starts the RegisterNewUser activity
     *
     * @param view the screen View
     */
    public void registerNewUser(View view){
        Intent registerIntent = new Intent(this, RegisterNewUser.class);
        startActivityForResult(registerIntent,REGISTER_USER);
    }

    /**
     * Destroys login and starts **** on successful user registration,
     * and displays error message on failed registration
     *
     * @param requestCode the code the activity was started with
     * @param resultCode the result of the activity
     * @param data the intent from the activity
     */
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == SUCCESSFUL_REGISTRATION){
            String welcome = getString(R.string.welcome) + data.getStringExtra("userName");
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            setResult(SUCCESSFUL_REGISTRATION, data);
            finish();
        }
        else {
            showRegistrationFail("Failed to create account");
        }
    }

    public void signIn(View view){

        try {
            email = userEmailText.getText().toString();
        }
        catch (NullPointerException e){
            userEmailText.setError("Please enter an email address");
            return;
        }
        try {
            password = passwordText.getText().toString();
        }
        catch (NullPointerException e){
            passwordText.setError("Please enter a password");
            return;
        }

        if (!isEmailValid(email)) {
            userEmailText.setError(getString(R.string.invalid_email));
        } else if (!isPasswordValid(password)) {
            passwordText.setError(getString(R.string.invalid_password));
        }
        else {
            logUserIn(email, password);
        }
    }

    /**
     * Displays a welcome message to the user upon successful login
     *
     * @param data with the user's login information
     */
    private void updateUiWithUser(Intent data) {
        String welcome = getString(R.string.welcome) + data.getStringExtra("userName");
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    /**
     * Prints a pop-up error message to the screen when Login fails
     *
     */
    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), "Authentication failed.",
                Toast.LENGTH_SHORT).show();    }

    /**
     * Prints a pop-up error message to the screen when registration failes
     *
     * @param message the error message to display to user
     */
    private void showRegistrationFail(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private static boolean isPasswordValid(String password) {
        return (PASSWORD_PATTERN.matcher(password).matches());
    }

    private static boolean isEmailValid(String email) {
        return (EMAIL_PATTERN.matcher(email).matches());
    }

    public void restoreSharedPreferences(View v){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        email = sp.getString("userEmail", "null");
        password = sp.getString("userPassword", "null");
    }

    public void saveSharedPreferences(View v){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("userEmail",email);
        edit.putString("userPassword", password);
        edit.putBoolean("isChecked", true);
        edit.apply();
    }

    public void logUserIn(String email, String password){
        loadingProgressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d("SignIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        document = task.getResult();
                                        if (document.exists()) {
                                            Intent userLogin = new Intent();
                                            userLogin.putExtra("userName", document.get("name").toString());
                                            updateUiWithUser(userLogin);
                                            setResult(LOGIN_SUCCESS);
                                            if (checkBox.isChecked()){
                                                saveSharedPreferences(findViewById(R.id.container));
                                            }
                                            finish();
                                            Log.d("Get user data", "DocumentSnapshot data: " + document.getData());
                                        } else {
                                            Log.d("Get user data", "No such document");
                                        }
                                    } else {
                                        Log.d("Get user data", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.w("SignIn", "signInWithEmail:failure", task.getException());
                            showLoginFailed();
                        }
                    }
                });
    }
}
