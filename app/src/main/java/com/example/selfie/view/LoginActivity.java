package com.example.selfie.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.selfie.R;
import com.example.selfie.common.GenericActivity;
import com.example.selfie.common.Utils;
import com.example.selfie.presenter.LoginPresenter;


public class LoginActivity extends GenericActivity<LoginPresenter.View, LoginPresenter> implements LoginPresenter.View {

    private EditText username;
    private EditText password;
    private Button login;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, LoginPresenter.class, this);

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().isEmpty()
                        || password.getText().toString().trim().isEmpty()) {
                    Utils.showToast(LoginActivity.this, "Please fill all the fields");
                    return;
                }

                //getOps().login(username.getText().toString(), password.getText().toString());
                startActivity(new Intent(LoginActivity.this, SelfieListActivity.class));
            }
        });
    }

    @Override
    public void error(Exception e) {
        Utils.showToast(LoginActivity.this, "Login failed. Exception: " + e.getMessage());
    }

    @Override
    public void success() {
        startActivity(new Intent(LoginActivity.this, SelfieListActivity.class));
    }
}
