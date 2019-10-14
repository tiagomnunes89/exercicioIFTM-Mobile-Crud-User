package br.edu.iftm.cruduser.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.iftm.cruduser.R;
import br.edu.iftm.cruduser.activities.EditUserActivity;
import br.edu.iftm.cruduser.dto.User;
import br.edu.iftm.cruduser.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<User> userList;

    public Context getContext() {
        return context;
    }

    private Integer recentlyDeletedItemPosition;
    private User recentlyDeletedItem;

    public UserAdapter(Context context, List<User> userList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String name = userList.get(position).getName();
        holder.textViewName.setText(name);
    }

    @Override
    public int getItemCount() {
        if(userList != null){
            return userList.size();
        } else {
            return 0;
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        final UserAdapter userAdapter;
        public final TextView textViewName;

        public UserViewHolder(@NonNull View itemView, UserAdapter userAdapter) {
            super(itemView);

            this.userAdapter = userAdapter;
            textViewName = itemView.findViewById(R.id.tv_user_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userList.get(getLayoutPosition());
                    String name = user.getName();
                    String email = user.getEmail();
                    String phone = user.getPhone();
                    Integer id = user.getId();
                    Intent intent = new Intent(context, EditUserActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("userId", id);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void deleteTask(int position) {
        recentlyDeletedItem = userList.get(position);
        recentlyDeletedItemPosition = position;
        userList.remove(position);
        notifyItemRemoved(position);
        showUndoDialog();
    }

    private void showUndoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseja apagar este usuário?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callAPIServiceDelete();
                    }
                });

        builder.setNegativeButton(
                "Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        undoDelete();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callAPIServiceDelete() {
        SharedPreferences sp = context.getSharedPreferences("dados", 0);
        String token = sp.getString("token", null);

        RetrofitService.getServico().deleteUser(recentlyDeletedItem.getId(), "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Usuário foi apagado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Falha ao apagar o usuário: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void undoDelete() {
        userList.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }
}
