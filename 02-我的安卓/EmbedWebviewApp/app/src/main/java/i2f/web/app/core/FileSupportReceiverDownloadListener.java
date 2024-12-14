package i2f.web.app.core;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.DownloadListener;

import java.util.HashMap;
import java.util.Map;

import i2f.web.app.DownloadUtils;

import static android.content.Context.DOWNLOAD_SERVICE;

public class FileSupportReceiverDownloadListener extends BroadcastReceiver implements DownloadListener {
    protected Activity activity;
    protected Map<Long,String> downloadIds=new HashMap<>();
    public FileSupportReceiverDownloadListener(Activity activity){
        this.activity=activity;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.activity.registerReceiver(this, intentFilter);
    }
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            long downloadId= DownloadUtils.downloadBySysDownloader(activity,url,contentDisposition,mimetype);
            downloadIds.put(downloadId,url);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                String type = downloadManager.getMimeTypeForDownloadedFile(downloadId);
                downloadIds.remove(downloadId);
                if (TextUtils.isEmpty(type)) {
                    type = "*/*";
                }
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                if (uri != null) {
                    Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
                    handlerIntent.setDataAndType(uri, type);
                    context.startActivity(handlerIntent);
                }
            }
        }
    }
}
