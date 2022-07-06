package hai2022.team.bususersapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentProfileBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.ui.activities.ContainerActivity;
import hai2022.team.bususersapp.ui.activities.SplashActivity;
import hai2022.team.bususersapp.utils.Constants;


public class ProfileFragment extends Fragment {

    private static ProfileFragment profileFragment;
    private Authentication authentication;
    private FragmentProfileBinding binding;
    private Realtime realtime;
    private CloudStorage storage;
    private User user1;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
        realtime = new Realtime(getContext(), new BusListiner() {
            @Override
            public void ceatebus(@NonNull Task<Void> task) {

            }

            @Override
            public void getUser(User user) {
                if (user.getImgPath().equals(""))
                    Glide.with(getContext()).load("").placeholder(R.drawable.profile).into(binding.SettingsFragmentIvProfile);
                else
                    storage.download(user.getImgPath());


                binding.SettingsFragmentTvMembersince.setText(user.getCreated_at());
                binding.SettingsFragmentTvUsername.setText(user.getUsername());
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

            }

            @Override
            public void getMsgs(ArrayList<Chat> chats) {

            }

            @Override
            public void updateUser(boolean state) {

            }
        });

        storage = new CloudStorage(new StorageListener() {
            @Override
            public void onDownloadImageListener(Uri uri) {
                Glide.with(getContext()).load(uri).placeholder(R.drawable.profile).into(binding.SettingsFragmentIvProfile);
            }

            @Override
            public void onUploadImageListener(boolean status) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        realtime.getUser(authentication.firebaseUser().getDisplayName(), authentication.firebaseUser().getUid());


        binding.SettingsFragmentBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication.logout();
                getActivity().finishAffinity();
                startActivity(new Intent(getContext(), SplashActivity.class));
            }
        });

        binding.SettingsFragmentBtnEditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ContainerActivity.class);
                i.putExtra(Constants.FRAGMENT, Constants.EDIT_PROFILE_FRAGMENT);
                startActivity(i);
            }
        });

        return root;
    }
}