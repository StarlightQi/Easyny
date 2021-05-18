package easyny.ludashen.com.easyny.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Timer;
import java.util.TimerTask;

import easyny.ludashen.com.easyny.view.FloatBallView;
import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.SimpleViewWitchParam;
import easyny.ludashen.com.easyny.util.FloatUtil;

/**
 * Created by zimo on 15/12/15.
 */
public class MyService extends Service {

    private Handler handler = new Handler();

    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final FloatBallView floatBallView = new FloatBallView(getApplicationContext());
                        FloatUtil.showFloatView(floatBallView, Gravity.LEFT | Gravity.TOP, WindowManager.LayoutParams.TYPE_TOAST,new Point(0,0), null, true);
                        final SimpleViewWitchParam simpleView = new SimpleViewWitchParam(getApplicationContext());
                        floatBallView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                FloatUtil.hideFloatView(MainActivity.this, FloatBallView.class, false);
                                WebView web = (WebView) simpleView.findViewById(R.id.web);
                                web.loadUrl("http://www.baidu.com");
                                web.loadUrl("https://www.baidu.com");
                                WebSettings settings = web.getSettings();
                                settings.setSupportZoom(true);
                                settings.setBuiltInZoomControls(true);
                                settings.setDisplayZoomControls(false);
                                settings.setUseWideViewPort(true);
                                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                                settings.setLoadWithOverviewMode(true);
                                settings.setJavaScriptEnabled(true);
                                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                                web.setWebChromeClient(new WebChromeClient());
                                FloatUtil.showSmartFloat(simpleView, Gravity.LEFT | Gravity.TOP, new Point(0,0), null, true);
                                simpleView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FloatUtil.hideFloatView(getApplicationContext(), SimpleViewWitchParam.class, false);
                                    }
                                });
                            }
                        });

                    }
                });

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();

    }
}
