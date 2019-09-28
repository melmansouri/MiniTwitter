package com.mel.minitwitter.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.retrofit.MiniTwitterClient;
import com.mel.minitwitter.retrofit.MiniTwitterService;
import com.mel.minitwitter.retrofit.request.RequestLogin;
import com.mel.minitwitter.retrofit.response.ResponseAuth;

import org.w3c.dom.Text;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtSignUp)
    TextView txtSignUp;
    @BindView(R.id.edtEmail)
    TextInputEditText edtEmail;
    @BindView(R.id.edtPwd)
    TextInputEditText edtPwd;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPwd)
    TextInputLayout tilPwd;
    @BindView(R.id.rootLogin)
    ConstraintLayout rootLogin;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        retrofitInit();

    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    @OnTextChanged({R.id.edtEmail,R.id.edtPwd})
    public void onTextChangedEmail(CharSequence text){
        View view=getCurrentFocus(); //Por ahora no es posible pasar view como parametro
        switch (view.getId()){
            case R.id.edtEmail:
                if (!TextUtils.isEmpty(text.toString().trim())){
                    tilEmail.setError(null);
                }
                break;
            case R.id.edtPwd:
                if (!TextUtils.isEmpty(text.toString().trim())){
                    tilPwd.setError(null);
                }
                break;
        }

    }

    /*@OnTextChanged(R.id.edtPwd)
    public void onTextChangedPwd(CharSequence text){
        switch (view.getId()){
            case R.id.edtEmail:
                if (!TextUtils.isEmpty(text.toString().trim())){
                    tilEmail.setError(null);
                }
                break;
            case R.id.edtPwd:
                if (!TextUtils.isEmpty(text.toString().trim())){
                    tilEmail.setError(null);
                }
                break;
        }

    }*/

    @OnClick(R.id.btnLogin)
    public void login() {
        String email = edtEmail.getText().toString();
        String pwd = edtPwd.getText().toString();
        boolean isOkData = true;
        if (email.isEmpty()) {
            tilEmail.setError("Email requerido");
            isOkData=false;
        }
        if (pwd.isEmpty()) {
            tilPwd.setError("Contraseña requerido");
            isOkData=false;
        }
        if (isOkData) {
            RequestLogin requestLogin = new RequestLogin(email.trim(), pwd.trim());
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()){
                        Snackbar.make(rootLogin,"Sesión iniciada correctamente",Snackbar.LENGTH_SHORT).show();
                        saveDataResponseLoginSharedPreference(response.body());
                        Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
                        startActivity(intent);
                        //Destruimos la activity para que no se vuelva a la pantalla de login
                        finish();
                    }else{
                        Snackbar.make(rootLogin,"Algo fue mal, revise sus datos de acceso",Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Snackbar.make(rootLogin,"Problemas de conexión. Intentelo de nuevo",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.txtSignUp)
    public void goToSignUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private void saveDataResponseLoginSharedPreference(ResponseAuth responseAuth){
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_TOKEN,responseAuth.getToken());
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_USERNAME,responseAuth.getUsername());
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_EMAIL,responseAuth.getEmail());
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_PHOTO_URL,responseAuth.getPhotoUrl());
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_CREATED,responseAuth.getCreated());
        SharedPreferenceManager.setSomeStringValue(Constantes.PREF_ROLE,responseAuth.getRole());
        SharedPreferenceManager.setSomeBooleanValue(Constantes.PREF_ACTIVE,responseAuth.isActive());

    }
}
