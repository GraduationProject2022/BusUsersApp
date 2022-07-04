package hai2022.team.bususersapp.interfaces;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;

public interface BusListiner {
    void ceatebus(@NonNull Task<Void> task);
    void getUser(User user);
    void getBus(Bus bus);
    void getAdmins(ArrayList<User> users);
    void getDrivers(ArrayList<User> users);
    void getStudents(ArrayList<User> users);
    void getBuses(ArrayList<Bus> buses);
    void getMsgs(ArrayList<Chat> chats);
    void updateUser(boolean state);
}
