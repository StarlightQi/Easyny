package easyny.ludashen.com.easyny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import easyny.ludashen.com.easyny.util.Channel;
import easyny.ludashen.com.easyny.adapter.ChannelAdapter;
import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;


public class Yule extends AppCompatActivity {
    private GridView mGridView;  //九宫格
    private int[] channelImg;
    private ArrayList<Channel> channelList;
    public static String[] channelDec = new String[]{
            "神奇的单词",
            "免费小说阅读",
            "VR全景",
            "魂斗罗",
            "超级玛丽",
            "热血格斗",
            "拳王",
            "流行语查询",
            "酷狗音乐",
            "电子书搜索",
            "临时文件分享",
            "装逼神器",
            "毒鸡汤",
            "梨视频",
            "黑米影院",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.yule);
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
                Intent ins=new Intent(Yule.this,Set.class);
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

        mGridView.setAdapter(new ChannelAdapter(channelList,this));
        //给九宫格设置监听器
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent web = new Intent(Yule.this, NyWeb.class);
                switch (position){
                    case 0:
                        web.putExtra("url","http://106.13.122.53/ludage/");
                        startActivity(web);
                        break;
                    case 1:
                        web.putExtra("url","https://m.biquge5200.com/");
                        startActivity(web);
                        break;
                    case 2:
                        web.putExtra("url","https://720yun.com/");
                        startActivity(web);
                        break;
                    case 3:
                        web.putExtra("url","https://www.yikm.net/play?id=4137&n=L2Zjcm9tL3NqL0NvbnRyYSAoVSkgWyFdLm5lcw==&t=%E9%AD%82%E6%96%97%E7%BD%97");
                        startActivity(web);
                        break;
                    case 4:
                        web.putExtra("url","https://www.yikm.net/play?id=3501&n=L2Zjcm9tL2R6bXgvU3VwZXIgTWFyaW8gQnJvcy4gKFcpIFshXS5uZXM=&t=%E8%B6%85%E7%BA%A7%E9%A9%AC%E9%87%8C%E5%A5%A5");
                        startActivity(web);
                        break;
                    case 5:
                        web.putExtra("url","https://www.yikm.net/play?id=4714&n=L2Zjcm9tL3lkYnMvTmVra2V0c3UgS2FrdXRvdSBEZW5zZXRzdSAoSikgWyFdLm5lcw==&t=%E7%83%AD%E8%A1%80%E6%A0%BC%E6%96%97");
                        startActivity(web);
                        break;
                    case 6:
                        web.putExtra("url","https://www.yikm.net/play?id=5324&n=a29mOTUuemlw&t=%E6%8B%B3%E7%9A%8795&ac=4&l=snk-neo-geo");
                        startActivity(web);
                        break;
                    case 7:
                        web.putExtra("url","https://jikipedia.com/");
                        startActivity(web);
                        break;
                    case 8:
                        web.putExtra("url","http://m.kugou.com/");
                        web.putExtra("js","document.getElementById(\"tcdownload\").innerHTML=\"感谢遇到你\"");
                        startActivity(web);
                        break;
                    case 9:
                        web.putExtra("url","https://www.jiumodiary.com/");
                        startActivity(web);
                        break;
                    case 10:
                        web.putExtra("url","https://send.eyhn.cloud/");
                        startActivity(web);
                        break;
                    case 11:
                        web.putExtra("url","http://wx.zemuo.com/");
                        web.putExtra("js","document.getElementsByClassName('footer')[0].innerHTML=\"<span style='color: blue'>陆大哥也很喜欢装逼哦，哈哈哈哈</span>\";");
                        startActivity(web);
                        break;
                    case 12:
                        web.putExtra("url","http://www.nows.fun/");
                        web.putExtra("js","document.getElementsByClassName('btn-xs')[1].innerHTML=\"<a class='btn btn-primary btn-filled btn-xs' href='http://www.nows.fun'>下一句</a>\";");
                        startActivity(web);
                        break;
                    case 13:
                        web.putExtra("url","https://www.pearvideo.com/?from=intro");
                        startActivity(web);
                        break;
                    case 14:
                        web.putExtra("url","https://www.tv432.com/");
                        web.putExtra("js","document.getElementsByClassName('hide')[0].innerHTML='';");
                        startActivity(web);
                        break;

                }
            }
        });
    }
}

