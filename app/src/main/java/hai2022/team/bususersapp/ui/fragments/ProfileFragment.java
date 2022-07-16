package hai2022.team.bususersapp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private Uri profile_uri;

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
                user1 = user;

                try {
                    if (user.getImgPath().equals(""))
                        Glide.with(getContext()).load("").placeholder(R.drawable.profile).into(binding.SettingsFragmentIvProfile);
                    else
                        storage.download(user.getImgPath());
                    Toast.makeText(getContext(), "y: " + user.getImgPath(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }

                binding.SettingsFragmentTvMembersince.setText(user.getCreated_at());
                binding.SettingsFragmentTvUsername.setText(user.getUsername());
                binding.SettingsFragmentTvEmail.setText(user.getEmail());

            }

            @Override
            public void getBus(Bus bus) {
                binding.SettingsFragmentTvUsername.setText(bus.getDriverName());
                binding.SettingsFragmentTvEmail.setText(bus.getEmail());

                storage.download(bus.getImgpath());
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
                try {
                    Glide.with(getContext()).load(uri).placeholder(R.drawable.profile).into(binding.SettingsFragmentIvProfile);
                } catch (Exception e) {

                }
            }

            @Override
            public void onUploadImageListener(boolean status) {
                if (status){
//                    binding.SettingsFragmentIvProfile.setImageURI(profile_uri);
                    String Imagepath = profile_uri != null ? "users/" + user1.getType() + "/" + user1.getUsername().replace(" ", "") + profile_uri.getLastPathSegment() : user1.getImgPath();
                    user1.setImgPath(Imagepath);
                    realtime.updateUser(user1);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();


        if (authentication.firebaseUser().getDisplayName().equals("driver") || authentication.firebaseUser().getDisplayName().equals("student")) {
            realtime.getUser(authentication.firebaseUser().getDisplayName(), authentication.firebaseUser().getUid());
        } else {
            realtime.getBus(authentication.firebaseUser().getUid());
            binding.SettingsFragmentBtnEditprofile.setVisibility(View.GONE);
            binding.SettingsFragmentTvMembersince.setVisibility(View.GONE);
            binding.SettingsFragmentTvMember.setVisibility(View.GONE);
            binding.ProfileFragmentIvEdit.setVisibility(View.GONE);
        }
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
                i.putExtra("user", user1);
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        editEmailAndPassword();
        openGallery();
    }

    private void editEmailAndPassword() {
        binding.SettingsFragmentBtnEditemailandpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContainerActivity.class);
//                intent.putExtra("User", user);
                intent.putExtra(Constants.FRAGMENT, Constants.EDIT_EMAIL_AND_PASSWORD_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void openGallery() {
        binding.ProfileFragmentIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PROFILE_REQUEST_QUDE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PROFILE_REQUEST_QUDE && resultCode == Activity.RESULT_OK) {
            profile_uri = data.getData();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage(R.string.RYShure);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            storage.upload("users", authentication.firebaseUser().getDisplayName(), user1.getUsername(), profile_uri);
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}