package hai2022.team.bususersapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.interfaces.UserCallback;
import hai2022.team.bususersapp.models.Bus;

public class UserRecyclerviewAdapter extends RecyclerView.Adapter<UserRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Bus> buses;
    private int res;
    private UserCallback callback;
    private CloudStorage storage;
    private String UserType;
    private Uri uri1;

    public UserRecyclerviewAdapter(Context context, ArrayList<Bus> buses, int res,String UserType, UserCallback callback) {
        this.context = context;
        this.buses = buses;
        this.res = res;
        this.callback = callback;
        this.UserType = UserType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        uri1 = null;

        if (position == 0 && !UserType.equals("student")) {
            holder.iv_user.setImageResource(R.drawable.add_user);
            holder.tv_username.setText("Add New Bus");
            holder.tv_rate.setVisibility(View.GONE);
            holder.iv_user.getLayoutParams().height = 200;
            holder.iv_user.getLayoutParams().width = 200;
            buses.add(buses.get(position));

        } else {
            storage = new CloudStorage(new StorageListener() {
                @Override
                public void onDownloadImageListener(Uri uri) {
                    Glide.with(context).load(uri).placeholder(R.drawable.no_pictures).into(holder.iv_user);
                    uri1 = uri;
//                    holder.iv_user.setImageResource(buses.get(position).getImg());
                }

                @Override
                public void onUploadImageListener(boolean status) {

                }
            });
            if (!buses.get(position).getImgpath().equals(""))
            storage.download(buses.get(position).getImgpath());
            if (buses.get(position).getImg() == 0) {
                holder.iv_user.setImageResource(R.drawable.no_pictures);
            }
            holder.tv_username.setText(buses.get(position).getLocation());
            holder.tv_rate.setText(buses.get(position).getRate() + "");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0 && !UserType.equals("student")) {
                    callback.add_new_bus();
                } else {
                    callback.bus_click_listener(buses.get(position), uri1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_user;
        TextView tv_username;
        TextView tv_rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            iv_user = itemView.findViewById(R.id.recyclerview_person_iv);
            tv_username = itemView.findViewById(R.id.recyclerview_person_tv);
            tv_rate = itemView.findViewById(R.id.recyclerview_person_tv_rate);
        }

    }
}
