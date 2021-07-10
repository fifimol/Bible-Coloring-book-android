package com.support.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.widget.Toast;

import com.biblestory.color.R;
import com.nostra13.universalimageloader.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PermissionUtils {
    static final int REQUEST_CODE_LIST_PERMISSION = 22;
    static final int REQUEST_CODE_SINGLE_PERMISSION = 11;
    static String[] androidPermissionList = new String[]{"android.permission.WRITE_CALENDAR", "android.permission.CAMERA", "android.permission.WRITE_CONTACTS", "android.permission.ACCESS_FINE_LOCATION", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE", "android.permission.BODY_SENSORS", "android.permission.SEND_SMS", "android.permission.WRITE_EXTERNAL_STORAGE"};
    static AlertDialog dialog;
    static AlertDialog listDialog;
    static Integer[] mPermissionList = new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9)};
    private static OnRequestedPermissionListListener onRequestedPermissionListListener;
    private static OnRequestedPermissionListener onRequestedPermissionListener;
    static ArrayList<Integer> requestPermissionsListForRationale;
    static ArrayList<Integer> requestedPermissionsList;
    private static int verifyPermission;
    private static String verifyRealPermission;

    static class C04781 implements Runnable {
        private final /* synthetic */ String val$androidPermission;
        private final /* synthetic */ Activity val$context;
        private final /* synthetic */ int val$mPermission;

        C04781(String str, int i, Activity activity) {
            this.val$androidPermission = str;
            this.val$mPermission = i;
            this.val$context = activity;
        }

        public void run() {
            PermissionUtils.verifyRealPermission = this.val$androidPermission;
            PermissionUtils.verifyPermission = this.val$mPermission;
            PermissionUtils.showPermissionDialog(this.val$context, this.val$context.getResources().getStringArray(R.array.ratinale_list)[this.val$mPermission - 1]);
        }
    }

    static class C04792 implements OnClickListener {
        private final /* synthetic */ Context val$context;

        C04792(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.val$context.getPackageName(), null));
            this.val$context.startActivity(intent);
        }
    }

    static class C04803 implements OnCancelListener {
        private final /* synthetic */ Context val$context;

        C04803(Context context) {
            this.val$context = context;
        }

        public void onCancel(DialogInterface arg0) {
            PermissionUtils.verifyPermission(this.val$context);
        }
    }

    static class C04814 implements OnClickListener {
        private final /* synthetic */ Context val$context;

        C04814(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.val$context.getPackageName(), null));
            this.val$context.startActivity(intent);
        }
    }

    static class C04825 implements OnCancelListener {
        private final /* synthetic */ Context val$context;

        C04825(Context context) {
            this.val$context = context;
        }

        public void onCancel(DialogInterface arg0) {
            PermissionUtils.verifyPermission(this.val$context);
        }
    }

    public interface OnRequestedPermissionListListener {
        void onPermissionListResult(ArrayList<Integer> arrayList, ArrayList<Boolean> arrayList2);
    }

    public interface OnRequestedPermissionListener {
        void onPermissionResult(int i, boolean z);
    }

    public static boolean checkPermission(int permission, Context context) {
        if (getAndroidPermission(permission) == null) {
            Toast.makeText(context, "You have checked for invalid permission", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkSelfPermissions(context, getAndroidPermission(permission)) == 0) {
            return true;
        }
        return false;
    }

    private static int checkSelfPermissions(Context context, String... permissions) {
        if (permissions.length == 0) {
            return 1;
        }
        String permission = permissions[0];
        if (ContextCompat.checkSelfPermission(context, permission) == 0) {
            return 0;
        }
        if (ContextCompat.checkSelfPermission(context, permission) == -1) {
            return -1;
        }
        return 1;
    }

    public static void verifyPermission(Context context) {
        if (!(verifyRealPermission == null || onRequestedPermissionListener == null)) {
            if (checkSelfPermissions(context, verifyRealPermission) == 0) {
                onRequestedPermissionListener.onPermissionResult(verifyPermission, true);
            } else {
                onRequestedPermissionListener.onPermissionResult(verifyPermission, false);
            }
            verifyRealPermission = null;
            onRequestedPermissionListener = null;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        Iterator it;
        if (requestPermissionsListForRationale != null && requestPermissionsListForRationale.size() > 0 && onRequestedPermissionListListener != null) {
            String message = BuildConfig.FLAVOR + context.getString(R.string.permission_list_rationale_message1) + " ";
            String[] permission_words = context.getResources().getStringArray(R.array.permission_words);
            it = requestPermissionsListForRationale.iterator();
            while (it.hasNext()) {
                message = new StringBuilder(String.valueOf(message)).append(permission_words[((Integer) it.next()).intValue() - 1]).append(", ").toString();
            }
            showPermissionListDialog(context, message.substring(0, message.length() - 2) + ". " + context.getString(R.string.permission_list_rationale_message2));
            requestPermissionsListForRationale.clear();
            requestPermissionsListForRationale = null;
        } else if (requestedPermissionsList != null && requestedPermissionsList.size() > 0 && onRequestedPermissionListListener != null) {
            ArrayList<Integer> responsePermissionList = new ArrayList();
            ArrayList<Boolean> isPermissionGrantedList = new ArrayList();
            it = requestedPermissionsList.iterator();
            while (it.hasNext()) {
                responsePermissionList.add(Integer.valueOf(((Integer) it.next()).intValue()));
                int mPermission = 0;
                if (checkSelfPermissions(context, androidPermissionList[mPermission - 1]) == 0) {
                    isPermissionGrantedList.add(Boolean.valueOf(true));
                } else {
                    isPermissionGrantedList.add(Boolean.valueOf(false));
                }
            }
            onRequestedPermissionListListener.onPermissionListResult(responsePermissionList, isPermissionGrantedList);
            onRequestedPermissionListListener = null;
            requestedPermissionsList.clear();
            requestedPermissionsList = null;
        }
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldShowRequestPermissionRationale(Fragment fragment, String... permissions) {
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }

    public static void requestPermission(Activity context, ArrayList<Integer> mPermissionListToRequest, OnRequestedPermissionListListener requestedPermissionListListener) {
        requestedPermissionsList = new ArrayList(mPermissionListToRequest);
        onRequestedPermissionListListener = requestedPermissionListListener;
        ArrayList<String> androidPermissionListToRequest = getAndroidPermissionList(requestedPermissionsList);
        if (androidPermissionListToRequest == null || androidPermissionListToRequest.size() == 0) {
            Toast.makeText(context, "You have requested for wrong permissions.", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> requestPermissionsListToRequest = null;
        Iterator it = androidPermissionListToRequest.iterator();
        while (it.hasNext()) {
            String permission = (String) it.next();
            if (checkSelfPermissions(context, permission) != 0) {
                if (shouldShowRequestPermissionRationale(context, permission)) {
                    if (requestPermissionsListForRationale == null) {
                        requestPermissionsListForRationale = new ArrayList();
                    }
                    requestPermissionsListForRationale.add(mPermissionList[Arrays.asList(androidPermissionList).indexOf(permission)]);
                } else {
                    if (requestPermissionsListToRequest == null) {
                        requestPermissionsListToRequest = new ArrayList();
                    }
                    requestPermissionsListToRequest.add(permission);
                }
            }
        }
        if (requestPermissionsListToRequest == null) {
            verifyPermission(context);
        } else {
            ActivityCompat.requestPermissions(context, (String[]) requestPermissionsListToRequest.toArray(new String[requestPermissionsListToRequest.size()]), 22);
        }
    }

    public static void requestPermission(Activity context, int mPermission, OnRequestedPermissionListener requestedPermissionListener) {
        onRequestedPermissionListener = requestedPermissionListener;
        String androidPermission = getAndroidPermission(mPermission);
        if (androidPermission == null) {
            Toast.makeText(context, "You have requested for wrong permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkSelfPermissions(context, androidPermission) != 0) {
            if (shouldShowRequestPermissionRationale(context, androidPermission)) {
                new Handler().postDelayed(new C04781(androidPermission, mPermission, context), 200);
                return;
            }
            ActivityCompat.requestPermissions(context, new String[]{androidPermission}, 11);
            verifyRealPermission = androidPermission;
            verifyPermission = mPermission;
        } else if (onRequestedPermissionListener != null) {
            onRequestedPermissionListener.onPermissionResult(mPermission, true);
        }
    }

    private static ArrayList<String> getAndroidPermissionList(ArrayList<Integer> mPermissionListToRequest) {
        ArrayList<String> permissionList = null;
        Iterator it = mPermissionListToRequest.iterator();
        while (it.hasNext()) {
            int mPermission = ((Integer) it.next()).intValue();
            if (!Arrays.asList(mPermissionList).contains(Integer.valueOf(mPermission))) {
                return null;
            }
            if (permissionList == null) {
                permissionList = new ArrayList();
            }
            permissionList.add(androidPermissionList[mPermission - 1]);
        }
        return permissionList;
    }

    static String getAndroidPermission(int mPermission) {
        if (mPermission <= 0 || mPermission >= 10) {
            return null;
        }
        return androidPermissionList[mPermission - 1];
    }

    static void showPermissionListDialog(Context context, String permissionMessage) {
        if (listDialog == null || !listDialog.isShowing()) {
            listDialog = new Builder(context).setTitle(context.getString(R.string.allow_permission)).setMessage(Html.fromHtml(permissionMessage)).setPositiveButton(context.getString(R.string.app_settings), new C04792(context)).setCancelable(true).create();
            listDialog.setOnCancelListener(new C04803(context));
            listDialog.show();
        }
    }

    static void showPermissionDialog(Context context, String permissionMessage) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new Builder(context).setTitle(context.getString(R.string.allow_permission)).setMessage(Html.fromHtml(permissionMessage)).setPositiveButton(context.getString(R.string.app_settings), new C04814(context)).setCancelable(true).create();
            dialog.setOnCancelListener(new C04825(context));
            dialog.show();
        }
    }
}
