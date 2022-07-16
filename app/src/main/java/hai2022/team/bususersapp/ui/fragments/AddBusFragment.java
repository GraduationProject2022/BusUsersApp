package hai2022.team.bususersapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentAddBusBinding;
import hai2022.team.bususersapp.interfaces.AuthListiner;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.ui.activities.MainActivity;
import hai2022.team.bususersapp.ui.activities.SignInActivity;
import hai2022.team.bususersapp.utils.Constants;

public class AddBusFragment extends Fragment {
    private static AddBusFragment addBusFragment;
    private FragmentAddBusBinding binding;
    private Uri bus_uri;
    private Bus bus;
    Realtime realtime;
    private String Imagepath;

    public AddBusFragment() {
        // Required empty public constructor
    }


    public static AddBusFragment newInstance() {
        if (addBusFragment == null)
            addBusFragment = new AddBusFragment();

        return addBusFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBusBinding.inflate(getLayoutInflater());
        editImage();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        realtime = new Realtime(getContext(), new BusListiner() {
            @Override
            public void ceatebus(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Imagepath = bus_uri != null ? "users/" + bus.getName() + "/" + bus.getDriverName().replace(" ", "") + bus_uri.getLastPathSegment() : bus.getImgpath();
                    bus.setImgpath(Imagepath);
                    Toast.makeText(getContext(), "Bus Added", Toast.LENGTH_SHORT).show();
                    if (bus_uri != null) {
                        CloudStorage storage = new CloudStorage(new StorageListener() {
                            @Override
                            public void onDownloadImageListener(Uri uri) {

                            }

                            @Override
                            public void onUploadImageListener(boolean status) {
                                if (status) {
                                    Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "image not uploaded", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        storage.upload("Buses", "BusDrivers", bus.getName().replace(" ", ""), bus_uri);

                    } else {

                    }
                    getActivity().finishAffinity();
                    startActivity(new Intent(getContext(), SignInActivity.class));
                }
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

            }

        });


        binding.SignupBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.ProfileFragmentEtEmail.getText().toString();
                String password = binding.ProfileFragmentEtPassword.getText().toString();
                Authentication authentication2 = new Authentication();
                Authentication authentication = new Authentication(getContext(), new AuthListiner() {
                    @Override
                    public void Signup(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            bus = new Bus(task.getResult().getUser().getUid(), binding.ProfileFragmentEtUsername.getText().toString(), binding.ProfileFragmentEtDriverName.getText().toString()
                                    , binding.ProfileFragmentEtEmail.getText().toString(), "Buses/" + "BusDrivers/" + binding.ProfileFragmentEtDriverName.getText().toString() + bus_uri.getLastPathSegment(), Integer.parseInt(binding.ProfileFragmentSpPassengers.getSelectedItem().toString())
                                    , 6, 1.1, binding.ProfileFragmentSpLocations.getSelectedItem().toString(), binding.ProfileFragmentEtPhone.getText().toString(), "06:00");
                            bus.setTimeToMove(binding.ProfileFragmentEtTime.getText().toString());
                            task.getResult().getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName("bus").build());
                            task.getResult().getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName("student").build());
                            realtime.createBus(task.getResult().getUser().getUid(), bus);
                        }
                    }

                    @Override
                    public void editInfo(@NonNull Task<Void> task, String edit) {

                    }
                });

                authentication.Signup(email, password);
            }
        });


    }


    private void editImage() {
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
            bus_uri = data.getData();

            binding.ProfileFragmentIvProfile.setImageURI(bus_uri);
        }
    }


}