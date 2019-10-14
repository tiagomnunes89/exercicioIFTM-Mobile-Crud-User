package br.edu.iftm.cruduser.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.iftm.cruduser.R;
import br.edu.iftm.cruduser.adapter.SwipeToDeleteCallback;
import br.edu.iftm.cruduser.adapter.UserAdapter;
import br.edu.iftm.cruduser.dto.User;
import br.edu.iftm.cruduser.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        searchData();
    }

    private void searchData() {
        SharedPreferences sp = getSharedPreferences("dados", 0);
        String token = sp.getString("token", null);

        RetrofitService.getServico().getAllUsers("Bearer " + token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    List<User> userList = response.body();
                    setUpRecyclerView(userList);
                }
            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("ListaUsuários", "OnFailure" + t.getMessage());
            }
        });
    }

    private void setUpRecyclerView(List<User> userList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_list_users);
        UserAdapter userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(userAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
