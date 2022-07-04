package hai2022.team.bususersapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import hai2022.team.bususersapp.adapters.BusAdapter;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentStudentHomeBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.UserCallback;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.ui.activities.ContainerActivity;
import hai2022.team.bususersapp.utils.Constants;

public class StudentHomeFragment extends Fragment {
    private static StudentHomeFragment homeFragment;
    private BusAdapter adapter;
    private ArrayList<Bus>buses1;
    private Realtime realtime;
    private FragmentStudentHomeBinding binding;
    private Authentication authentication;
    private ProgressDialog dialog;

    public StudentHomeFragment() {
        // Required empty public constructor
    }


    public static StudentHomeFragment newInstance() {
        if (homeFragment == null){
            homeFragment = new StudentHomeFragment();
        }
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
        dialog.show();
        dialog.setTitle("Loading Data ...");
        dialog.setMessage("Loading Data ...");

        authentication =new Authentication();
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

        buses1 = new ArrayList<>();
        realtime.getBuses(authentication.firebaseUser().getDisplayName());
        adapter = new BusAdapter(getContext(), buses1, new UserCallback() {
            @Override
            public void bus_click_listener(Bus bus, Uri uri) {
                Intent i = new Intent(getContext(), ContainerActivity.class);
                i.putExtra(Constants.FRAGMENT,Constants.BUS_DETAILS_FRAGMENT);
                i.putExtra("Bus", bus);
                startActivity(i);

            }

            @Override
            public void add_new_bus() {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.StudentHomeFragmentRv.setAdapter(adapter);
        binding.StudentHomeFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));


        binding.StudentHomeFragmentSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return root;
    }
}