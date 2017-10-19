package com.ananda.tailing.bike.smartbike;

import java.util.ArrayList;
import java.util.List;

import com.ananda.tailing.bike.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter{
		private Context context;
		
	
		public DeviceAdapter(Context context) {
			super();
			this.context = context;
			
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
			LayoutInflater inflater = LayoutInflater.from(context);
			View row = inflater.inflate(R.layout.listitem_device, null);//viewGroup);
			updateListItem(row, getDevice(i));
			return row;
		}

		// Public members.
		public void addDevice(DeviceDB.Record r) {
			for(int i=0; i<mDevices.size(); i++) {
				//ListItem t = mDevices.get(i);
				//if(t.rec.identifier.equals(r.identifier)) return;
				DeviceDB.Record t = getDevice(i);
				if(t.identifier.equals(r.identifier)) return;
			}
			
			ListItem item = new ListItem(r);
			mDevices.add(item);
			notifyDataSetChanged();
		}
		
		DeviceDB.Record getDevice(int index) {
			return mDevices.get(index).rec;
		}

		public void reset() {
			mDevices.clear();
			DeviceDB.Record rec = DeviceDB.load(context);
			if(rec != null)
				addDevice(rec);
			notifyDataSetChanged();
		}

		// Private members.
		public class ListItem {
			DeviceDB.Record rec;
			ListItem(DeviceDB.Record r) {
				rec = r;
			}
		}
		private List<ListItem> mDevices = new ArrayList<ListItem>();

		private void updateListItem(View row, DeviceDB.Record rec) {
			TextView txt;
			txt = (TextView)row.findViewById(R.id.device_name);
			txt.setText(rec.name);
			txt = (TextView)row.findViewById(R.id.device_address);
			txt.setText(rec.identifier);
		}
		
		public DeviceDB.Record getClickItem(int index){
			return mDevices.get(index).rec;
		}
}
