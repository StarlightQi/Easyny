package easyny.ludashen.com.easyny;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import easyny.ludashen.com.easyny.util.Permission;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.util.Tool;
import easyny.ludashen.com.easyny.view.TitlebarView;
import easyny.ludashen.com.easyny.view.editor.SEditorData;
import easyny.ludashen.com.easyny.view.editor.SEditorDataI;
import easyny.ludashen.com.easyny.view.editor.SortRichEditor;



public class YuYing extends AppCompatActivity {
    private TextToSpeech mSpeech;
    private Button btn,open;
    public SEditorDataI editor;
    private Switch duan;
    public static String[] permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.yuying);
        btn= (Button) findViewById(R.id.yudu);
        open= (Button) findViewById(R.id.open);
        duan=(Switch)findViewById(R.id.duan);
        editor = (SortRichEditor) findViewById(R.id.richEditor);
        Intent intent=getIntent();
        String duanxi = intent.getStringExtra("duanxi");
        editor.setText(duanxi);
        if (Tool.fileExists(this,"duan"))
            duan.setChecked(true);
        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("lanageTag", "not use");
                    } else {
                        btn.setEnabled(true);
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<SEditorData> editList = editor.buildEditData();
                mSpeech.speak(dealEditData(editList),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
        });

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
                startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Permission.lacksPermissions(YuYing.this,permissionsREAD)){//读写权限没开启
                    ActivityCompat.requestPermissions(YuYing.this,permissionsREAD,0);
                    Toast.makeText(YuYing.this,"你的读取权限没开启，如果授权失败，请手动处理",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.setType("text/plain");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                }
            }
        });

        duan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tool.saveFile(YuYing.this,"duan","");
                if (Permission.lacksPermissions(YuYing.this,permissionsREAD)){//读写权限没开启
                    ActivityCompat.requestPermissions(YuYing.this,permissionsREAD,0);
                    Toast.makeText(YuYing.this,"你的读取短信权限没开启，如果授权失败，请手动处理",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(YuYing.this,"技术原因请手动设置后台弹出界面权限",Toast.LENGTH_SHORT).show();
                   if(duan.isChecked())
                       Tool.saveFile(YuYing.this,"duan","");
                    else
                       Tool.deleteFile(YuYing.this,"duan");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            String fileName = null;
            Uri uri = data.getData();
            if (uri != null) {
                if (uri.toString().startsWith("file:")) {
                    fileName = uri.getPath();
                } else {
                    Cursor c = getContentResolver().query(uri, null, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        int id = c.getColumnIndex(MediaStore.Images.Media.DATA);
                        if (id != -1) {
                            fileName = c.getString(id);
                        }
                    }
                }
            }

            editor.setText(readText(new File(fileName)));
        }
    }

    private String readText(File file){
        String temp="";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while(line != null) {
                temp+=line+"\n";
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    private String dealEditData(List<SEditorData> editList) {
        StringBuffer str=new StringBuffer();
        for (SEditorData itemData : editList) {
            if (itemData.getInputStr() != null) {
                str.append(itemData.getInputStr());
            }
        }
        return str.toString();
    }
    @Override
    protected void onDestroy() {
        if (mSpeech != null) {
            mSpeech.stop();
            mSpeech.shutdown();
        }
        super.onDestroy();
    }
    }
