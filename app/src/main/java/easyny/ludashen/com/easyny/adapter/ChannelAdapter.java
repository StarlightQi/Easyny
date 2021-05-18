package easyny.ludashen.com.easyny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.Yule;
import easyny.ludashen.com.easyny.util.Channel;

public class ChannelAdapter extends BaseAdapter {

    private ArrayList<Channel> channelList;
    private LayoutInflater layoutInflater;

    public ChannelAdapter(ArrayList<Channel> list, Context context){
        channelList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channelList.size();
    }

    @Override
    public Object getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            //加载布局
            convertView =layoutInflater.inflate(R.layout.grid_item,null);

            holder = new ViewHolder();
            holder.imgChannel = (ImageView)convertView.findViewById(R.id.channel_img);
            holder.decChannel = (TextView)convertView.findViewById(R.id.channel_dec);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //设置图标和文字
        Channel channel = channelList.get(position);
        if(channel != null){
            holder.decChannel.setText(channel.getDec());
            String dec = channel.getDec();
            String[] data= Yule.channelDec;
            if (dec.equals(data[0]))
                holder.imgChannel.setImageResource(R.drawable.word);
            if (dec.equals(data[1]))
                holder.imgChannel.setImageResource(R.drawable.biqu);
            if (dec.equals(data[2]))
                holder.imgChannel.setImageResource(R.drawable.vr);
            if (dec.equals(data[3]))
                holder.imgChannel.setImageResource(R.drawable.hdl);
            if (dec.equals(data[4]))
                holder.imgChannel.setImageResource(R.drawable.cjmla);
            if (dec.equals(data[5]))
                holder.imgChannel.setImageResource(R.drawable.rx);
            if (dec.equals(data[6]))
                holder.imgChannel.setImageResource(R.drawable.qw);
            if (dec.equals(data[7]))
                holder.imgChannel.setImageResource(R.drawable.lxy);
            if (dec.equals(data[8]))
                holder.imgChannel.setImageResource(R.drawable.mc);
            if (dec.equals(data[9]))
                holder.imgChannel.setImageResource(R.drawable.book2);
            if (dec.equals(data[10]))
                holder.imgChannel.setImageResource(R.drawable.share);
            if (dec.equals(data[11]))
                holder.imgChannel.setImageResource(R.drawable.zb);
            if (dec.equals(data[12]))
                holder.imgChannel.setImageResource(R.drawable.big);
            if (dec.equals(data[13]))
                holder.imgChannel.setImageResource(R.drawable.li);
            if (dec.equals(data[14]))
                holder.imgChannel.setImageResource(R.drawable.mover);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imgChannel;
        TextView decChannel;
    }
}
