package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword, edtPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConfirm = findViewById(R.id.edtPasswordConfirm);

    }

    private void moveLogin() {
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void handleRegister(View view) {
        String name = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String password_confirmation = edtPasswordConfirm.getText().toString();

        RegisterRequest register = new RegisterRequest(name, email, password, password_confirmation);
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterResponse> call =  service.doRegister(register);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()){
                    RegisterResponse respon = response.body();
                    Snackbar.make(view, "succesfully register " + respon.getEmail(), Snackbar.LENGTH_SHORT).show();
                    moveLogin();
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (name.length() == 0){
                        int i = 0;
                        while (i < error.getError().getName().size()){
                            Snackbar.make(view, error.getError().getName().get(i), Snackbar.LENGTH_SHORT).show();
                            i++;
                        }
                    }else if (error.getError().getEmail() != null){
                        int i = 0;
                        while (i < error.getError().getEmail().size()){
                            Snackbar.make(view, error.getError().getEmail().get(i), Snackbar.LENGTH_SHORT).show();
                            i++;
                        }
                    }else{
                        int i = 0;
                        while (i < error.getError().getPassword().size()){
                            Snackbar.make(view, error.getError().getPassword().get(i), Snackbar.LENGTH_SHORT).show();
                            i++;
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Snackbar.make(view, "failed register account", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

//        if(validate == true){
//
//        }else{
//            Toast.makeText(RegisterActivity.this, "fill all data!", Toast.LENGTH_SHORT).show();
//        }

}

