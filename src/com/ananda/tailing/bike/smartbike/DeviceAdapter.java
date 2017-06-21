package com.ananda.tailing.bike.smartbike;

import java.util.ArrayList;
import java.util.List;







import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.entity.DeviceInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter{
	private Context context;
	private List<DeviceInfo> mDevices = new ArrayList<DeviceInfo>();
	
	public DeviceAdapter(Context context,List<DeviceInfo> mDevices){
		this.context = context;
		this.mDevices = mDevices;
	}
	
	@Override
	public int getCount() {
		return mDevices.size();
	}

	@Override
	public Object getItem(int i) {
		return mDevices.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		// General ListView optimization code.
		View row = LayoutInflater.from(context).inflate(R.layout.listitem_device, null);//viewGroup);
		updateListItem(row, getDevice(i));
		return row;
	}

	// Public members.
	void addDevice(DeviceInfo r) {
		mDevices.add(r);
		notifyDataSetChanged();
	}
	
	public void reset() {
		mDevices.clear();
		DeviceInfo rec = DeviceDB.loadUsed(context);
		if(rec != null)
			this.addDevice(rec);
		notifyDataSetChanged();
	}
	
	DeviceInfo getDevice(int index) {
		return mDevices.get(index);
	}

	private void updateListItem(View row, DeviceInfo rec) {
		TextView txt;
		txt = (TextView)row.findViewById(R.id.device_name);
		txt.setText(rec.getName());
		txt = (TextView)row.findViewById(R.id.device_address);
		txt.setText(rec.getIdentifier());
	}
}
