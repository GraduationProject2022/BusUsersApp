package hai2022.team.bususersapp.ui.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databases.firebase.CloudStorage;
import hai2022.team.bususersapp.databinding.FragmentDriverDetailsBinding;
import hai2022.team.bususersapp.interfaces.StorageListener;
import hai2022.team.bususersapp.models.Bus;

public class DriverDetailsFragment extends Fragment {

    private static DriverDetailsFragment detailsFragment;
    private Bus bus;
    private Uri uri;
    private FragmentDriverDetailsBinding binding;
    private CloudStorage storage;

    public DriverDetailsFragment() {
        // Required empty public constructor
    }


    public static DriverDetailsFragment newInstance(Bus bus, Uri uri) {
        if (detailsFragment == null) {
            detailsFragment = new DriverDetailsFragment();
        }
        if (bus != null) {
            Bundle b = new Bundle();
            b.putSerializable("Bus", bus);
         /*   if (uri!=null)
            b.putParcelable("Uri", uri);*/
            detailsFragment.setArguments(b);
        }

        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bus = (Bus) getArguments().getSerializable("Bus");
//            uri = getArguments().getParcelable("Uri");
        }

        storage = new CloudStorage(new StorageListener() {
            @Override
            public void onDownloadImageListener(Uri uri) {
                if (uri != null) {
                    Glide.with(getContext()).load(uri).placeholder(R.drawable.no_pictures).into(binding.DriverDetailsFragmentIv);
                }
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
        binding = FragmentDriverDetailsBinding.inflate(getLayoutInflater());
        View Root = binding.getRoot();

        binding.DriverDetailsFragmentTvName.setText(bus.getDriverName());
        binding.DriverDetailsFragmentTvLoc.setText(bus.getLocation());
        binding.DriverDetailsFragmentTvPhone.setText(bus.getPhone() + "");


        if (!bus.getImgpath().equals(""))
            storage.download(bus.getImgpath());
        return Root;
    }

}