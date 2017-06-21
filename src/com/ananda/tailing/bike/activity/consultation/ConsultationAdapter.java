package com.ananda.tailing.bike.activity.consultation;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.entity.ConsulationInfo;
import com.ananda.tailing.bike.util.ImageLoadManager;

public class ConsultationAdapter extends BaseAdapter {
	private Context context;
	private List<ConsulationInfo> list;
	private ImageLoadManager imageLoader;

	public ConsultationAdapter(Context context,List<ConsulationInfo> list){
		this.context=context;
		this.list=list;
		imageLoader = ImageLoadManager.getInstance(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void setData(List<ConsulationInfo> list)
	{
		this.list = list;
		this.notifyDataSetChanged();
	}

	public class ViewHolder{
		TextView consultation_item_title;
		TextView consultation_item_details;
		TextView consultation_item_date;
		ImageView consultation_item_image;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ConsulationInfo consulationInfo=list.get(position);
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.consultation_item, null);
			viewHolder.consultation_item_title=(TextView)convertView.findViewById(R.id.consultation_item_title);
			viewHolder.consultation_item_details=(TextView)convertView.findViewById(R.id.consultation_item_details);
			viewHolder.consultation_item_date=(TextView)convertView.findViewById(R.id.consultation_item_date);
			viewHolder.consultation_item_image=(ImageView)convertView.findViewById(R.id.consultation_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.consultation_item_title.setText(consulationInfo.getTitle());
		viewHolder.consultation_item_details.setText(consulationInfo.getAbstract());
		viewHolder.consultation_item_date.setText(consulationInfo.getTime());
		String url = consulationInfo.getPicture();
		imageLoader.display("http://tl.adinnet.cn/" + url, viewHolder.consultation_item_image);
		return convertView;
	}

}
