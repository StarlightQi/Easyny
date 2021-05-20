package easyny.ludashen.com.easyny;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class Help extends AppCompatActivity {
    private TextView help;
    private String words="便利南院，由南宁学院热心校友陆大哥花费巨力打造，只为造福广大南宁学院校友，也希望这个APP能给你们提供了良好和便捷的服务。同时也在一定程度上获得大家的一致好评\n" +
            "\n本系统的所有API均由强智科技提供——对接学校教务系统，如有信息错误，与开发如有无关\n" +
            "本系统为免费系统，本APP进制对外有收费行为，如有收费行为情与开发人员无关——Ericlan（陆大哥） ";

    private Handler handler=new Handler() {
        public void handleMessage(android.os.Message msg) {
            help.setText((String) msg.obj);
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.help);
        help = (TextView) findViewById(R.id.helpText);
        Thread syncTask = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < words.length(); i++) {
                        String substring = words.substring(0, i);
                        Message message = new Message();
                        message.obj = substring;
                        handler.sendMessage(message);
                        Thread.sleep(90);
                    }
                } catch (Exception e) {
                }
            }
        };
        syncTask.start();

        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void help(View view){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.lanzous.com/b0e7h4xlc"));
        startActivity(intent);

        Toast.makeText(Help.this,"你可以在该链接中下载最新版本，当前版本为V0.0.1",Toast.LENGTH_SHORT).show();
    }
}