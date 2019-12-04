package com.example.study_session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    private boolean changeFlag = false;
    private boolean emailFlag = false;
    private boolean schoolFlag = false;
    private String uid, email, school;
    private FirebaseUser user;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        TextView schoolView = findViewById(R.id.schoolView);
        TextView emailView = findViewById(R.id.emailView);
        Button emailBTN = findViewById(R.id.changeEmailBTN);
        Button schoolBTN = findViewById(R.id.changeNameBTN);
        final Button updateBTN = findViewById(R.id.updateBTN);
        final EditText changeInput = findViewById(R.id.changeInput);
        updateBTN.setEnabled(false);
        Intent data = getIntent();
        uid = data.getStringExtra("uid");
        email = data.getStringExtra("email");
        school = data.getStringExtra("school");
        String emailString = "Current Email: " + email;
        String schoolString = "Current School: " + school;
        emailView.setText(emailString);
        schoolView.setText(schoolString);

        changeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(TextUtils.isEmpty(changeInput.getText()))){
                        updateBTN.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(changeInput.getText())){
                    updateBTN.setEnabled(false);
                }
                else {
                    updateBTN.setEnabled(true);
                }
            }
        });

        emailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFlag = true;
                schoolFlag = false;
            }
        });

        schoolBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFlag = false;
                schoolFlag = true;
            }
        });


    }

    public void backToProfile(View view){
        if (changeFlag){
            Intent data = new Intent();
            data.putExtra("school", school);
            data.putExtra("email", email);
            setResult(ProfileActivity.UPDATE_PROFILE,data);
            finish();
        }
        else {
            setResult(ProfileActivity.VIEW_PROFILE);
            finish();
        }
    }

    public void updateInfo(View view){
        final EditText changeInput = findViewById(R.id.changeInput);
        if (emailFlag){
            updateEmail(changeInput);
        }
        else if (schoolFlag){

        }
        else {
            Toast.makeText(getApplicationContext(), "Please select a field to update", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEmail(EditText input){
        final String newEmail = input.getText().toString();
        if (isEmailValid(newEmail)){
            user.updateEmail(newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Update Email", "User email address updated.");
                                email = newEmail;
                                Toast.makeText(getApplicationContext(), "Changed email to "+ newEmail, Toast.LENGTH_SHORT).show();
                                changeFlag = true;
                            }
                        }
                    });
            TextView emailView = findViewById(R.id.emailView);
            String emailString = "Current Email: " + email;
            emailView.setText(emailString);
        }
        else {
            input.setError("Not a valid email");
        }
    }

    public void updateUsername(EditText input){

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

}
