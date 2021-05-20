package easyny.ludashen.com.easyny.notiutil;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;

import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.ui.PhotoPickerActivity;
import easyny.ludashen.com.easyny.util.Permission;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class NotiUtil extends AppCompatActivity implements View.OnClickListener {
    Button simple;
    Button pic;
    Button bigtext;
    Button mailbox;
    ImageButton picsrc;
    EditText tit;
    EditText con;
    EditText t1;
    EditText t2;
    EditText t3;
    EditText t4;
    EditText t5;
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    public static String[] permissionsREAD = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Bitmap bitmap;
    private FileInputStream fis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notiutil);
        simple = (Button) findViewById(R.id.simple);
        mailbox = (Button) findViewById(R.id.mailbox);
        bigtext = (Button) findViewById(R.id.bigtext);
        picsrc = (ImageButton) findViewById(R.id.picsrc);
        pic = (Button) findViewById(R.id.pic);
        tit = (EditText) findViewById(R.id.tit);
        con = (EditText) findViewById(R.id.con);
        t1 = (EditText) findViewById(R.id.t1);
        t2 = (EditText) findViewById(R.id.t2);
        t3 = (EditText) findViewById(R.id.t3);
        t4 = (EditText) findViewById(R.id.t4);
        t5 = (EditText) findViewById(R.id.t5);
        simple.setOnClickListener(this);
        mailbox.setOnClickListener(this);
        bigtext.setOnClickListener(this);
        pic.setOnClickListener(this);
        picsrc.setOnClickListener(this);
        NotifyUtil.init(getApplicationContext());

        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);

        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",NotiUtil.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);
        if (photoPaths.length > 0) {
            try {
                fis = new FileInputStream(photoPaths[0]);
                bitmap = BitmapFactory.decodeStream(fis);
                picsrc.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public void onClick(View view) {
        String tits = tit.getText().toString();
        String cons = con.getText().toString();
        if (tits.equals("") && cons.length() > 5) {
            Toast.makeText(this, "标题和内容不能太少", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.simple:
                NotifyUtil.buildSimple((int) (System.currentTimeMillis() / 1000), R.drawable.timg, tits, cons, null)
                        .setHeadup()
                        .addBtn(R.mipmap.ic_launcher, "取消", NotifyUtil.buildIntent(NotiUtil.class))
                        .addBtn(R.mipmap.ic_launcher, "确认", NotifyUtil.buildIntent(NotiUtil.class))
                        .show();

                break;
            case R.id.pic:
                if (bitmap == null) {
                    Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                NotifyUtil.buildBigPic((int) (System.currentTimeMillis() / 1000), R.drawable.timg, tits, cons, cons)
                        .setBitmap(bitmap)
                        .show();
                break;
            case R.id.bigtext:
                NotifyUtil.buildBigText((int) (System.currentTimeMillis() / 1000), R.drawable.timg, tits, cons).show();
                break;
            case R.id.mailbox:
                NotifyUtil.buildMailBox((int) (System.currentTimeMillis() / 1000), R.drawable.timg, tits)
                        .addMsg(t1.getText().toString())
                        .addMsg(t2.getText().toString())
                        .addMsg(t3.getText().toString())
                        .addMsg(t4.getText().toString())
                        .addMsg(t5.getText().toString())
                        .show();
                break;
            case R.id.picsrc:
                if (Permission.lacksPermissions(this, permissionsREAD)) {//读写权限没开启
                    ActivityCompat.requestPermissions(this, permissionsREAD, 0);
                    Toast.makeText(this, "你的读写权限没开启没，如果授权失败，请手动处理", Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(this, PhotoPickerActivity.class), 999);
                }
                break;
        }
    }

}
