package easyny.ludashen.com.easyny.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import easyny.ludashen.com.easyny.R;

public class ThemeUtil {
    public static class ThemeColors {
        public static final int THEME_GREEN = 1;
        public static final int ThEME_BLUE = 2;
        public static final int THEME_GREY = 3;
        public static final int THEME_YELLOW = 4;
        public static final int ZT1 = 5;
        public static final int ZT2 = 6;
        public static final int ZT3 = 7;
        public static final int ZT4 = 8;
        public static final int ZT5 = 9;
        public static final int ZT6 = 10;
    }

    public static void setBaseTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyThemeShared", context.MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 0);
        int themeId;
        switch (themeType) {
            case ThemeColors.THEME_GREEN:
                themeId = R.style.AppThemeNoAction_Green;
                break;
            case ThemeColors.ThEME_BLUE:
                themeId = R.style.AppThemeNoAction_Blue;
                break;

            case ThemeColors.THEME_GREY:
                themeId = R.style.AppThemeNoAction_Grey;
                break;
            case ThemeColors.THEME_YELLOW:
                themeId = R.style.AppThemeNoAction_Yellow;
                break;
            case ThemeColors.ZT1:
                themeId = R.style.zt1;
                break;
            case ThemeColors.ZT2:
                themeId = R.style.zt2;
                break;
            case ThemeColors.ZT3:
                themeId = R.style.zt3;
                break;
            case ThemeColors.ZT4:
                themeId = R.style.zt4;
                break;
            case ThemeColors.ZT5:
                themeId = R.style.zt5;
                break;
            case ThemeColors.ZT6:
                themeId = R.style.zt6;
                break;
            default:
                themeId = R.style.AppTheme;
        }
        context.setTheme(themeId);
    }

    public static void setBackgroud(Context context ,View v){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyThemeShared", context.MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 0);
        int themeId = R.drawable.zt1;
        switch (themeType) {
            case ThemeColors.ZT1:
                themeId = R.drawable.zt1;
                break;
            case ThemeColors.ZT2:
                themeId = R.drawable.zt2;
                break;
            case ThemeColors.ZT3:
                themeId = R.drawable.zt3;
                break;
            case ThemeColors.ZT4:
                themeId = R.drawable.zt4;
                break;
            case ThemeColors.ZT5:
                themeId = R.drawable.zt5;
                break;
            case ThemeColors.ZT6:
                themeId = R.drawable.zt6;
                break;
        }
        if (themeType>4)
            v.setBackgroundResource(themeId);
    }
    public static boolean setNewTheme(Context context, int theme) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "MyThemeShared", context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt("theme_type",theme);
        return  e.commit();//有返回值
    }


}