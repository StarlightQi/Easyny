package easyny.ludashen.com.easyny.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.util.Permission;
import easyny.ludashen.com.easyny.view.editor.SEditorData;
import easyny.ludashen.com.easyny.view.editor.SortRichEditor;


public class TextInput extends AppCompatActivity implements View.OnClickListener{
    private TextToSpeech mSpeech;
    public static final int REQUEST_CODE_PICK_IMAGE = 1023;
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private static final File BIJI = new File(
            Environment.getExternalStorageDirectory() + "/便利笔记");
    private TextView tvSort;
    private SortRichEditor editor;
    private ImageView ivGallery, ivCamera;
    private Boolean openStatus=false;
    private Button btnPosts;
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    private String name;
    public static String[] permissionsREAD={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.textinput);
        tvSort = (TextView) findViewById(R.id.tv_sort);
        TextView back = (TextView) findViewById(R.id.back);
        editor = (SortRichEditor) findViewById(R.id.richEditor);
        ivGallery = (ImageView) findViewById(R.id.iv_gallery);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        btnPosts = (Button) findViewById(R.id.btn_posts);
        Button btn=(Button)findViewById(R.id.read) ;
        tvSort.setOnClickListener(this);
        ivGallery.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        btnPosts.setOnClickListener(this);
        back.setOnClickListener(this);
        Intent getPath=getIntent();
        String openPath = getPath.getStringExtra("openPath");
        name = getPath.getStringExtra("title");
        if(openPath!=null) {
            File file = new File(openPath);
            readText(file);
            editor.setTitle(name);
            openStatus=true;
        }

        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("lanageTag", "not use");
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


    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private void readText(File file){
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

        List<String> l=new ArrayList<>();
        String[] s=temp.split("##");
        int i=0;
        for(String ss:s){
            if(ss.endsWith(".jpg")||ss.endsWith(".png")||ss.endsWith(".jpeg")) {
                editor.insertImageViewAtIndex(i, new File(ss).getAbsolutePath(),true);
            }else {
                editor.insertEditTextAtIndex(i,ss);
            }
            i++;
        }
    }

    private String dealEditData(List<SEditorData> editList) {
        final StringBuffer str=new StringBuffer();
        StringBuffer read=new StringBuffer();
        for (SEditorData itemData : editList) {
            if (itemData.getInputStr() != null) {
                str.append(itemData.getInputStr());
                read.append(itemData.getInputStr());
            } else if (itemData.getImagePath() != null) {
                str.append( "##" + itemData.getImagePath()+"##");
                read.append("图片");
            }
        }
        try {
            String titleData = editor.getTitleData();
            if(titleData!=null&& !titleData.trim().equals("")) {
                BIJI.mkdirs();
                final File file=new File(BIJI, titleData + ".txt");
                if(file.exists()&&!openStatus){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("同名");
                    builder.setMessage("提示你新建的文本在列表中有同名的，请问是否覆盖源文件？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            try {
                                save(file,str);
                            } catch (Exception e) {

                            }
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                }else {
                    save(file,str);
                }
                if(!titleData.equals(name)&&openStatus) {
                    new File(BIJI, name + ".txt").delete();
                }
            }
            else {
                Toast.makeText(this, "名字不能为空", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this,"保存失败",Toast.LENGTH_LONG).show();
        }

        return read.toString();

    }

    public void save(File file,StringBuffer str) throws Exception {
        FileOutputStream outputStream;
        outputStream = new FileOutputStream(file);
        outputStream.write(str.toString().getBytes());
        outputStream.close();
        Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
    }
    private void openCamera() {
        try {
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

//    接收意图插入图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (editor.isSort()) tvSort.setText("排序");
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);
            editor.addImageArray(photoPaths);
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            editor.addImage(mCurrentPhotoFile.getAbsolutePath());
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sort:
                if (editor.sort()) {
                    tvSort.setText("完成");
                } else {
                    tvSort.setText("排序");
                }
                break;
            case R.id.iv_gallery:
                if (Permission.lacksPermissions(this,permissionsREAD)){//读写权限没开启
                    ActivityCompat.requestPermissions(this,permissionsREAD,0);
                    Toast.makeText(this,"你的读写权限没开启没，如果授权失败，请手动处理",Toast.LENGTH_SHORT).show();
                }else {
                    startActivityForResult(new Intent(this, PhotoPickerActivity.class), REQUEST_CODE_PICK_IMAGE);
                }
                break;
            case R.id.iv_camera:
                if (Permission.lacksPermissions(this,permissionsREAD)){//读写权限没开启
                    ActivityCompat.requestPermissions(this,permissionsREAD,0);
                    Toast.makeText(this,"你的相机权限没有给，如果授权失败，请手动处理",Toast.LENGTH_SHORT).show();
                }else {
                    openCamera();
                }
                break;
            case R.id.btn_posts:
                List<SEditorData> editList = editor.buildEditData();
                dealEditData(editList);
                break;
            case R.id.back:
                finish();
                break;
        }
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
