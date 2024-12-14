package i2f.android.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

public class PermissionUtil {
    private static final int REQUEST_CODE_READ_WRITE_STORAGE_PERMISSION = 0x1001;
    public static final int REQUEST_CODE_MANAGE_FILES_PERMISSION = 0x1002;

    public static void requireExternalReadWriteManagePermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, REQUEST_CODE_MANAGE_FILES_PERMISSION);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_READ_WRITE_STORAGE_PERMISSION);
        }
    }
}
