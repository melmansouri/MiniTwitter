package com.mel.minitwitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mel.minitwitter.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.txtLogin)
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
    @OnClick(R.id.txtLogin)
    public void goToLoginScreen(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
