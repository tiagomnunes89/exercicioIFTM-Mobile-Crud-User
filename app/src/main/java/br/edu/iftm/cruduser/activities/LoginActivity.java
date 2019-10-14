package br.edu.iftm.cruduser.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.iftm.cruduser.R;
import br.edu.iftm.cruduser.dto.Login;
import br.edu.iftm.cruduser.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void logar(View view) {

        String email = ((EditText) findViewById(R.id.editText_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_password)).getText().toString();

        Login login = new Login(email, password);

        RetrofitService.getServico().login(login).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Toast.makeText(LoginActivity.this, "Usuário logado com sucesso", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("dados", 0);
                SharedPreferences.Editor editor = sp.edit();
                if (response.body() != null)
                    editor.putString("token", response.body().getToken());
                editor.apply();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Falha ao validar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
