package br.edu.iftm.cruduser.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.iftm.cruduser.R;
import br.edu.iftm.cruduser.dto.User;
import br.edu.iftm.cruduser.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private Button button;
    private User currentUser;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_insert);
        findViewByIds();
        Bundle bundle = getIntent().getExtras();
        String name = (String) bundle.get("name");
        String email = (String) bundle.get("email");
        String phone = (String) bundle.get("phone");
        userId = (Integer) bundle.get("userId");
        currentUser = new User(name, email, phone);
        fillData(name, email, phone);
        updateButtonText();
        hidePasswordField();
    }

    private void hidePasswordField() {
        EditText editTextPassword = findViewById(R.id.et_password);
        editTextPassword.setVisibility(View.INVISIBLE);
    }

    private void findViewByIds() {
        editTextName = findViewById(R.id.et_nome);
        editTextEmail = findViewById(R.id.et_email);
        editTextPhone = findViewById(R.id.et_phone);
        button = findViewById(R.id.bt_save);
    }

    private void fillData(String name, String email, String phone) {
        editTextName.setText(name);
        editTextEmail.setText(email);
        editTextPhone.setText(phone);
    }

    private void updateButtonText() {
        button.setText("SALVAR ALTERAÇÕES");
    }

    public void salvar(View view) {

        if (!currentUser.getName().contentEquals(editTextName.getText()) ||
                !currentUser.getEmail().contentEquals(editTextEmail.getText()) ||
                !currentUser.getPhone().contentEquals(editTextPhone.getText())) {
            User user = new User(editTextName.getText().toString(),
                    editTextEmail.getText().toString(), editTextPhone.getText().toString());

            SharedPreferences sp = getSharedPreferences("dados", 0);
            String token = sp.getString("token", null);

            RetrofitService.getServico().updateUser(user, userId, "Bearer " + token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(EditUserActivity.this, "Usuário foi alterado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditUserActivity.this,ListUsersActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(EditUserActivity.this, "Falha ao alterar o usuário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
