package easyny.ludashen.com.easyny;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easyny.ludashen.com.easyny.ui.TextInput;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;


public class DiaryList extends AppCompatActivity {
    private ListView list_diary;
    private List<String> abs;
    private static final File BIJIS = new File(
            Environment.getExternalStorageDirectory() + "/便利笔记");
    private List<String> vecFile;
    private int NEWOPEN=333;
    private boolean set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.diary);
        abs=new ArrayList<>();
        vecFile = new ArrayList<>();
        getFileName();
        Toast.makeText(this,"长按可以删除哦",Toast.LENGTH_SHORT).show();


        list_diary= (ListView) findViewById(R.id.list_diary);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(DiaryList.this, android.R.layout.simple_list_item_1,vecFile);
        list_diary.setAdapter(adapter);
        list_diary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(DiaryList.this, TextInput.class);
                mainIntent.putExtra("openPath",abs.get(position));
                mainIntent.putExtra("title",vecFile.get(position));
                startActivityForResult(mainIntent,NEWOPEN);
            }
        });

//        长按删除
        list_diary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(DiaryList.this);
                builder.setTitle("提示");
                builder.setMessage("是否删除名字为："+vecFile.get(position));
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            new File(abs.get(position)).delete();
                            Toast.makeText(DiaryList.this,"删除成功",Toast.LENGTH_SHORT).show();
                            reLoad();
                        }catch (Exception e)
                        {
                            Toast.makeText(DiaryList.this,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(DiaryList.this, TextInput.class);
                startActivityForResult(mainIntent,NEWOPEN);
            }
        });
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent2=getIntent();
        set = intent2.getBooleanExtra("search", false);
        if (set){
            titlebarView.setRightText("设置");
        }
        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ins=new Intent(DiaryList.this,Set.class);
                startActivity(ins);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        reLoad();
    }
    private void reLoad(){
        vecFile.clear();
        abs.clear();
        getFileName();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(DiaryList.this, android.R.layout.simple_list_item_1,vecFile);
        list_diary.setAdapter(adapter);
    }
    public void getFileName() {
        if (!BIJIS.exists())
            BIJIS.mkdirs();
        File file = new File(BIJIS.getAbsolutePath());
        File[] subFile = file.listFiles();
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                abs.add(subFile[iFileLength].getAbsolutePath());
                vecFile.add(filename.split("\\.")[0]);
            }
        }

    }

}