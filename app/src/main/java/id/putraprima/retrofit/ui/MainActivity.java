package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    private TextView appName, appVersion;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtEmail = findViewById(R.id.mainEdtEmail);
        edtPassword = findViewById(R.id.mainEdtPassword);
        setupLayout();
        checkAppVersion();

    }

    private void setupLayout(){
        appName = findViewById(R.id.mainTxtAppName);
        appVersion = findViewById(R.id.mainTxtAppVersion);
    }

    private void checkAppVersion(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>(){

            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                AppVersion app = response.body();
                appName.setText(app.getApp());
                appVersion.setText(app.getVersion());
            }
            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed to connect server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String token;
    public void handleLogin(View view){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        LoginRequest login = new LoginRequest(email,password);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse respon = response.body();
                if(response.isSuccessful()){
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(token, respon.getToken());
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (email.length() == 0 && password.length() == 0) {
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0) + "and" + error.getError().getPassword().get(0),  Toast.LENGTH_SHORT).show();

                    }
                    else if(email.length() == 0){
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0),Toast.LENGTH_SHORT).show();
                    }else if(password.length() == 0) {
                        Toast.makeText(MainActivity.this, error.getError().getPassword().get(0),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "login is not correct", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveRegisterActivity(View view) {
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    public void handleResep(View view) {
        Intent intent = new Intent(this, ResepActivity.class);
        startActivity(intent);
    }
}
