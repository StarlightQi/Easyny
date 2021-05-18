package easyny.ludashen.com.easyny;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import easyny.ludashen.com.easyny.util.DataCleanManager;
import easyny.ludashen.com.easyny.util.PasswordMag;
import easyny.ludashen.com.easyny.util.Tool;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class Set extends Activity implements View.OnClickListener ,AdapterView.OnItemSelectedListener {
    private ArrayAdapter<String> adapter;
    private Spinner dl;
    private ListView listView;
    private PasswordMag openHelper;
    List<String> n=new ArrayList<>();
    List<String> p=new ArrayList<>();
    List<String> z=new ArrayList<>();
    private EditText pass;
    private EditText root;
    private EditText zhanhao;
    private boolean yan=false;
    String da="管理员";
    String data[]={"饭堂","管理员","教务系统","学习通","U校园","易班","百度翻译秘钥","南院VPN","请假办事"};
    private Button yanzhen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        openHelper = new PasswordMag(this);
        openHelper.getReadableDatabase();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, data);
        dl= (Spinner) findViewById(R.id.dl);
        listView= (ListView) findViewById(R.id.password);
        pass= (EditText) findViewById(R.id.pass);
        root= (EditText) findViewById(R.id.root);
        zhanhao= (EditText) findViewById(R.id.zhanhao);


        Button sup= (Button) findViewById(R.id.sup);
        sup.setOnClickListener(this);
        yanzhen= (Button) findViewById(R.id.yanzhen);
        yanzhen.setOnClickListener(this);

        adapter.setDropDownViewResource(R.layout.spitem);
        dl.setAdapter(adapter);
        dl.setOnItemSelectedListener(this);

        SQLiteDatabase data = openHelper.getWritableDatabase();
        String sql = "select *from pass";
        Cursor cursor = data.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            n.add(cursor.getString(0));
            z.add(cursor.getString(1));
            p.add(cursor.getString(2));
        }
        data.close();
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
                Intent sets = new Intent(Set.this, Sets.class);
                startActivity(sets);
            }
        });
        listView.setAdapter(new Mylist());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sup:
                if (!yan){
                    Toast.makeText(this,"你好，请先验证本人才可进行操作，请在上边输入管理员的密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    SQLiteDatabase data= openHelper.getReadableDatabase();
                    String sql="insert into pass(name,zhanghao,pass) values(?,?,?)";
                    String pas = pass.getText().toString();
                    String zs = zhanhao.getText().toString();
                    data.execSQL(sql,new String[]{da, zs,pas});
                    data.close();
                    n.add(da);
                    p.add(pas);
                    z.add(zs);
                    listView.setAdapter(new Mylist());
                    Toast.makeText(this,"数据添加成功,有的界面需要重启才可获得刚输入的密码",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(this,"你的数据有误请检查，列表是否存在",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.yanzhen:
                SQLiteDatabase r = openHelper.getWritableDatabase();
                String s = "select *from pass where name='管理员'and pass=?";
                Cursor cursor = r.rawQuery(s, new String[]{root.getText().toString()});
                yan = cursor.moveToFirst();
                if (!yan){
                    Toast.makeText(this,"验证失败请重新输入,默认密码为123456",Toast.LENGTH_SHORT).show();
                    root.setText("");
                }else {
                    Toast.makeText(this,"验证成功",Toast.LENGTH_SHORT).show();
                    yanzhen.setText("验证成功");
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        da=data[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    class Mylist extends BaseAdapter{
        @Override
        public int getCount() {
            return n.size();
        }

        @Override
        public Object getItem(int position) {
            return n.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null)
                view = View.inflate(Set.this, R.layout.list_set, null);
            else
                view = convertView;
            TextView t= (TextView)view. findViewById(R.id.data);
            final EditText vs= (EditText) view. findViewById(R.id.va);
            final EditText zh= (EditText) view. findViewById(R.id.zh);
            Button del= (Button) view.findViewById(R.id.del);
            Button fx= (Button) view.findViewById(R.id.fx);
            Button look= (Button) view.findViewById(R.id.look);

            try {
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!yan) {
                        Toast.makeText(Set.this, "你好，请先验证本人才可进行操作，请在上边输入管理员的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (n.get(position).equals("管理员")) {
                        Toast.makeText(Set.this, "管理员不可以删除", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SQLiteDatabase data = openHelper.getWritableDatabase();
                    String sql = "delete from pass where name=?";
                    data.execSQL(sql,new String[]{ n.get(position)});
                    data.close();
                    n.remove(position);
                    p.remove(position);
                    z.remove(position);
                    listView.setAdapter(new Mylist());

                }
            });

            fx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!yan) {
                        Toast.makeText(Set.this, "你好，请先验证本人才可进行操作，请在上边输入管理员的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SQLiteDatabase data = openHelper.getWritableDatabase();
                    String sql = "update pass set pass=?,zhanghao=? where name=?";
                    data.execSQL(sql,new String[]{vs.getText().toString(),zh.getText().toString(),n.get(position)});
                    data.close();
                    Toast.makeText(Set.this, "修改成功", Toast.LENGTH_SHORT).show();
                    p.set(position,vs.getText().toString());
                    z.set(position,zh.getText().toString());
                }
            });

            look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!yan) {
                        Toast.makeText(Set.this, "你好，请先验证本人才可进行操作，请在上边输入管理员的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    vs.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            });
            }catch (Exception e){
                Toast.makeText(Set.this, "操作失败，请查看数据是否正确", Toast.LENGTH_SHORT).show();
            }
            t.setText(n.get(position));
            vs.setText(p.get(position));
            zh.setText(z.get(position));
            return view;
        }
    };


}