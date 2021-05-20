package easyny.ludashen.com.easyny;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


import easyny.ludashen.com.easyny.util.ThemeUtil;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class ZhuTi extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setBaseTheme(this);
        setContentView(R.layout.zhuti);
        View yel = findViewById(R.id.yel);
        View green = findViewById(R.id.green);
        View grey = findViewById(R.id.grey);
        View blue = findViewById(R.id.blue);
        View df = findViewById(R.id.df);
        ImageButton zt1= (ImageButton) findViewById(R.id.zt1);
        ImageButton zt2= (ImageButton) findViewById(R.id.zt2);
        ImageButton zt3= (ImageButton) findViewById(R.id.zt3);
        ImageButton zt4= (ImageButton) findViewById(R.id.zt4);
        ImageButton zt5= (ImageButton) findViewById(R.id.zt5);
        ImageButton zt6= (ImageButton) findViewById(R.id.zt6);
        yel.setOnClickListener(this);
        green.setOnClickListener(this);
        grey.setOnClickListener(this);
        blue.setOnClickListener(this);
        df.setOnClickListener(this);
        zt1.setOnClickListener(this);
        zt2.setOnClickListener(this);
        zt3.setOnClickListener(this);
        zt4.setOnClickListener(this);
        zt5.setOnClickListener(this);
        zt6.setOnClickListener(this);
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yel:
                ThemeUtil.setNewTheme(this, ThemeUtil.ThemeColors.THEME_YELLOW);
                recreate();
                break;
            case R.id.green:
                ThemeUtil.setNewTheme(this, ThemeUtil.ThemeColors.THEME_GREEN);
                recreate();
                break;
            case R.id.grey:
                ThemeUtil.setNewTheme(this, ThemeUtil.ThemeColors.THEME_GREY);
                recreate();
                break;
            case R.id.blue:
                ThemeUtil.setNewTheme(this, ThemeUtil.ThemeColors.ThEME_BLUE);
                recreate();
                break;
            case R.id.df:
                ThemeUtil.setNewTheme(this,100);
                recreate();
                break;
            case R.id.zt1:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT1);
                recreate();
                break;
            case R.id.zt2:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT2);
                recreate();
                break;
            case R.id.zt3:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT3);
                recreate();
                break;
            case R.id.zt4:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT4);
                recreate();
                break;
            case R.id.zt5:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT5);
                recreate();
                break;
            case R.id.zt6:
                ThemeUtil.setNewTheme(this,ThemeUtil.ThemeColors.ZT6);
                recreate();
                break;
        }
    }
}