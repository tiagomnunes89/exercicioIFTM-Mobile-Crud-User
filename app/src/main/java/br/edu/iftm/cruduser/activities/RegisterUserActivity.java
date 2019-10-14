package br.edu.iftm.cruduser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.iftm.cruduser.R;
import br.edu.iftm.cruduser.dto.User;
import br.edu.iftm.cruduser.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_insert);
    }

    public void salvar(View view) {
        String name = ((EditText) findViewById(R.id.et_nome)).getText().toString();
        String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.et_phone)).getText().toString();
        String password = ((EditText) findViewById(R.id.et_password)).getText().toString();
        User user = new User(name, email, phone, password);

        RetrofitService.getServico().insertUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, "Usuário - "
                            + response.body().getName() + " foi cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterUserActivity.this, "Falha ao salvar o usuário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
