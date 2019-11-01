package com.example.study_session.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterNewUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);
    private EditText emailView;
    private EditText passwordView;
    private FirebaseAuth mAuth;
    //private DatabaseReference mDatabase;
    private ProgressBar spinner;
    private EditText userView;
    private Spinner schoolSpinner;
    private String userSchool;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        emailView = findViewById(R.id.emailView);
        passwordView = findViewById(R.id.passwordView);
        spinner = findViewById(R.id.progressBar);
        schoolSpinner = findViewById(R.id.schoolDropDown);
        spinner.setVisibility(View.GONE);
        userView = findViewById(R.id.nameVIew);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schools_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        schoolSpinner.setAdapter(adapter);
        schoolSpinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        final Button createAccount = findViewById(R.id.createBTN);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String email = emailView.getText().toString();
                    String password = passwordView.getText().toString();
                    userName = userView.getText().toString();

                    if (!isEmailValid(email)) {
                        emailView.setError(getString(R.string.invalid_email));
                    } else if (!isPasswordValid(password)) {
                        passwordView.setError(getString(R.string.invalid_password));
                    } else if (userSchool == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.empty_school), Toast.LENGTH_SHORT).show();
                    } else if (userName == null) {
                        userView.setError(getString(R.string.empty_username));
                    } else {
                        spinner.setVisibility(View.VISIBLE);
                        createAccount(email, password);
                    }
                }
                catch (NullPointerException e){
                    System.out.println("OOPS");
                }
            }
        });


    }

    public void createAccount(String email, String password){
        //TODO add logic for validating user email

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            spinner.setVisibility(View.GONE);
                            Log.d("CreateUser", "createUserWithEmail:success");

                            //mDatabase = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = mAuth.getCurrentUser();


                            //updateUI(user);
                        } else {
                            spinner.setVisibility(View.GONE);
                            Log.w("CreateTag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Failed to create account.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private static boolean isPasswordValid(String password) {
        return (PASSWORD_PATTERN.matcher(password).matches());
    }

    private static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return (email.contains("@") && email.contains(".com"));
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        userSchool = parent.getItemAtPosition(pos).toString();
    }

    //This is only here to not make the interface call angry
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
