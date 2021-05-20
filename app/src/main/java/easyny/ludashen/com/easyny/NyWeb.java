package easyny.ludashen.com.easyny;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.view.KeyEvent;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;

import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import easyny.ludashen.com.easyny.searchbox.SearchFragment;
import easyny.ludashen.com.easyny.searchbox.custom.IOnSearchClickListener;
import easyny.ludashen.com.easyny.translate.TransApi;
import easyny.ludashen.com.easyny.util.MyClickListener;
import easyny.ludashen.com.easyny.util.PasswordMag;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;

@SuppressLint("SetJavaScriptEnabled")
public class NyWeb extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    private static final int REQUEST_CODE_ALBUM = 0x01;
    private static final int REQUEST_CODE_CAMERA = 0x02;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 0x03;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private String mCurrentPhotoPath;
    private String mLastPhothPath;
    private Thread mThread;
    private WebView mWebView;
    private PasswordMag openHelper;
    private String url = "https://www.baidu.com/";
    private TextToSpeech mSpeech;
    String js;
    private TitlebarView titlebarView;
    private static String APP_ID;
    private static String SECURITY_KEY;
    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private String htmls = "";
    private boolean search = false;
    private TextView zh;
    private TransApi api;
    private ProgressBar progressBar;
    private long exitTime = 0;
    private TextView en;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.nyweb);
        mWebView = (WebView) findViewById(R.id.web);
        progressBar = (ProgressBar) findViewById(R.id.index_progressBar);

        Intent intent2 = getIntent();
        url = intent2.getStringExtra("url");
        js = intent2.getStringExtra("js");
        search = intent2.getBooleanExtra("search", false);
        init();
        WebSettings settings = mWebView.getSettings();
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
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");

        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    } else {
                    }
                }
            }
        });


        mWebView.setWebViewClient(new WebViewClient() {

            @SuppressLint("NewApi")
            @Override
            public void onPageFinished(WebView view, String url) {

                view.loadUrl("javascript:window.java_obj.getSource(document.getElementsByTagName('html')[0].innerHTML);");
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT >= 19) {
                    view.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                        }
                    });
                } else {
                    view.loadUrl(js);
                }
                titlebarView.setTitle(mWebView.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressBar.setVisibility(View.VISIBLE);
                htmls = "";
                if (url == null) return false;
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NyWeb.this);
                builder.setTitle("下载");
                builder.setMessage("当前有文件需要下载，是否进行下载？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                uploadMessageAboveL = filePathCallback;
                uploadPicture();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(NyWeb.this);
                frameLayout.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                uploadPicture();
            }

        });


    }


    @Override
    public void onBackPressed() {
        /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
        if (customView != null) {
            hideCustomView();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {

        //初始化标题栏
        titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        //右边语音阅读
        titlebarView.setOnClickRightImg(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpeech.isSpeaking()) {
                    mSpeech.stop();
                    titlebarView.setRightDrawable(R.drawable.read);
                } else {
                    if (htmls.length() > 5) {
                        mSpeech.speak(htmls, TextToSpeech.QUEUE_FLUSH, null);
                        titlebarView.setRightDrawable(R.drawable.reading);
                    } else
                        Toast.makeText(NyWeb.this, "现在没有发现文字请等待网页加载完毕", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //点击标题刷新
        titlebarView.setOnClickTwoTitle(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                mWebView.reload();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doubleClick() {
                mWebView.goForward();
            }
        }));

        if (search) {
            final RelativeLayout trans = (RelativeLayout) findViewById(R.id.trans);
            zh = (TextView) findViewById(R.id.zh);
            en = (TextView) findViewById(R.id.en);
            ImageButton close = (ImageButton) findViewById(R.id.close);

            titlebarView.setRightsDrawable(R.drawable.ic_search_white_24dp);
            titlebarView.setRightText("设置");
            titlebarView.setLeftText("翻译");
            titlebarView.setLeftDrawable(R.drawable.ic_back_24dp);

            openHelper = new PasswordMag(this);
            openHelper.getReadableDatabase();

            SQLiteDatabase r = openHelper.getWritableDatabase();
            String s = "select *from pass where name=?";
            Cursor cursor = r.rawQuery(s, new String[]{"百度翻译秘钥"});
            final boolean b = cursor.moveToFirst();
            if (b) {
                APP_ID = cursor.getString(1);
                SECURITY_KEY = cursor.getString(2);
                api = new TransApi(APP_ID, SECURITY_KEY);
            }

            titlebarView.setOnClickRightText(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ins = new Intent(NyWeb.this, Set.class);
                    startActivity(ins);
                }
            });
            //搜索框
            titlebarView.setOnClickSearch(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchFragment searchFragment = SearchFragment.newInstance();
                    searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
                        @Override
                        public void OnSearchClick(String keyword) {

                            if (Pattern.matches("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", keyword)){
                                mWebView.loadUrl(keyword);
//                                Toast.makeText(getApplication(),"加载连接")
                            }

                            else
                                mWebView.loadUrl("https://www.baidu.com/s?wd=" + keyword);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                    searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);
                }
            });

            //左标图标返回上一页
            titlebarView.setOnClickLeftImg(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWebView.canGoBack())
                        mWebView.goBack();
                    else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NyWeb.this);
                        builder.setTitle("退出");
                        builder.setMessage("是否退出？");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                NyWeb.this.finish();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                }
            });

            //左边文字翻译
            titlebarView.setOnClickLeftText(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (b) {
                        if (htmls.length() > 5) {
                            zh.setText(htmls);
                            trans.bringToFront();
                            Toast.makeText(NyWeb.this, "请手动对数据进行修改不然可能翻译错误！", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(NyWeb.this, "请耐心等你网页加我完成", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(NyWeb.this, "如果需要此功能，请到设置中添加百度翻译秘钥", Toast.LENGTH_SHORT).show();
                }
            });
            //关闭翻译图层
            close.setOnClickListener(this);
            ImageButton tran = (ImageButton) findViewById(R.id.tran);
            tran.setOnClickListener(this);
            ImageButton clear = (ImageButton) findViewById(R.id.clear);
            clear.setOnClickListener(this);

        } else {
            titlebarView.setRightText("语音");
            titlebarView.setLeftText("关闭");
            titlebarView.setOnClickLeftText(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NyWeb.this.finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults == null && grantResults.length == 0) {
            return;
        }
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
                new AlertDialog.Builder(mContext)
                        .setTitle("无法拍照")
                        .setMessage("您未授予拍照权限")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent localIntent = new Intent();
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(localIntent);
                            }
                        }).create().show();
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            takePhoto();
        }
    };

    private String getTrans(String text) throws Exception {
        String transResult = api.getTransResult(text, "auto", "auto");
        if (transResult == "" || transResult == null) {
            Toast.makeText(NyWeb.this, "翻译失败", Toast.LENGTH_SHORT).show();
            return "翻译失败了";
        }
        JSONObject jsonObject = new JSONObject(transResult);
        JSONArray trans_result = jsonObject.getJSONArray("trans_result");
        StringBuffer afterText = new StringBuffer();
        for (int i = 0; i < trans_result.length(); i++) {
            JSONObject jo = trans_result.optJSONObject(i);
            afterText.append(jo.getString("dst"));
        }
        return afterText.toString();
    }

    /**
     * 选择相机或者相册
     */
    public void uploadPicture() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NyWeb.this);
        builder.setTitle("请选择图片上传方式");

        //取消对话框
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                //一定要返回null,否则<input type='file'>
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;

                }
            }
        });
        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(mLastPhothPath)) {
                    //上一张拍照的图片删除
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            File file = new File(mLastPhothPath);
                            if (file != null) {
                                file.delete();
                            }
                            mHandler.sendEmptyMessage(1);
                        }
                    });
                    mThread.start();
                } else {
                    //请求拍照权限
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        ActivityCompat.requestPermissions(NyWeb.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
                    }
                }
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                chooseAlbumPic();
            }
        });
        builder.create().show();

    }

    /**
     * 拍照
     */
    private void takePhoto() {

        StringBuilder fileName = new StringBuilder();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileName.append(UUID.randomUUID()).append("_upload.png");
        File tempFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mCurrentPhotoPath = tempFile.getAbsolutePath();
        startActivityForResult(intent, REQUEST_CODE_CAMERA);


    }

    /**
     * 选择相册照片
     */
    private void chooseAlbumPic() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE_ALBUM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ALBUM || requestCode == REQUEST_CODE_CAMERA) {

            if (uploadMessage == null && uploadMessageAboveL == null) {
                return;
            }

            //取消拍照或者图片选择时
            if (resultCode != RESULT_OK) {
                //一定要返回null,否则<input file> 就是没有反应
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;

                }
            }

            //拍照成功和选取照片时
            if (resultCode == RESULT_OK) {
                Uri imageUri = null;

                switch (requestCode) {
                    case REQUEST_CODE_ALBUM:

                        if (data != null) {
                            imageUri = data.getData();
                        }

                        break;
                    case REQUEST_CODE_CAMERA:

                        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
                            File file = new File(mCurrentPhotoPath);
                            Uri localUri = Uri.fromFile(file);
                            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                            sendBroadcast(localIntent);
                            imageUri = Uri.fromFile(file);
                            mLastPhothPath = mCurrentPhotoPath;
                        }
                        break;
                }


                //上传文件
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(imageUri);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(new Uri[]{imageUri});
                    uploadMessageAboveL = null;

                }

            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.reload();
    }


    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        NyWeb.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(NyWeb.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不播放时竖屏

    }

    @Override
    public void onClick(View v) {
        if (search) {
            switch (v.getId()) {
                case R.id.close:
                    mWebView.bringToFront();
                    break;
                case R.id.tran:
                    try {
                        en.setText(getTrans(zh.getText().toString()));
                    } catch (Exception e) {
                        Toast.makeText(NyWeb.this, "数据异常可能你的百度秘钥有误，请检查", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.clear:
                    zh.setText("");
                    en.setText("");
                    break;
            }
        }
    }


    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 逻辑处理
     *
     * @author linzewu
     */
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Document parse = Jsoup.parse(html);
            htmls = parse.body().text();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            } else {
                if (search)
                    exit();
                else
                    finish();

            }


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