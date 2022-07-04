package hai2022.team.bususersapp.interfaces;

import android.net.Uri;

public interface StorageListener {
    void onDownloadImageListener(Uri uri);
    void onUploadImageListener(boolean status);

}
