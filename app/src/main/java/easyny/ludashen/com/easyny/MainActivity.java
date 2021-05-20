package easyny.ludashen.com.easyny;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import easyny.ludashen.com.easyny.service.WidgetProvider;

import easyny.ludashen.com.easyny.util.DataCleanManager;
import easyny.ludashen.com.easyny.util.PasswordMag;
import easyny.ludashen.com.easyny.util.Tool;

//添加一个日历选择日期查看没周课程表
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static JSONObject userinfo;
    private static JSONObject user;
    TextView bt_mian, bt_kb, bt_cj;
    private Mains mainf;
    private Kcb kcbs;
    private Cjcx cjcx;
    private long exitTime=0;
    private PasswordMag openHelper;
    public static void User(JSONObject info) throws JSONException {
        userinfo = info;
        user = userinfo.getJSONObject("user");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        disableAPIDialog();
        setSupportActionBar(toolbar);
        openHelper = new PasswordMag(this);
        openHelper.getReadableDatabase();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        bt_mian = (TextView) this.findViewById(R.id.bt_main);
        bt_kb = (TextView) this.findViewById(R.id.bt_kb);
        bt_cj = (TextView) this.findViewById(R.id.bt_cj);
        bt_mian.setOnClickListener(this);
        bt_kb.setOnClickListener(this);
        bt_cj.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        TextView name = (TextView) headerLayout.findViewById(R.id.name);
        try {
            name.setText(user.getString("username")+"\n"+user.getString("userdwmc"));
        } catch (Exception e) {

        }
        bt_mian.performClick();
    }

    private void disableAPIDialog(){
        if (Build.VERSION.SDK_INT < 28)return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent set = new Intent(this, Set.class);
            startActivity(set);
            return true;
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("清除数据");
            builder.setMessage("此操作将把程序运行间的全部数据进行删除，而删除完成后系统会自动退出，此功能是为了保证客户数据安全，和程序数据异常的修复");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //清除内部缓存
                    DataCleanManager.cleanApplicationData(MainActivity.this);
                    Toast.makeText(MainActivity.this, "数据已经清空", Toast.LENGTH_LONG).show();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            builder.show();

            return true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent mainIntent = new Intent(MainActivity.this, Help.class);
            MainActivity.this.startActivity(mainIntent);
        } else if (id == R.id.nav_manage) {
            Intent mainIntent = new Intent(this, NyWeb.class);
            mainIntent.putExtra("url", "http://m.jspoo.com/");
            mainIntent.putExtra("js","document.getElementById(\"vxfc\").innerHTML=\"\"");
            startActivity(mainIntent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "大家好我正在由Ericlan(陆大哥)设计的app便利南宁，非常的good,我也推荐你们使用，你们记得也好评啊分享给好友，Ericlan,邮箱ludagewudi@gmail.com,软件下载链接下载链接" +
                    "https://www.lanzous.com/b0e7h4xlc");
            startActivity(Intent.createChooser(intent, "分享到"));
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] tos = {"ludagewudi@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, tos);
            intent.putExtra(Intent.EXTRA_TEXT, "陆大哥你好，我是你应用的忠实粉丝！我非常的喜欢你的应用。可是现在我在运行你的应用的时候发现了一个问题，现在我把问题反馈给你希望你看见我的信息。解决一下应用的问题");
            intent.putExtra(Intent.EXTRA_SUBJECT, "便利南院信息反馈");
            intent.setType("message/rfc882");
            startActivity(Intent.createChooser(intent, "选择发送邮件的应用"));
        }else if (id==R.id.setting){
            Intent set = new Intent(this, Set.class);
            startActivity(set);
        }else if(id==R.id.zhuti){
            Intent zhu = new Intent(this, ZhuTi.class);
            startActivity(zhu);
        }else if(id==R.id.fan){
            Cursor cursor;
            Intent web = new Intent(MainActivity.this, NyWeb.class);
            SQLiteDatabase r = openHelper.getWritableDatabase();
            String s = "select *from pass where name=?";
            web.putExtra("url","https://school.gczlsa.com/#/index");
            cursor = r.rawQuery(s,new String[]{"饭堂"});
            if (cursor.moveToFirst()) {
                String js = "javascript:document.getElementsByTagName('input')[0].value = '" +cursor.getString(1)+ "';document.getElementsByTagName('input')[1].value='" + cursor.getString(2) + "';" ;
                web.putExtra("js",js);
            }
            startActivity(web);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.bt_main:
                if (mainf == null) {
                    mainf = new Mains();
                    List<String> data = new ArrayList<>();
                    List<String> dic = new ArrayList<>();
                    List<String> time = new ArrayList<>();
                    try {
                        JSONArray coures = coures();
                        for (int i = 0; i < coures.length(); i++) {
                            JSONObject jsonObject = coures.getJSONObject(i);
                            String[] kcsj = jsonObject.getString("kcsj").split("0");
                            if (((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) + "").equals(kcsj[0])) {
                                data.add(jsonObject.getString("kcmc"));
                                dic.add("教室：" + jsonObject.getString("jsmc") + "\n上课时间：" + jsonObject.getString("kssj") + "——下课时间：" + jsonObject.getString("jssj"));
                                time.add(jsonObject.getString("kssj"));
                            }
                        }
                    } catch (Exception e) {}

                    String tem="";
                    int i=0;
                    for (String da:data){
                        tem+=da+"-"+dic.get(i)+"\n";
                        i++;
                    }

                    WidgetProvider.setData(tem);
                    Mains.data(data, dic, time);

                }
                transaction.replace(R.id.fth, mainf);
                //设置图片为选中状态
                bt_mian.setTextColor(Color.parseColor("#CC6633"));
                bt_cj.setTextColor(Color.BLACK);
                bt_kb.setTextColor(Color.BLACK);
                break;
            case R.id.bt_kb:
                if (kcbs == null) {
                    kcbs = new Kcb();
                    try {
                        List<String> datas = new ArrayList<>();
                        List<String> dics = new ArrayList<>();
                        List<String> days = new ArrayList<>();
                        JSONArray coures = coures();
                        for (int i = 0; i < coures.length(); i++) {
                            JSONObject jsonObject = coures.getJSONObject(i);
                            String[] kcsj = jsonObject.getString("kcsj").split("0");
                            datas.add(jsonObject.getString("kcmc"));
                            dics.add("教室：" + jsonObject.getString("jsmc") + "\n上课时间：" + jsonObject.getString("kssj") + "——下课时间：" + jsonObject.getString("jssj"));
                            days.add(kcsj[0]);
                        }
                        Kcb.data(datas, dics, days);
                    } catch (Exception e) {

                    }
                }
                transaction.replace(R.id.fth, kcbs);
                bt_mian.setTextColor(Color.BLACK);
                bt_kb.setTextColor(Color.parseColor("#CC6633"));
                bt_cj.setTextColor(Color.BLACK);
                break;
            case R.id.bt_cj:
                if (cjcx == null) {
                    cjcx = new Cjcx();
                }
                transaction.replace(R.id.fth, cjcx);
                bt_mian.setTextColor(Color.BLACK);
                bt_kb.setTextColor(Color.BLACK);
                bt_cj.setTextColor(Color.parseColor("#CC6633"));
                break;

        }
        transaction.commit();

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
                else
                    ck = new JSONArray(kcinfo(times));
            } else {
                ck = new JSONArray(kcinfo(times));
            }
        } catch (Exception e) {
            Tool.deleteFile(this, "kcb");
            Toast.makeText(this, "可能是秘钥过期了或者文件读取异常，退出在进来就没问题", Toast.LENGTH_SHORT).show();
        }
        return ck;
    }

    public String kcinfo(String times) throws JSONException {
        String getDay = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getCurrentTime&currDate="+times,userinfo.getString("token"));
        JSONObject day = new JSONObject(getDay);
        Tool.saveFile(this, "time", getDay);
        String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getKbcxAzc&xh="+user.getString("useraccount")+"&xnxqid="+day.getString("xnxqh")+"&zc="+day.getString("zc"), userinfo.getString("token"));
        String info = times + "@@" + getsearchresult;
        Tool.saveFile(this, "kcb", info);
        return getsearchresult;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
