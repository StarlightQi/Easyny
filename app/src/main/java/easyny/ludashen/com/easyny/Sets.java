package easyny.ludashen.com.easyny;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import easyny.ludashen.com.easyny.util.DataCleanManager;
import easyny.ludashen.com.easyny.util.Tool;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class Sets extends Activity implements View.OnClickListener {
    private RadioGroup chose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sets);
        Button yuying= (Button) findViewById(R.id.yuyin);
        yuying.setOnClickListener(this);
        Button prm= (Button) findViewById(R.id.prm);
        prm.setOnClickListener(this);
        Button tapi= (Button) findViewById(R.id.tapi);
        tapi.setOnClickListener(this);
        Button shuju= (Button) findViewById(R.id.shuju);
        shuju.setOnClickListener(this);
        chose= (RadioGroup) findViewById(R.id.chose);
        Button save= (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        Button zhuti= (Button) findViewById(R.id.zhuti);
        zhuti.setOnClickListener(this);
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Tool.fileExists(this,"chose")) {
            String ch = Tool.readinfo(this, "chose").trim();
            switch (ch) {
                case "0":
                    chose.check(R.id.r0);
                case "1":
                    chose.check(R.id.r1);
                    break;
                case "2":
                    chose.check(R.id.r2);
                    break;
                case "3":
                    chose.check(R.id.r3);
                    break;
                case "4":
                    chose.check(R.id.r4);
                    break;
                case "5":
                    chose.check(R.id.r5);
                    break;
                case "6":
                    chose.check(R.id.r6);
                    break;
                case "7":
                    chose.check(R.id.r7);
                    break;
                case "8":
                    chose.check(R.id.r8);
                    break;
                case "9":
                    chose.check(R.id.r9);
                    break;
                case "10":
                    chose.check(R.id.r10);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yuyin:
                startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
                break;
            case R.id.prm:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.save:
                String con="1";
                int cs = chose.getCheckedRadioButtonId();
                switch (cs){
                    case R.id.r0:
                        con="0";
                        break;
                    case R.id.r1:
                        con="1";
                        break;
                    case R.id.r2:
                        con="2";
                        break;
                    case R.id.r3:
                        con="3";
                        break;
                    case R.id.r4:
                        con="4";
                        break;
                    case R.id.r5:
                        con="5";
                        break;
                    case R.id.r6:
                        con="6";
                        break;
                    case R.id.r7:
                        con="7";
                        break;
                    case R.id.r8:
                        con="8";
                        break;
                    case R.id.r9:
                        con="9";
                        break;
                    case R.id.r10:
                        con="10";
                        break;
                }
                Tool.saveFile(this,"chose",con);
                break;
            case R.id.tapi:
                Intent api=new Intent(this,NyWeb.class);
                api.putExtra("url","https://api.fanyi.baidu.com/api/trans/product/index");
                startActivity(api);
                break;
            case R.id.shuju:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("清除数据");
                builder.setMessage("此操作将把程序运行间的全部数据进行删除，而删除完成后系统会自动退出，此功能是为了保证客户数据安全，和程序数据异常的修复");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DataCleanManager.cleanApplicationData(Sets.this);
                        Toast.makeText(Sets.this, "数据已经清空", Toast.LENGTH_LONG).show();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;

            case R.id.zhuti:
                Intent zhuti=new Intent(this,ZhuTi.class);
                startActivity(zhuti);
                break;
        }
    }
}