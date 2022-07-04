package hai2022.team.bususersapp.databases.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.interfaces.BusListiner;
import hai2022.team.bususersapp.interfaces.ReservationsListiner;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.models.Chat;
import hai2022.team.bususersapp.models.User;


public class Realtime {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private BusListiner userListiner;
    private ReservationsListiner reservationsListiner;
    private Context context;

    public Realtime(Context context, BusListiner userListiner) {
        this.context = context;
        this.userListiner = userListiner;
        this.database = FirebaseDatabase.getInstance("https://busapplication-f0066-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.myRef = database.getReference("");
    }

    public Realtime(Context context, BusListiner userListiner, ReservationsListiner reservationsListiner) {
        this.context = context;
        this.userListiner = userListiner;
        this.reservationsListiner = reservationsListiner;
        this.database = FirebaseDatabase.getInstance("https://busapplication-f0066-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.myRef = database.getReference("");
    }

    public Realtime(Context context, ReservationsListiner reservationsListiner) {
        this.context = context;
        this.reservationsListiner = reservationsListiner;
        this.database = FirebaseDatabase.getInstance("https://busapplication-f0066-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.myRef = database.getReference("");
    }


    public void createUser(String uid, User user) {
        Toast.makeText(context, "test" + uid, Toast.LENGTH_SHORT).show();
// Write a message to the datab-ase
        user.setImgPath("");
        myRef.child("users").child(user.getType()).child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userListiner.ceatebus(task);
            }
        });
    }


    public void createBus(String uid, Bus bus) {
// Write a message to the database
        myRef.child("buses").child(uid).setValue(bus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userListiner.ceatebus(task);
            }
        });
    }

    public void senMsg(Chat msg) {
// Write a message to the database
        myRef.child("msgs").child(msg.getUserId()).push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void getUser(String type, String id) {
        myRef.child("users").child(type).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userListiner.getUser(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getBus(String id) {
        myRef.child("buses").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userListiner.getBus(snapshot.getValue(Bus.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getReservations(String id) {
        myRef.child("Reservations").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservationsListiner.numOfReservations((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void isReserved(String uid) {
        myRef.child("users").child("student").child("Reservation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reservationsListiner.isReserved(snapshot.hasChild(uid));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ReservedBusId(String uid) {
        myRef.child("users").child("student").child("Reservation").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String,String> map = (HashMap<String, String>) snapshot.getValue();
                    reservationsListiner.ReservedBusId(map.get("busid"));
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addReversation(String id, String uid, String type) {
        if (type.equals("add")){
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("busid", id);
        myRef.child("Reservations").child(id).child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                myRef.child("users").child("student").child("Reservation").child(uid).setValue(map);
                reservationsListiner.onReservationListener(task.isSuccessful());
            }
        });
        }else{
            myRef.child("Reservations").child(id).child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    myRef.child("users").child("student").child("Reservation").child(uid).setValue(null);
                    reservationsListiner.onReservationListener(task.isSuccessful());
                }
            });
        }
    }

    public void getUsers(String type) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("", R.drawable.ic_baseline_home_24, "Add new " + type, ""));

        myRef.child("users").child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    users.add(snapshot1.getValue(User.class));
                }
                if (type.equalsIgnoreCase("admin"))
                    userListiner.getAdmins(users);
                else if (type.equalsIgnoreCase("driver"))
                    userListiner.getDrivers(users);
                else
                    userListiner.getStudents(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void getBuses(String type) {
        ArrayList<Bus> buses = new ArrayList<>();
//        if (!type.equals("student"))
//        buses.add(new Bus("Add new Bus","Add new Bus","Add new Bus","",0,"Add new Bus",""));

        myRef.child("buses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    buses.add(snapshot1.getValue(Bus.class));
                }
                userListiner.getBuses(buses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getMsgs(String id) {
        ArrayList<Chat> chats = new ArrayList<>();
        myRef.child("msgs").child(id).orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    chats.add(snapshot1.getValue(Chat.class));
                }
                userListiner.getMsgs(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateUser(User user) {
        myRef.child("users").child(user.getType()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userListiner.updateUser(task.isSuccessful());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
