package com.mel.minitwitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mel.minitwitter.R;
import com.mel.minitwitter.retrofit.MiniTwitterClient;
import com.mel.minitwitter.retrofit.MiniTwitterService;
import com.mel.minitwitter.retrofit.request.RequestSignup;
import com.mel.minitwitter.retrofit.response.ResponseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.txtLogin)
    TextView txtLogin;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.edtPwd)
    TextInputEditText edtPwd;
    @BindView(R.id.tilPwd)
    TextInputLayout tilPwd;
    @BindView(R.id.edtUserName)
    TextInputEditText edtUserName;
    @BindView(R.id.tilUserName)
    TextInputLayout tilUserName;
    @BindView(R.id.rootSignup)
    ConstraintLayout rootSignup;
    private MiniTwitterService miniTwitterService;
    private MiniTwitterClient miniTwitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initRetrofit();
    }

    private void initRetrofit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @OnTextChanged(value = {R.id.edtUserName, R.id.edtEmail, R.id.edtPwd})
    public void onTextChanged(CharSequence text) {
        View view=getCurrentFocus(); //Por ahora no es posible pasar view como parametro
        switch (view.getId()) {
            case R.id.edtUserName:
                if (!TextUtils.isEmpty(text.toString().trim())) {
                    tilUserName.setError(null);
                }
                break;
            case R.id.edtEmail:
                if (!TextUtils.isEmpty(text.toString().trim())) {
                    tilEmail.setError(null);
                }
                break;
            case R.id.edtPwd:
                if (!TextUtils.isEmpty(text.toString().trim())) {
                    tilPwd.setError(null);
                }
                break;
        }
    }

    @OnClick(R.id.btnSignup)
    public void signup() {
        String userName = edtUserName.getText().toString();
        String email = edtEmail.getText().toString();
        String pwd = edtPwd.getText().toString();
        boolean isAllOk = true;

        if (TextUtils.isEmpty(userName)) {
            tilUserName.setError("Nombre de usuario requerido");
            isAllOk = false;
        }
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email requerido");
            isAllOk = false;
        }
        if (TextUtils.isEmpty(pwd)) {
            tilPwd.setError("Contraseña requerida");
            isAllOk = false;
        }

        if (isAllOk) {
            RequestSignup requestSignup = new RequestSignup(userName.trim(), email.trim(), pwd.trim());
            Call<ResponseAuth> call = miniTwitterService.doSignup(requestSignup);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()){
                        Intent intent = new Intent(SignUpActivity.this,DashboardActivity.class);
                        startActivity(intent);
                        //Destruimos la activity para que no se vuelva a la pantalla de login
                        finish();
                    }else{
                        Snackbar.make(rootSignup,"Algo fue mal, revise sus datos de acceso",Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Snackbar.make(rootSignup,"Problemas de conexión. Intentelo de nuevo",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.txtLogin)
    public void goToLoginScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
