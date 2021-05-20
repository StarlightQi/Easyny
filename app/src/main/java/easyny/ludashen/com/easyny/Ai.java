package easyny.ludashen.com.easyny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import easyny.ludashen.com.easyny.Chat.Chat;
import easyny.ludashen.com.easyny.adapter.AiChannelAdapter;

import easyny.ludashen.com.easyny.util.Channel;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;


public class Ai extends AppCompatActivity {
    private GridView mGridView;  //九宫格
    private int[] channelImg;
    private ArrayList<Channel> channelList;
    public static String[] channelDec = new String[]{
                "为你写诗",
                "文字识别",
                "语读助手",
                "无聊语聊",
                "书荒",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.ai);
        initChannelView();
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ai.this.finish();
            }
        });
        Intent intent2=getIntent();
        if (intent2.getBooleanExtra("search", false)){
            titlebarView.setRightText("设置");
        }
        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ins=new Intent(Ai.this,Set.class);
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

        };
        channelList = new ArrayList<>();
        for(int i=0;i<channelDec.length;i++){
            Channel channel = new Channel();
            channel.setImgId(channelImg[i]);
            channel.setDec(channelDec[i]);
            channelList.add(channel);
        }

        mGridView.setAdapter(new AiChannelAdapter(channelList,this));
        //给九宫格设置监听器
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent web = new Intent(Ai.this, NyWeb.class);
                switch (position){
                    case 0:
                        web.putExtra("url","https://poem.msxiaobing.com/");
                        startActivity(web);
                        break;
                    case 1:
                        web.putExtra("url","http://www.ocrmaker.com/");
                        startActivity(web);
                        break;
                    case 2:
                        startActivy(YuYing.class);
                        break;
                    case 3:
                       startActivy(Chat.class);
                        break;
                    case 4:
                        web.putExtra("url","https://www.xuanpai.com/tool/shuhuangshenqi");
                        startActivity(web);
                        break;
                }
            }
        });

    }
    private void startActivy(Class<?> cls){
        Intent mainIntent = new Intent(this,cls);
        startActivity(mainIntent);
    }
}
