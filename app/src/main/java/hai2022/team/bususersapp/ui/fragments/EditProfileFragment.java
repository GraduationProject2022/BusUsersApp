package hai2022.team.bususersapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.models.User;

public class EditProfileFragment extends Fragment {
    private static EditProfileFragment editProfileFragment;
    private User user;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(User user) {
        if (editProfileFragment == null){
            editProfileFragment = new EditProfileFragment();
        }
        Bundle b = new Bundle();
        b.putSerializable("user",user);
        editProfileFragment.setArguments(b);
        return editProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }
}