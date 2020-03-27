package id.putraprima.retrofit.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private RegisterRequest registerRequest;
    EditText namaInput, emailInput, passwordInput, conPassInput;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        namaInput = findViewById(R.id.eName);
        emailInput = findViewById(R.id.eEmail);
        passwordInput=findViewById(R.id.ePassword);
        conPassInput = findViewById(R.id.eConfirm);
        btnRegister = findViewById(R.id.btnSubmit);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = namaInput.getText().toString();
                String email= emailInput.getText().toString();
                String pass = passwordInput.getText().toString();
                String pass_con = conPassInput.getText().toString();

                registerRequest = new RegisterRequest(name,email,pass,pass_con);

                boolean validate = !name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !pass_con.isEmpty() && pass.equals(pass_con) && pass.length()>=8;
                if(validate){
                    register();                }
                else{
                    Toast.makeText(RegisterActivity.this, "isi coy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<RegisterResponse>> call = service.doRegister(registerRequest);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if(response.code() == 302){
                    Toast.makeText(RegisterActivity.this, "Failed, Duplicate User", Toast.LENGTH_SHORT).show();
                }
                else if(response.code()==201){
                    Toast.makeText(RegisterActivity.this, response.body().getData().getName(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, response.body().getData().getEmail(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Failed, Duplicate User", Toast.LENGTH_SHORT).show();

            }
        });
        }

//    public void handlerRegisterProcess(View view) {
//        String name = namaInput.getText().toString();
//        String email= emailInput.getText().toString();
//        String pass = passwordInput.getText().toString();
//        String pass_con = conPassInput.getText().toString();
//        registerRequest = new RegisterRequest(name, email, pass, pass_con);
//
//        boolean check;
//        if (name.equals("")) {
//            Toast.makeText(this, "Name is Empty!", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if(email.equals("")) {
//            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if (pass.equals("")) {
//            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if (pass.equals("")) {
//            Toast.makeText(this, "Password Confirmation is Empty!", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if (pass.length() < 8) {
//            Toast.makeText(this, "Password limit 8", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if (!pass_con.equals(pass)) {
//            Toast.makeText(this, "Confirm Password not Same!", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            Toast.makeText(this, "Email Not Valid", Toast.LENGTH_SHORT).show();
//            check = false;
//        } else {
//            check = true;
//        }
//
//        if (check == true) {
//            register();
//        }
//    }
}