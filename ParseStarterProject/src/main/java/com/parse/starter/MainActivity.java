/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{
    EditText username;
    EditText password;
    boolean SignUpMode = true;
    TextView textView;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), LogActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            onClick(view);
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login){
            Button  button = (Button)findViewById(R.id.button);
            if(SignUpMode) {
                SignUpMode = false;
                button.setText("Login");
                textView.setText("Sign Up");
            } else {
                SignUpMode = true;
                button.setText("Sign Up");
                textView.setText("Or, Login");

            }
        } else if (view.getId() == R.id.Logon || view.getId() == R.id.backGround) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
    public void Click(final View view) {


        if(username.getText().toString().matches("") || password.getText().toString().matches("")) {
            Toast.makeText(this, "Missing username or password", Toast.LENGTH_SHORT).show();
        } else {
            if (SignUpMode) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(username.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                //
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Login !", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Weibo");

        textView = (TextView) findViewById(R.id.login);
        textView.setOnClickListener(this);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        ImageView logo = (ImageView) findViewById(R.id.Logon);
        RelativeLayout backGround =(RelativeLayout) findViewById(R.id.backGround);
        logo.setOnClickListener(this);
        backGround.setOnClickListener(this);
        password.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }



}