package easyny.ludashen.com.easyny.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import easyny.ludashen.com.easyny.Nnxy;
import easyny.ludashen.com.easyny.R;
import easyny.ludashen.com.easyny.util.Channel;
public class NnxyAdapter extends BaseAdapter {
    private ArrayList<Channel> channelList;
    private LayoutInflater layoutInflater;

    public NnxyAdapter(ArrayList<Channel> list, Context context){
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
            String[] data= Nnxy.channelDec;
            if (dec.equals(data[0]))
                holder.imgChannel.setImageResource(R.drawable.xfu);
            if (dec.equals(data[1]))
                holder.imgChannel.setImageResource(R.drawable.noti);
            if (dec.equals(data[2]))
                holder.imgChannel.setImageResource(R.drawable.yuying);
            if (dec.equals(data[3]))
                holder.imgChannel.setImageResource(R.drawable.vpn);
            if (dec.equals(data[4]))
                holder.imgChannel.setImageResource(R.drawable.yiban);
            if (dec.equals(data[5]))
                holder.imgChannel.setImageResource(R.drawable.live);
            if (dec.equals(data[6]))
                holder.imgChannel.setImageResource(R.drawable.lib);
            if (dec.equals(data[7]))
                holder.imgChannel.setImageResource(R.drawable.dian);
            if (dec.equals(data[8]))
                holder.imgChannel.setImageResource(R.drawable.wz5);
            if (dec.equals(data[9]))
                holder.imgChannel.setImageResource(R.drawable.uxy);
            if (dec.equals(data[10]))
                holder.imgChannel.setImageResource(R.drawable.iwriter);
            if (dec.equals(data[11]))
                holder.imgChannel.setImageResource(R.drawable.learn);
            if (dec.equals(data[12]))
                holder.imgChannel.setImageResource(R.drawable.cn);
            if (dec.equals(data[13]))
                holder.imgChannel.setImageResource(R.drawable.prg);
            if (dec.equals(data[14]))
                holder.imgChannel.setImageResource(R.drawable.biancheng);

        }
        return convertView;
    }
    class ViewHolder{
        ImageView imgChannel;
        TextView decChannel;
    }
}
