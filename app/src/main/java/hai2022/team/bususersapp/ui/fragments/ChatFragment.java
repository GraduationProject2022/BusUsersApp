package hai2022.team.bususersapp.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.adapters.ChatAdapter;
import hai2022.team.bususersapp.databases.firebase.Authentication;
import hai2022.team.bususersapp.databases.firebase.Realtime;
import hai2022.team.bususersapp.databinding.FragmentChatBinding;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.ReservationsListiner;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;
import hai2022.team.bususersapp.utils.Utils;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private Realtime realtime;
    private Authentication authentication;
    private ChatAdapter adapter;
    private ArrayList<Chat> chats1;
    private static ChatFragment chatFragment;
    private ProgressDialog dialog;


    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance() {
        if (chatFragment == null)
            chatFragment = new ChatFragment();
        return chatFragment;
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

            }

            @Override
            public void getMsgs(ArrayList<Chat> chats) {
                chats1.addAll(chats);
                Collections.reverse(chats1);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void updateUser(boolean state) {

            }
        }, new ReservationsListiner() {
            @Override
            public void numOfReservations(int num) {

            }

            @Override
            public void onReservationListener(boolean status) {

            }

            @Override
            public void isReserved(boolean status) {

            }

            @Override
            public void ReservedBusId(String id) {
                realtime.getMsgs(id);
            }
        });
        authentication = new Authentication();

        realtime.ReservedBusId(authentication.firebaseUser().getUid());

        realtime.getMsgs(authentication.firebaseUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        if (authentication.firebaseUser().getDisplayName().equals("student")){
            binding.chatSend.setVisibility(View.GONE);
            binding.chatMessage.setVisibility(View.GONE);
        }

        chats1 = new ArrayList<>();
        binding.chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat(binding.chatMessage.getText().toString(), Utils.currentDate(), Utils.currentTime(), authentication.firebaseUser().getUid());
                realtime.senMsg(chat);
                chats1.add(chat);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new ChatAdapter(chats1, getContext());
        binding.ChatFragmentRv.setAdapter(adapter);
        binding.ChatFragmentRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        return root;
    }
}