package hai2022.team.bususersapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.adapters.UserRecyclerviewAdapter;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentCoHomeBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.UserCallback;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.ui.activities.ContainerActivity;
import hai2022.team.bususersapp.utils.Constants;
import hai2022.team.bususersapp.utils.Utils;


public class CoHomeFragment extends Fragment {
    private static CoHomeFragment coHomeFragment;
    private FragmentCoHomeBinding binding;
    private ArrayList<Bus> buses1;
    private UserRecyclerviewAdapter adapter;
    private Realtime realtime;
    private ProgressDialog dialog;
    private Authentication authentication;


    public CoHomeFragment() {
        // Required empty public constructor
    }

    public static CoHomeFragment newInstance() {
        if (coHomeFragment == null) {
            coHomeFragment = new CoHomeFragment();
        }
        return coHomeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
        dialog.show();
        dialog.setTitle("Loading Data ...");
        dialog.setMessage("Loading Data ...");

        realtime = new Realtime(getContext(), new BusListiner() {
            @Override
            public void ceatebus(@NonNull Task<Void> task) {

            }

            @Override
            public void getUser(User user) {

            }

            @Override
            public void getBus(Bus bus) {

            }

            @Override
            public void getAdmins(ArrayList<User> users) {

            }

            @Override
            public void getDrivers(ArrayList<User> users) {

            }

            @Override
            public void getStudents(ArrayList<User> users) {

            }

            @Override
            public void getBuses(ArrayList<Bus> buses) {
                buses1.addAll(buses);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void getMsgs(ArrayList<Chat> chats) {

            }

            @Override
            public void updateUser(boolean state) {

            }
        });
        authentication =new Authentication();
        realtime.getBus(authentication.firebaseUser().getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        buses1 = new ArrayList<>();

        realtime.getBuses(authentication.firebaseUser().getDisplayName());
        adapter = new UserRecyclerviewAdapter(getContext(), buses1, R.layout.recyclerview_person, authentication.firebaseUser().getDisplayName(), new UserCallback() {
            @Override
            public void bus_click_listener(Bus bus, Uri uri) {
                Intent i = new Intent(getContext(), ContainerActivity.class);
                i.putExtra(Constants.FRAGMENT,Constants.DRIVER_DETAILS_FRAGMENT);
                i.putExtra("Bus", bus);
                i.putExtra("Uri", uri);
                startActivity(i);
            }

            @Override
            public void add_new_bus() {
                Intent i = new Intent(getContext(), ContainerActivity.class);
                i.putExtra(Constants.FRAGMENT, Constants.ADD_BUS_FRAGMENT);
                startActivity(i);
            }
        });

        binding.CoHomeFragmentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.CoHomeFragmentRv.setAdapter(adapter);


        return root;

    }
}