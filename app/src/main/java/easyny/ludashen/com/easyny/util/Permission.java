package easyny.ludashen.com.easyny.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
public class Permission {
    /**
     * 读写权限 自己可以添加需要判断的权限
     */

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限 false-表示权限已开启
     */
    public static boolean lacksPermissions(Context mContexts,String [] permissionsREAD) {
        for (String permission : permissionsREAD) {
            if (lacksPermission(mContexts,permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

}