package com.example.study_session.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.study_session.Date;
import com.example.study_session.Profile;
import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class RegisterNewUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);
    private EditText emailView,passwordView,userView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ProgressBar spinner;
    private String userSchool,userName, day, email, password;
    private TimePicker timePicker;
    private ArrayList<Date> timesAvailable;
    private boolean timeFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        emailView = findViewById(R.id.emailView);
        passwordView = findViewById(R.id.passwordView);
        spinner = findViewById(R.id.progressBar);
        Spinner schoolSpinner = findViewById(R.id.schoolDropDown);
        Spinner daySpinner = findViewById(R.id.dayDropDown);
        spinner.setVisibility(View.GONE);
        userView = findViewById(R.id.nameVIew);
        timePicker = findViewById(R.id.timePicker);
        timesAvailable = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schools_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(adapter);
        schoolSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapterDay);
        daySpinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        final Button createAccount = findViewById(R.id.createBTN);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks fields are non-null and inputs are valid
                try {
                    email = emailView.getText().toString();
                    password = passwordView.getText().toString();
                    userName = userView.getText().toString();

                    if (!isEmailValid(email)) {
                        emailView.setError(getString(R.string.invalid_email));
                    } else if (!isPasswordValid(password)) {
                        passwordView.setError(getString(R.string.invalid_password));
                    } else if (userSchool == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.empty_school), Toast.LENGTH_SHORT).show();
                    } else if (userName == null) {
                        userView.setError(getString(R.string.empty_username));
                    } else if (!timeFlag) {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_time), Toast.LENGTH_SHORT).show();
                    }else {
                        spinner.setVisibility(View.VISIBLE);
                        db = FirebaseFirestore.getInstance();
                        checkUsername();
                    }
                }
                catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Handles gathering the time data from the timePicker
        final Button addTime = findViewById(R.id.addTimeBTN);
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                String meridiem;
                if (hour == 0) {
                    hour += 12;
                    meridiem = "AM";
                } else if (hour == 12) {
                    meridiem = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    meridiem = "PM";
                } else {
                    meridiem = "AM";
                }
                Date date = new Date(day, hour, min, meridiem);
                timesAvailable.add(date);
                Log.d("Add Time", "Added available time to user");
                Toast.makeText(getApplicationContext(), getString(R.string.added_time), Toast.LENGTH_SHORT).show();
                timeFlag = true;
            }
        });
    }

    /**
     * Checks that the password is valid (it must meet OWASP password standard)
     *
     * @param password the user's password
     * @return true if the password is valid
     */
    private static boolean isPasswordValid(String password) {
        return (PASSWORD_PATTERN.matcher(password).matches());
    }

    /**
     * Checks that the email is valid (only contains email specific characters)
     *
     * @param email the user's email
     * @return true if the email is valid
     */
    private static boolean isEmailValid(String email) {
        return (EMAIL_PATTERN.matcher(email).matches());
    }

    /**
     * Checks if the given userName has already been used.
     */
    public void checkUsername(){
        db.collection("users")
                .whereEqualTo("name", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() > 0) {
                                Log.d("Username Collision", "Username already exists");
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Username already used",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("No Username Collision", "Username available");
                                new CreateAccount().execute();
                            }
                        }
                    }
                });
    }

    /**
     * Handles selecting in a spinner
     */
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Spinner spin = (Spinner)parent;
        if(spin.getId() == R.id.schoolDropDown)
        {
            userSchool = parent.getItemAtPosition(pos).toString();
        }
        if(spin.getId() == R.id.dayDropDown)
        {
            day = parent.getItemAtPosition(pos).toString();
        }
    }

    //This is only here to not make the interface call angry
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Creates the user account with the given email and password
     */
    private class CreateAccount extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                spinner.setVisibility(View.GONE);
                                Log.d("CreateUser", "createUserWithEmail:success");
                                user = mAuth.getCurrentUser();

                                new AddData().execute();
                                //Pass user information to main activity
                                Intent successIntent = new Intent();
                                successIntent.putExtra("userName", userName);
                                successIntent.putExtra("school", userSchool);
                                successIntent.putExtra("uid", user.getUid());
                                setResult(LoginActivity.SUCCESSFUL_REGISTRATION, successIntent);
                                finish();
                            } else {
                                spinner.setVisibility(View.GONE);
                                Log.w("CreateTag", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(),"Failed to create account.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            return null;
        }
    }

    /**
     * Adds the user's information to the database
     */
    private class AddData extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Profile profile = new Profile(userName,userSchool,timesAvailable);
            db.collection("users").document(user.getUid())
                    .set(profile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Adding User Info", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            spinner.setVisibility(View.GONE);
                            Log.w("Adding User Info", "Error writing document", e);
                        }
                    });
            return null;
        }
    }
}
