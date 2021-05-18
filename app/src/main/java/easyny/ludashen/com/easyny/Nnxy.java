package easyny.ludashen.com.easyny;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import easyny.ludashen.com.easyny.adapter.NnxyAdapter;
import easyny.ludashen.com.easyny.notiutil.NotiUtil;
import easyny.ludashen.com.easyny.service.MyService;
import easyny.ludashen.com.easyny.util.Channel;
import easyny.ludashen.com.easyny.util.PasswordMag;
import easyny.ludashen.com.easyny.util.Permission;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;


public class Nnxy extends AppCompatActivity {
    private GridView mGridView;
    private ArrayList<Channel> channelList;
    public static String[] permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private int[] channelImg;
    private PasswordMag openHelper;
    public static String[] channelDec = new String[]{
            "悬浮小助手",
            "趣通知栏",
            "事情办理",
            "南院VPN",
            "易班",
            "校园直播",
            "数字图书馆",
            "用电查询",
            "51CTO学院",
            "U校园",
            "iwrite",
            "超星学习",
            "菜鸟教程",
            "IT习题",
            "代码实践",
    };
//    通过登录后获取cookie然后为webview设置cookie从而实现自动登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.nnxy);

        openHelper = new PasswordMag(this);
        openHelper.getReadableDatabase();

        initChannelView();
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent2=getIntent();
        if (intent2.getBooleanExtra("search", false)){
            titlebarView.setRightText("设置");
        }
        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ins=new Intent(Nnxy.this,Set.class);
                startActivity(ins);
            }
        });
    }
    private void initChannelView(){
        mGridView = (GridView)findViewById(R.id.channel);
        channelImg = new int[]{
                R.id.grid_img1,
                R.id.grid_img2,
                R.id.grid_img3,
                R.id.grid_img4,
                R.id.grid_img5,
                R.id.grid_img6,
                R.id.grid_img7,
                R.id.grid_img8,
                R.id.grid_img9,
                R.id.grid_img10,
                R.id.grid_img11,
                R.id.grid_img12,
                R.id.grid_img13,
                R.id.grid_img14,
                R.id.grid_img15,
        };
        channelList = new ArrayList<>();
        for(int i=0;i<channelDec.length;i++){
            Channel channel = new Channel();
            channel.setImgId(channelImg[i]);
            channel.setDec(channelDec[i]);
            channelList.add(channel);
        }
        mGridView.setAdapter(new NnxyAdapter(channelList,this));
        //给九宫格设置监听器
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public Cursor cursor;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent web = new Intent(Nnxy.this, NyWeb.class);
                SQLiteDatabase r = openHelper.getWritableDatabase();
                String s = "select *from pass where name=?";
                switch (position){
                    case 0:
                        if (Permission.lacksPermissions(Nnxy.this, permissionsREAD)) {
                            ActivityCompat.requestPermissions(Nnxy.this, permissionsREAD, 0);
                        } else {
                            Intent start = new Intent(Nnxy.this, MyService.class);
                            startService(start);
                            Toast.makeText(Nnxy.this, "由于并非专业认识占时不提供更多服务,功能还在测试与完善中", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        Toast.makeText(Nnxy.this, "由于并非专业认识占时不提供更多服务,功能还在测试与完善中", Toast.LENGTH_SHORT).show();
                        Intent noti = new Intent(Nnxy.this, NotiUtil.class);
                        startActivity(noti);
                        break;
                    case 2:
                        //请假办事
//                        Cursor cursor = r.rawQuery(s,new String[]{"请假办事"});
//                        cursor.moveToFirst();
//                        cursor.getString(1);
                        web.putExtra("url","http://xgp.nnxy.cn/new/index.html");
                        startActivity(web);
                        break;
                    case 3:
                        cursor = r.rawQuery(s,new String[]{"南院VPN"});
                        boolean b = cursor.moveToFirst();
                        web.putExtra("url","https://vpn.nnxy.cn/");
                        if (b) {
                            String js = "javascript:document.getElementById('user_name').value = '" +cursor.getString(1)+ "';document.getElementsByTagName('input')[3].value='" + cursor.getString(2) + "';" ;
                            web.putExtra("js",js);
                        }
                        startActivity(web);
                        break;
                    case 4:
                        web.putExtra("url","http://www.yiban.cn/");
                        cursor = r.rawQuery(s,new String[]{"易班"});
                        if (cursor.moveToFirst()) {
                            String js = "javascript:document.getElementById('account-txt').value = '" +cursor.getString(1)+ "';document.getElementById('password-txt').value='" + cursor.getString(2) + "';" ;
                            web.putExtra("js",js);
                        }
                        startActivity(web);
                        break;
                    case 5:
                        web.putExtra("url","http://live.nnxy.cn/show");
                        startActivity(web);
                        break;
                    case 6:
                        web.putExtra("url","http://lib.nnxy.cn/");
                        startActivity(web);
                        break;
                    case 7:
                        web.putExtra("url","https://vpn.nnxy.cn/http/77726476706e69737468656265737421a1ae13d27666301e2c58d9e2c90572/?wrdrecordvisit=record");
                        startActivity(web);
                        break;
                    case 8:
                        web.putExtra("url","https://vpn.nnxy.cn/http/77726476706e69737468656265737421f5ba4d9926226659700fc7f9c956373a237b6432/?wrdrecordvisit=record");
                        startActivity(web);
                        break;
                    case 9:
                        web.putExtra("url","https://u.unipus.cn/user/student/");
                        cursor = r.rawQuery(s,new String[]{"U校园"});
                        if (cursor.moveToFirst()) {
                            String js = "javascript:document.getElementsByTagName('input')[0].value = '" +cursor.getString(1)+ "';document.getElementsByTagName('input')[1].value='" + cursor.getString(2) + "';" ;
                            web.putExtra("js",js);
                        }
                        startActivity(web);
                        break;
                    case 10:
                        cursor = r.rawQuery(s,new String[]{"U校园"});
                        if (cursor.moveToFirst()) {
                            String js = "javascript:document.getElementsByTagName('input')[0].value = '" +cursor.getString(1)+ "';document.getElementsByTagName('input')[1].value='" + cursor.getString(2) + "';" ;
                            web.putExtra("js",js);
                        }
                        web.putExtra("url","https://sso.unipus.cn/sso/login?service=http://iwrite.unipus.cn/system/login");
                        startActivity(web);
                        break;
                    case 11:
                        web.putExtra("url","https://passport2.chaoxing.com/mlogin?fid=1682&refer=http://i.mooc.chaoxing.com");
                        web.putExtra("js","javascript:document.getElementById('unameId').value = '" +cursor.getString(1)+ "';document.getElementById('passwordId').value='" + cursor.getString(2) + "';document.getElementById(\"notice_box_0x004\").innerHTML=\"<a style='color: blue;text-decoration:underline' href='mailto:ludagewudi@gmail.com?subject=我想对陆大哥说&body=我在用的的便利南院app做得很好，陆大哥我很崇拜你呢！！'>技术：陆大哥  邮箱:ludagewudi@gmail.com</a>\"");
                        startActivity(web);
                        break;
                    case 12:
                        web.putExtra("url","https://www.runoob.com/");
                        startActivity(web);
                        break;
                    case 13:
                        web.putExtra("url","https://nanti.jisuanke.com/oi");
                        startActivity(web);
                        break;
                    case 14:
                        web.putExtra("url","https://www.codewars.com/");
                        startActivity(web);
                        break;
                }
            }
        });
    }
}
