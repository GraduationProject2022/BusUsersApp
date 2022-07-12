package hai2022.team.bususersapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.interfaces.UserCallback;
import hai2022.team.bususersapp.models.Bus;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Bus> buses;
    private CloudStorage storage;
    private UserCallback callback;


    public BusAdapter(Context context, ArrayList<Bus> buses) {
        this.context = context;
        this.buses = buses;
    }

    public BusAdapter(Context context, ArrayList<Bus> buses, UserCallback callback) {
        this.context = context;
        this.buses = buses;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.bus_click_listener(buses.get(position), null);
            }
        });
        storage = new CloudStorage(new StorageListener() {
            @Override
            public void onDownloadImageListener(Uri uri) {
                Log.d("UriImages", uri.getPath());
                Glide.with(context).load(uri).placeholder(R.drawable.no_pictures).into(holder.iv_bus);
            }

            @Override
            public void onUploadImageListener(boolean status) {

            }
        });
        if (!buses.get(position).getImgpath().equals("")) {
            storage.download(buses.get(position).getImgpath());
        } else {
            Glide.with(context).load("").placeholder(R.drawable.no_pictures).into(holder.iv_bus);
        }
        holder.tv_name.setText(buses.get(position).getName());
        holder.tv_location.setText(buses.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_bus, iv_btn;
        TextView tv_name, tv_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_btn = itemView.findViewById(R.id.busItem_iv_btn);
            iv_bus = itemView.findViewById(R.id.busItem_iv);
            tv_name = itemView.findViewById(R.id.busItem_tv_name);
            tv_location = itemView.findViewById(R.id.busItem_tv_location);
        }
    }
}
