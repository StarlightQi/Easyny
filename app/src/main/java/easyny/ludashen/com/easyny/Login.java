package easyny.ludashen.com.easyny;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import easyny.ludashen.com.easyny.util.Tool;
import easyny.ludashen.com.easyny.view.TitlebarView;
public class Login extends AppCompatActivity{
    String fn;
    ImageView spit;
    int a=0;
    int img[]={R.drawable.wo2,R.drawable.usr0,R.drawable.usr1,R.drawable.usr2};
    private Handler handler=new Handler() {
        public void handleMessage(android.os.Message msg) {
            spit.setImageResource((Integer) msg.obj);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        spit= (ImageView) findViewById(R.id.sipt);
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                Message message = new Message();
                if(a<img.length){

                    message.obj =img[a++];
                    handler.sendMessage(message);
                }else {
                    a=0;
                    message.obj =img[a];
                    handler.sendMessage(message);
                }
            }
        };
        timer.schedule(task,0,5000);
        TextView bei= (TextView) findViewById(R.id.textView4);
       bei.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent ban = new Intent(Login.this, NyWeb.class);
               ban.putExtra("url", "http://106.13.122.53/ludage/");
               startActivity(ban);
           }
       });
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);

        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                builder.setTitle("跳过");
                builder.setMessage("温馨提示跳过后不享有查询成绩，查询课表和课表的服务哦！");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent mainIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(mainIntent);
                        Login.this.finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });

    }

    public void login(View v) throws Exception {
        EditText hx = (EditText) findViewById(R.id.editText);
        EditText pw = (EditText) findViewById(R.id.editText2);
        CheckBox  lo = (CheckBox) findViewById(R.id.checkBox);
        if (!hx.getText().toString().equals("") && !pw.getText().toString().equals("")) {
            fn = HtmlService.checkuser(hx.getText().toString(), pw.getText().toString());
        if (fn.contains("username")) {
            if (lo.isChecked()) {
                String info = hx.getText().toString()+"##"+ pw.getText().toString();
                Tool.saveFile(Login.this,"info.txt" ,info);
            } else {
                Tool.deleteFile(Login.this,"info.txt");
            }
            JSONObject info = new JSONObject(fn);
            Cjcx.User(info);
            MainActivity.User(info);
            Kcb.User(new JSONObject(fn));
            Intent mainIntent = new Intent(Login.this, MainActivity.class);
            Login.this.startActivity(mainIntent);
            Login.this.finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
        } else {
            Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
        }
        }
        else {
            Toast.makeText(Login.this, "账号密码不能为空", Toast.LENGTH_LONG).show();
        }
        }


}
