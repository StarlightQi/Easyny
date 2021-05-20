package easyny.ludashen.com.easyny.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.Set;
import easyny.ludashen.com.easyny.view.TitlebarView;

public class Chat extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Button mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        Toast.makeText(this,"还在测试中",Toast.LENGTH_SHORT).show();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mButtonSend = (Button) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mRecyclerView.setAdapter(mAdapter);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
                mEditTextMessage.setText("");
            }
        });
        TitlebarView titlebarView = (TitlebarView) findViewById(R.id.title);
        titlebarView.setTitleSize(20);
        titlebarView.setOnClickLeft(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat.this.finish();
            }
        });
        Intent intent2=getIntent();
        if (intent2.getBooleanExtra("search", false)){
            titlebarView.setRightText("设置");
        }
        titlebarView.setOnClickRidht(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ins=new Intent(Chat.this,Set.class);
                startActivity(ins);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        //对话接收的消息后进行回复
        Document document = null;
        try {
            document = Jsoup.connect("http://106.13.122.53/ludage/chat.do?msg="+message).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").timeout(5000).get();
            mimicOtherMessage(document.text());
        } catch (IOException e) {
            mimicOtherMessage("网络出错了");
        }


    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
