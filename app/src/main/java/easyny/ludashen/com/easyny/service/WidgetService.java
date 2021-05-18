package easyny.ludashen.com.easyny.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import easyny.ludashen.com.easyny.MainActivity;
import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.util.Tool;

/**
 * 控制 桌面小部件 更新
 * Created by lyl on 2017/8/23.
 */
public class WidgetService extends Service {

    // 更新 widget 的广播对应的 action
    private final String ACTION_UPDATE_ALL = "com.lyl.widget.UPDATE_ALL";
    // 周期性更新 widget 的周期
    private static final int UPDATE_TIME = 7200000;

    private Timer mTimer;
    private TimerTask mTimerTask;
    // 将0~9的液晶数字图片定义成数组
    private int[] digits = new int[] { R.drawable.digit00, R.drawable.digit01,
            R.drawable.digit02, R.drawable.digit03, R.drawable.digit04,
            R.drawable.digit05, R.drawable.digit06, R.drawable.digit07,
            R.drawable.digit08, R.drawable.digit09, };

    // 将显示年 月 日 小时、分钟、秒钟的ImageView定义成数组
    private int[] digitViews = new int[] {
            R.id.img12, R.id.img13, R.id.img15, R.id.img16, R.id.img18,
            R.id.img19 };

    private Timer timer;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            /** 获取并格式化当前时间 */
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String time = sdf.format(new Date());
            /** 获得显示时间的View */
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.app_widget);
            for (int i = 0; i < time.length(); i++) {
                // 将第i个数字字符转换为对应的数字
                int num = time.charAt(i) - 48;
                // 将第i张图片设为对应的液晶数字图片
                views.setImageViewResource(digitViews[i], digits[num]);
            }
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(getApplicationContext());

            ComponentName componentName = new ComponentName(
                    getApplicationContext(), WidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, views);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 定义计时器
        timer = new Timer();
        // 启动周期性调度
        timer.schedule(timerTask, 0, 1000);

        // 每经过指定时间，发送一次广播
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                sendBroadcast(updateIntent);
                try {
                    String tem = "";
                    JSONArray coures = coures();
                    for (int i = 0; i < coures.length(); i++) {
                        JSONObject jsonObject = coures.getJSONObject(i);
                        String[] kcsj = jsonObject.getString("kcsj").split("0");
                        if (((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) + "").equals(kcsj[0])) {
                            tem += jsonObject.getString("kcmc") + "教室：" + jsonObject.getString("jsmc") + "\n上课时间：" + jsonObject.getString("kssj") + "——下课时间：" + jsonObject.getString("jssj")+"\n";
                        }
                    }
                    WidgetProvider.setData(tem);
                } catch (Exception e) {
                }
            }
        };
        mTimer.schedule(mTimerTask, 1000, UPDATE_TIME);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public  JSONArray coures() {
        JSONArray ck = new JSONArray();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        try {
            if (Tool.fileExists(this, "kcb")) {
                String info = Tool.readinfo(this, "kcb");
                String[] result = info.split("@@");
                if (result[0].equals(times))
                    ck = new JSONArray(result[1]);
            }
        } catch (Exception e) {
            Tool.deleteFile(this, "kcb");
            Toast.makeText(this, "可能是秘钥过期了或者文件读取异常，退出在进来就没问题", Toast.LENGTH_SHORT).show();
        }
        return ck;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerTask.cancel();
        mTimer.cancel();

        timer.cancel();
        timer = null;
    }

    /*
     *  服务开始时，即调用startService()时，onStartCommand()被执行。
     *
     *  这个整形可以有四个返回值：start_sticky、start_no_sticky、START_REDELIVER_INTENT、START_STICKY_COMPATIBILITY。
     *  它们的含义分别是：
     *  1):START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，
     *     由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null;
     *  2):START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务;
     *  3):START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入;
     *  4):START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
