package easyny.ludashen.com.easyny;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.yalantis.pulltomakesoup.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.util.Tool;


public class Kcb extends Fragment {
    private static List<String> data=new ArrayList<>();
    private static List<String> dis=new ArrayList<>();
    private static List<String> day=new ArrayList<>();
    private static JSONObject userinfo;
    private static JSONObject user;
    private ExpandingList expandingList;
    private Spinner xqxn;
    private ArrayAdapter<String> adapter;
    String xqn;
    String zcsb="1";
    private JSONArray jsonArray;
    private List<String> listc;
    private ExpandingItem item;
    private PullToRefreshView mPullToRefreshView;


    //    List<String> zcs;
    public static void data( List<String> datas,List<String> diss,List<String> times){
        data =datas;
        dis=diss;
        day=times;
    }
    public static void User(JSONObject info) throws JSONException {
        userinfo = info;
        user = userinfo.getJSONObject("user");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kcb,container, false);
        ThemeUtil.setBackgroud(getContext(),view);
        expandingList= (ExpandingList) view.findViewById(R.id.expanding_list_main);
        reUi();

       xqxn= (Spinner) view.findViewById(R.id.xqxn);
        try {
            if(Tool.fileExists(getContext(),"data")){
                jsonArray = new JSONArray(Tool.readinfo(getContext(),"data"));
                setAdapter(jsonArray);
            }else {
                String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getXnxq&xh=" + user.getString("useraccount"), userinfo.getString("token"));
                Tool.saveFile(getContext(),"data",getsearchresult);
                jsonArray = new JSONArray(getsearchresult);
                setAdapter(jsonArray);
            }
        } catch (Exception e) {
            Tool.deleteFile(getContext(),"data");
            Toast.makeText(getContext(),"可能是秘钥过期了或者文件读取异常，退出在进来就没问题"+Tool.fileExists(getContext(),"data"),Toast.LENGTH_SHORT).show();
        }


        final List<String> zcs = new ArrayList<>();
        for (int i=0;i<20;i++)
            zcs.add("第"+i+"周");
        final Spinner zc= (Spinner) view.findViewById(R.id.zc);
        ArrayAdapter<String> zcadapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, zcs);
        zcadapter.setDropDownViewResource(R.layout.spitem);
        zc.setAdapter(zcadapter);
        zc.setSelection(0, true);
        zc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    zcsb=String.valueOf(position);
                    kcinfo(xqn,zcsb);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            if(Tool.fileExists(getContext(),"time")){
                JSONObject day = new JSONObject(Tool.readinfo(getContext(),"time"));
                if (!day.getString("zc").equals("null")) {
                    zc.setSelection(Integer.parseInt(day.getString("zc")));
                    setdata(day.getString("xnxqh"));
                }else
                    Toast.makeText(getContext(),"当前属于假期，没有课程",Toast.LENGTH_SHORT).show();
            }else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String times = formatter.format(curDate);
                String getDay = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getCurrentTime&currDate="+times,userinfo.getString("token"));
                JSONObject day = new JSONObject(getDay);
                if (!day.getString("zc").equals("null")) {
                    zc.setSelection(Integer.parseInt(day.getString("zc")));
                    setdata(day.getString("xnxqh"));
                }else {
                    Toast.makeText(getContext(),"当前属于假期，没有课程",Toast.LENGTH_SHORT).show();
                }
                Tool.saveFile(getContext(), "time", getDay);

            }
        } catch (Exception e) {
            Tool.deleteFile(getContext(),"time");
            Toast.makeText(getContext(),"数据异常无法确定当前周数"+Tool.fileExists(getContext(),"data"),Toast.LENGTH_SHORT).show();

        }

        xqxn.setSelection(0, true);
        xqxn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xqn=listc.get(position);
                kcinfo(xqn,zcsb);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String times = formatter.format(curDate);
                    String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getXnxq&xh=" + user.getString("useraccount"), userinfo.getString("token"));
                    Tool.saveFile(getContext(),"data",getsearchresult);
                    String getDay = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getCurrentTime&currDate="+times,userinfo.getString("token"));
                    Tool.saveFile(getContext(), "time", getDay);
                    Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                    mPullToRefreshView.setRefreshing(false);
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    mPullToRefreshView.setRefreshing(false);
                }

            }
        });

        return view;
    }
    public void setAdapter(JSONArray jsonArray) throws JSONException {
        listc = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            listc.add(jsonObject.getString("xqmc"));
        }
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, listc);
        adapter.setDropDownViewResource(R.layout.spitem);
        xqn=listc.get(1);
        xqxn.setAdapter(adapter);
    }
    private void setdata(String xq){
        int i=0;
        for(String a:listc) {
            i++;
            if(xq.equals(a))
                break;
        }
        xqxn.setSelection(i);
    }
    public void setImate(String text,List<String> list,int res){
        int size=list.size();

        item = expandingList.createNewItem(R.layout.expanding_layout);
        TextView tite = (TextView) item.findViewById(R.id.title);
        tite.setText(text);
        item.createSubItems(size);
        for(int i=0;i<size;i++){
            String[] info=list.get(i).split("@@");
            View subItemZero = item.getSubItemView(i);
            ((TextView) subItemZero.findViewById(R.id.sub_title)).setText(info[0]);
            ((TextView) subItemZero.findViewById(R.id.sub_dex)).setText(info[1]);
        }
        item.setIndicatorIconRes(res);
    }

    public void reUi(){
        List<String> day1 = new ArrayList<>();
        List<String> day2=new ArrayList<>();
        List<String> day3=new ArrayList<>();
        List<String> day4=new ArrayList<>();
        List<String> day5=new ArrayList<>();
        List<String> day6=new ArrayList<>();
        List<String> day7=new ArrayList<>();
        for (int i=0;i<day.size();i++) {
            switch (day.get(i)) {
                case "1":
                    day1.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "2":
                    day2.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "3":
                    day3.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "4":
                    day4.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "5":
                    day5.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "6":
                    day6.add(data.get(i)+"@@"+dis.get(i));
                    break;
                case "7":
                    day7.add(data.get(i)+"@@"+dis.get(i));
                    break;

            }
        }
        if (day1.size()!=0)
            setImate("星期一",day1,R.drawable.wz1);
        if (day2.size()!=0)
            setImate("星期二",day2,R.drawable.wz2);
        if (day3.size()!=0)
            setImate("星期三",day3,R.drawable.wz3);
        if (day4.size()!=0)
            setImate("星期四",day4,R.drawable.wz4);
        if (day5.size()!=0)
            setImate("星期五",day5,R.drawable.wz5);
        if (day6.size()!=0)
            setImate("星期六",day6,R.drawable.wz6);
        if (day7.size()!=0)
            setImate("星期天",day7,R.drawable.wz7);
    }


    public void kcinfo(String xq,String vv){
        try {
            expandingList. removeAllViews();
            data.clear();
            dis.clear();
            day.clear();
            String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getKbcxAzc&xh=" + user.getString("useraccount") + "&xnxqid=" +xq+ "&zc=" + vv, userinfo.getString("token"));
            JSONArray coures = new JSONArray(getsearchresult);
            for (int i = 0; i < coures.length(); i++) {
                JSONObject jsonObject = coures.getJSONObject(i);
                String[] kcsj = jsonObject.getString("kcsj").split("0");
                data.add(jsonObject.getString("kcmc"));
                dis.add("教室：" + jsonObject.getString("jsmc") + "\n上课时间：" + jsonObject.getString("kssj") + "——下课时间：" + jsonObject.getString("jssj"));
                day.add(kcsj[0]);
            }
        } catch (Exception e) {
        }
        reUi();
    }

}