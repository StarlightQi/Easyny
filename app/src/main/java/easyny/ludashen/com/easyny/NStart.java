package easyny.ludashen.com.easyny;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import easyny.ludashen.com.easyny.Chat.Chat;
import easyny.ludashen.com.easyny.util.PasswordMag;
import easyny.ludashen.com.easyny.util.Permission;
import easyny.ludashen.com.easyny.util.Tool;

public class NStart extends Activity implements Runnable {
    private PasswordMag openHelper;
    private Cursor cursor;
    private long timeout = 0;
    private Handler aoto = new Handler();
    private Timer timer;
    private WebView webView;
    private Handler handler=new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    if(((String) msg.obj).contains("文件取消分享了")) {
                        Toast.makeText(getApplicationContext(), "你好检测到有新版本请自行到帮助下，下载最新版本",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    webView.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        webView = (WebView) findViewById(R.id.web);
        webView.setEnabled(false);
        openHelper = new PasswordMag(this);
        openHelper.getReadableDatabase();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.setWebChromeClient(new MywebChromeClient()); //在加载网页前加上这句就可以了
        Button sip= (Button) findViewById(R.id.sip);
        sip.bringToFront();
        sip.setAlpha(0.5f);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                start();
            }
        };

        sip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aoto.removeCallbacks(runnable);
                start();
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isnetwork()){
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                    return;
                }
                timer = new Timer();
                final int progress = webView.getProgress();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        if (progress <50) {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                            timer.cancel();
                            timer.purge();
                        }
                    }
                };
                timer.schedule(tt, timeout, 1);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (timer!=null){
                    timer.cancel();
                    timer.purge();
                }
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
        aoto.postDelayed(runnable, 8000);
        thread = new Thread(this);
    }


    private void start(){
        if (Tool.fileExists(NStart.this,"chose")) {
            String ch = Tool.readinfo(NStart.this, "chose").trim();
            Intent jw = new Intent(NStart.this, NyWeb.class);
            SQLiteDatabase r = openHelper.getWritableDatabase();
            switch (ch) {
                case "0":
                    String s = "select *from pass where name=?";
                    jw.putExtra("url","https://school.gczlsa.com/#/index");
                    cursor = r.rawQuery(s,new String[]{"饭堂"});
                    if (cursor.moveToFirst()) {
                        String js = "javascript:document.getElementsByTagName('input')[0].value = '" +cursor.getString(1)+ "';document.getElementsByTagName('input')[1].value='" +cursor.getString(2) + "';" ;
                        jw.putExtra("js",js);
                    }
                    jw.putExtra("search",true);
                    startActivity(jw);
                    break;
                case "1":
                    zhengchang();
                    break;
                case "2":
                    Intent ss= new Intent(NStart.this, NyWeb.class);
                    ss.putExtra("url","https://www.baidu.com/");
                    ss.putExtra("search",true);
                    startActivity(ss);
                    break;
                case "3":
                    Intent b= new Intent(NStart.this, NyWeb.class);
                    b.putExtra("url","http://106.13.122.53/ludage/");
                    b.putExtra("search",true);
                    startActivity(b);
                    break;
                case "4":
                    if (Permission.lacksPermissions(NStart.this,Mains.permissionsREAD)){
                        ActivityCompat.requestPermissions(NStart.this,Mains.permissionsREAD,0);
                        Toast.makeText(NStart.this,"你的读写权限没有授权，如果授权失败请手动处理",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent mainIntent = new Intent(NStart.this, DiaryList.class);
                        mainIntent.putExtra("search",true);
                        startActivity(mainIntent);
                    }
                    break;
                case "5":
                    Intent mainIntent = new Intent(NStart.this, Nnxy.class);
                    mainIntent.putExtra("search",true);
                    startActivity(mainIntent);
                    break;
                case "6":
                    Intent yule = new Intent(NStart.this, Yule.class);
                    yule.putExtra("search",true);
                    startActivity(yule);
                    break;
                case "7":
                    Intent ai = new Intent(NStart.this, Ai.class);
                    ai.putExtra("search",true);
                    startActivity(ai);
                    break;
                case "8":
                    Intent bq= new Intent(NStart.this, NyWeb.class);
                    bq.putExtra("url","https://m.biquge5200.com/");
                    bq.putExtra("search",true);
                    startActivity(bq);
                    break;
                case "9":

                    String vs = "select *from pass where name=?";
                    jw.putExtra("url", "http://jw.nnxy.cn/jsxsd/");
                    String js="";
                    cursor = r.rawQuery(vs,new String[]{"教务系统"});
                    if (cursor.moveToFirst()) {
                        js = "javascript:document.getElementById('userAccount').value = '" + cursor.getString(1) + "';document.getElementById('userPassword').value='" +  cursor.getString(2) + "';" +
                                "document.getElementsByTagName(\"span\")[0].innerHTML=\"<a style='color: blue;text-decoration:underline' href='mailto:ludagewudi@gmail.com?subject=我想对陆大哥说&body=我在用的的便利南院app做得很好，陆大哥我很崇拜你呢！！'>技术：陆大哥  邮箱:ludagewudi@gmail.com</a>\";";
                    } else {
                        js = "javascript:document.getElementById('userAccount').value = '" + "" + "';document.getElementById('userPassword').value='" + "" + "';" +
                                "document.getElementsByTagName(\"span\")[0].innerHTML=\"<a style='color: blue;text-decoration:underline' href='mailto:ludagewudi@gmail.com?subject=我想对陆大哥说&body=我在用的的便利南院app做得很好，陆大哥我很崇拜你呢！！'>技术：陆大哥  邮箱:ludagewudi@gmail.com</a>\";";
                    }
                    jw.putExtra("url", "http://jw.nnxy.cn/jsxsd/");
                    jw.putExtra("js",js);
                    startActivity(jw);
                    break;
                case "10":
                    Intent yul = new Intent(NStart.this, Chat.class);
                    yul.putExtra("search",true);
                    startActivity(yul);
                    break;
            }
            finish();
        }else {
            zhengchang();
        }
    }
    private void zhengchang(){
        Intent mainIntent = null;
        File file = NStart.this.getFileStreamPath("info.txt");
        if (file.exists()) {
            try {
                String readinfo = Tool.readinfo(NStart.this, "info.txt");
                String[] result = readinfo.split("##");
                String fn = HtmlService.checkuser(result[0], result[1]);
                MainActivity.User(new JSONObject(fn));
                Cjcx.User(new JSONObject(fn));
                Kcb.User(new JSONObject(fn));
                mainIntent = new Intent(NStart.this, MainActivity.class);
            } catch (Exception e) {
                file.delete();
                mainIntent = new Intent(NStart.this, Login.class);
            }
        } else
            mainIntent = new Intent(NStart.this, Login.class);
        NStart.this.startActivity(mainIntent);
        NStart.this.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
    }
    private boolean  isnetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo!=null){
            return activeNetworkInfo.isAvailable();
        }
        return false ;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what=1;
        message.obj =HtmlService.getsearchresult("https://www.lanzous.com/i9lp1vc","a");
        handler.sendMessage(message);
    }

    class MywebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            AlertDialog.Builder b2 = new AlertDialog.Builder(NStart.this)
                    .setTitle(R.string.app_name).setMessage(message)
                    .setPositiveButton("ok", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });


            b2.setCancelable(false);
            b2.create();
            b2.show();

            return true;
        }
    }

}