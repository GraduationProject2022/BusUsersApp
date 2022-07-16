package hai2022.team.bususersapp.ui.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import hai2022.team.bususersapp.databinding.FragmentBusDetailsBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.ReservationsListiner;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;

public class BusDetailsFragment extends Fragment {
    private static BusDetailsFragment detailsFragment;
    private Bus bus;
    private FragmentBusDetailsBinding binding;
    //    private CloudStorage storage;
    private Realtime realtime;
    private Authentication authentication;
    private ProgressDialog dialog;
    private boolean isJoined = false;

    public BusDetailsFragment() {
        // Required empty public constructor
    }


    public static BusDetailsFragment newInstance(Bus bus) {
        if (detailsFragment == null)
            detailsFragment = new BusDetailsFragment();
        Bundle b = new Bundle();
        b.putSerializable("Bus", bus);
        detailsFragment.setArguments(b);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = (Bus) getArguments().getSerializable("Bus");
        }
        dialog = new ProgressDialog(getContext());
//        dialog.show();
        authentication = new Authentication();
//        storage = new CloudStorage(new StorageListener() {
//            @Override
//            public void onDownloadImageListener(Uri uri) {
//                Glide.with(getContext()).load(uri).placeholder(R.drawable.no_pictures).into(binding.BusDetailsFragmentIv);
//            }
//
//            @Override
//            public void onUploadImageListener(boolean status) {
//
//            }
//        })

        realtime = new Realtime(getContext(), new BusListiner() {
            @Override
            public void ceatebus(@NonNull Task<Void> task) {

            }

            @Override
            public void getUser(User user) {

            }

            @Override
            public void getBus(Bus bus) {
                binding.DriverDetailsFragmentTvName.setText(bus.getName());
                binding.DriverDetailsFragmentTvDriverName.setText(bus.getDriverName());
                binding.DriverDetailsFragmentTvLoc.setText(bus.getLocation());
                binding.DriverDetailsFragmentTvPhone.setText(bus.getPhone());
                binding.DriverDetailsFragmentTvSize.setText("" + bus.getPassengers());
                binding.DriverDetailsFragmentTvTime.setText(bus.getTimeToMove());
                if (bus.getLocation().equals("Gaza") || bus.getLocation().equals("غزة") || bus.getLocation().equals("الوسطى") || bus.getLocation().equals("الشمال")) {
                    binding.BusDetailsFragmentIv.setImageResource(R.drawable.gaza);
                } else if (bus.getLocation().equals("Khanyounis") || bus.getLocation().equals("خانيونس")) {
                    binding.BusDetailsFragmentIv.setImageResource(R.drawable.khanyounis);
                } else {
                    binding.BusDetailsFragmentIv.setImageResource(R.drawable.rafah);
                }
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
        }, new ReservationsListiner() {
            @Override
            public void numOfReservations(int num) {
                binding.DriverDetailsFragmentTvCurrentNum.setText("" + num);
            }

            @Override
            public void onReservationListener(boolean status) {
                if (status) {
                    if (isJoined) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "تم حذف الحجز بنجاح", Toast.LENGTH_SHORT).show();
                        isJoined = false;
                        binding.DriverDetailsFragmentFabJoin.setImageResource(R.drawable.ic_baseline_person_add_alt_1_24);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "تم الحجز بنجاح", Toast.LENGTH_SHORT).show();
                        isJoined = true;
                        binding.DriverDetailsFragmentFabJoin.setImageResource(R.drawable.ic_baseline_close_24);
                    }
                }
            }

            @Override
            public void isReserved(boolean status) {
//                dialog.dismiss();
                if (status) {
                    isJoined = true;
                    binding.DriverDetailsFragmentFabJoin.setImageResource(R.drawable.ic_baseline_close_24);
                }
            }

            @Override
            public void ReservedBusId(String id) {

            }
        });

        realtime.isReserved(authentication.firebaseUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBusDetailsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        realtime.getReservations(bus.getID());

//        storage.download(bus.getImgpath());

        if (bus!=null) {
            binding.DriverDetailsFragmentTvName.setText(bus.getName());
            binding.DriverDetailsFragmentTvDriverName.setText(bus.getDriverName());
            binding.DriverDetailsFragmentTvLoc.setText(bus.getLocation());
            binding.DriverDetailsFragmentTvPhone.setText(bus.getPhone());
            binding.DriverDetailsFragmentTvSize.setText("" + bus.getPassengers());
            binding.DriverDetailsFragmentTvTime.setText("" + bus.getTimeToMove());
            if (bus.getLocation().equals("Gaza") || bus.getLocation().equals("غزة") || bus.getLocation().equals("الوسطى") || bus.getLocation().equals("الشمال")) {
                binding.BusDetailsFragmentIv.setImageResource(R.drawable.gaza);
            } else if (bus.getLocation().equals("Khanyounis") || bus.getLocation().equals("خانيونس")) {
                binding.BusDetailsFragmentIv.setImageResource(R.drawable.khanyounis);
            } else {
                binding.BusDetailsFragmentIv.setImageResource(R.drawable.rafah);
            }
        }else{
            realtime.getBus(authentication.firebaseUser().getUid());
        }
        binding.DriverDetailsFragmentFabJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.setMessage("in progress...");
                if (!isJoined) {
                    if (Integer.parseInt(binding.DriverDetailsFragmentTvCurrentNum.getText().toString()) < bus.getPassengers()) {
                        realtime.addReversation(bus.getID(), authentication.firebaseUser().getUid(), "add");
                    } else {
                        dialog.dismiss();
                    }
                } else {
                    realtime.addReversation(bus.getID(), authentication.firebaseUser().getUid(), "remove");
                }
            }
        });

        return root;
    }
}