package i2f.android.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    public static boolean onExternalReadWritePermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == REQUEST_CODE_READ_WRITE_STORAGE_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "外部存取权限已经获取！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "外部存取权限未获取！", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return false;
    }

    public static boolean onExternalManagePermissionActivityResult(Activity activity,int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_CODE_MANAGE_FILES_PERMISSION && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){
                Toast.makeText(activity, "外部管理权限已经获取！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "外部管理权限已经获取！", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}
