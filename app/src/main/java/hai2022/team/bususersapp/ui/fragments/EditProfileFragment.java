package hai2022.team.bususersapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentEditProfileBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.utils.Utils;

public class EditProfileFragment extends Fragment {
    private static EditProfileFragment editProfileFragment;
    private User user;
    private FragmentEditProfileBinding binding;
    private Realtime realtime;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(User user) {
        if (editProfileFragment == null) {
            editProfileFragment = new EditProfileFragment();
        }
        Bundle b = new Bundle();
        b.putSerializable("user", user);
        editProfileFragment.setArguments(b);
        return editProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }

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

            }

            @Override
            public void getMsgs(ArrayList<Chat> chats) {

            }

            @Override
            public void updateUser(boolean state) {
                if (state){
                    Toast.makeText(getContext(), "user updated successfully!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "user can't be updated successfully!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.ProfileFragmentEtUsername.setText(user.getUsername());
        binding.ProfileFragmentEtFullname.setText(user.getFullname());
        binding.ProfileFragmentEtPhone.setText(user.getPhone());
        binding.ProfileFragmentEtAddress.setText(user.getAddress());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.SignupBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user2 = user;
                user2.setUsername(binding.ProfileFragmentEtUsername.getText().toString());
                user2.setFullname(binding.ProfileFragmentEtFullname.getText().toString());
                user2.setPhone(binding.ProfileFragmentEtPhone.getText().toString());
                user2.setAddress(binding.ProfileFragmentEtAddress.getText().toString());
                user2.setUpdated_at( Utils.currentDate());
                realtime.updateUser(user2);
            }
        });
    }
}