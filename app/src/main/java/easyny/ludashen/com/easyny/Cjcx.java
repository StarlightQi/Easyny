package easyny.ludashen.com.easyny;

import android.app.Fragment;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.pulltomakesoup.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;

import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.util.Tool;


public class Cjcx extends Fragment implements  AdapterView.OnItemSelectedListener {
    private static JSONObject userinfo;
    private static JSONObject user;
    private Spinner xqsz;
    private ArrayAdapter<String> adapter;
    private TextView cx;
    private PullToRefreshView mPullToRefreshView;

    public static void User(JSONObject info) throws JSONException {
            userinfo = info;
            user = userinfo.getJSONObject("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cjcx, container, false);
        ThemeUtil.setBackgroud(getContext(),view);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        //加载日期
        xqsz= (Spinner) view.findViewById(R.id.spinner1);
        cx= (TextView) view.findViewById(R.id.cj);
        try {
        if(Tool.fileExists(getContext(),"data")){
            JSONArray jsonArray = new JSONArray(Tool.readinfo(getContext(),"data"));
            setAdapter(jsonArray);
        }else {
            String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getXnxq&xh=" + user.getString("useraccount"), userinfo.getString("token"));
            Tool.saveFile(getContext(),"data",getsearchresult);
            setAdapter(new JSONArray(getsearchresult));
        }
            redata("");
        } catch (Exception e) {
            Tool.deleteFile(getContext(),"data");
            Toast.makeText(getContext(),"可能是秘钥过期了或者文件读取异常，退出在进来就没问题"+Tool.fileExists(getContext(),"data"),Toast.LENGTH_SHORT).show();
        }
        xqsz.setSelection(0, true);
        xqsz.setOnItemSelectedListener(this);

        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    String getsearchresult = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getXnxq&xh=" + user.getString("useraccount"), userinfo.getString("token"));
                    Tool.saveFile(getContext(),"data",getsearchresult);
                    JSONArray jsonArray = new JSONArray(Tool.readinfo(getContext(),"data"));
                    setAdapter(jsonArray);
                    redata("");
                    Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                    mPullToRefreshView.setRefreshing(false);
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"刷新失败",Toast.LENGTH_SHORT).show();
                    mPullToRefreshView.setRefreshing(false);
                }
            }
        });
        return  view;
    }
    public void setAdapter(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(jsonObject.getString("xqmc"));
        }
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spitem);
        xqsz.setAdapter(adapter);
    }

    private void redata(String xq) {
        try {
            String cjs = HtmlService.getsearchresult("http://jw.nnxy.cn/app.do?method=getCjcx&xh=" + user.getString("useraccount") + "&xnxqid=" + xq, userinfo.getString("token"));
            JSONArray result = new JSONObject(cjs).getJSONArray("result");
            String tes = "";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                tes += "科目：";
                tes += jsonObject.getString("kcmc");
                tes += "\n成绩：";
                tes += jsonObject.getString("zcj");
                tes += "\n\n";
            }
            cx.setText(tes);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "数据异常", Toast.LENGTH_LONG).show();
            cx.setText(e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        redata(xqsz.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}