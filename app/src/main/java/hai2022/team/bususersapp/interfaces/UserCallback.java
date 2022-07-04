package hai2022.team.bususersapp.interfaces;


import android.net.Uri;

import hai2022.team.bususersapp.models.Bus;

public interface UserCallback {
    void bus_click_listener(Bus bus, Uri uri);
    void add_new_bus();
}
