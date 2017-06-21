package com.ananda.tailing.bike.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ananda.tailing.bike.R;
import com.ananda.tailing.bike.activity.BaseActivity;
import com.ananda.tailing.bike.util.CommonUtils;
import com.ananda.tailing.bike.util.PreferencesUtils;
import com.ananda.tailing.bike.view.TitleBarView;
import com.google.zxing.client.android.CaptureActivity;

/**
 * @package com.ananda.tailing.bike.activity.more
 * @description: 产品信息
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2014-1-22 下午5:52:14
 */
public class GoodsActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private TitleBarView mTitleBarView;
	private TextView scan_result_vehicle_code_tv, scan_result_name_tv,
			scan_result_model_tv, scan_result_cfg_tv, scan_result_bar_code_tv,
			scan_result_frame_number_tv, scan_result_motor_tv,
			scan_result_date_tv;
	private Spinner scan_result_voltage_sp,scan_result_wheel_diameter_sp,scan_result_pole_number_sp;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final String voltage_key = "voltage_key";
	public static final String wheel_diameter_key = "wheel_diameter_key";
	public static final String pole_number_key = "pole_number_key";
	public static final String vehicle_code_key = "vehicle_code_key";
	public static final String name_key = "name_key";
	public static final String model_key = "model_key";
	public static final String cfg_key = "cfg_key";
	public static final String bar_code_key = "bar_code_key";
	public static final String frame_number_key = "frame_number_key";
	public static final String motor_key = "motor_key";
	public static final String date_key = "date_key";
	public static final String vehicle_code_val = "整车编码";
	public static final String name_val = "名称";
	public static final String model_val = "型号";
	public static final String cfg_val = "配置";
	public static final String bar_code_val = "条码";
	public static final String frame_number_val = "车架号";
	public static final String motor_val = "电机号";
	public static final String date_val = "生产日期";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.wait_scan_goods_layout);
		context = this;
		super.onCreate(savedInstanceState);
		CommonUtils.subActivityStack.add(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.scan_button) {
			Intent intent = new Intent(GoodsActivity.this, CaptureActivity.class);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				//显示 (Bitmap) data.getParcelableExtra("bitmap")
				saveResult(result);
				setValues();
			}
			break;
		}
    }

	protected void setValues() {
		scan_result_vehicle_code_tv.setText(PreferencesUtils.getString(context,
				vehicle_code_key, ""));
		scan_result_name_tv.setText(PreferencesUtils.getString(context,
				name_key, ""));
		scan_result_model_tv.setText(PreferencesUtils.getString(context,
				model_key, ""));
		scan_result_cfg_tv.setText(PreferencesUtils.getString(context,
				cfg_key, ""));
		scan_result_bar_code_tv.setText(PreferencesUtils.getString(context,
				bar_code_key, ""));
		scan_result_frame_number_tv.setText(PreferencesUtils.getString(context,
				frame_number_key, ""));
		scan_result_motor_tv.setText(PreferencesUtils.getString(context,
				motor_key, ""));
		scan_result_date_tv.setText(PreferencesUtils.getString(context,
				date_key, ""));
		scan_result_voltage_sp.setSelection(PreferencesUtils.getInt(GoodsActivity.this, voltage_key,0));
		scan_result_wheel_diameter_sp.setSelection(PreferencesUtils.getInt(GoodsActivity.this, wheel_diameter_key,0));
		scan_result_pole_number_sp.setSelection(PreferencesUtils.getInt(GoodsActivity.this, pole_number_key,0));
	}
	
	protected void saveResult(String result){
		String[] productInfoStrList = result.split(",");
		for(String str : productInfoStrList){
			if(str.contains(vehicle_code_val)){
				PreferencesUtils.putString(context, vehicle_code_key, trimString(str,vehicle_code_val));
			}
			if(str.contains(name_val)){
				PreferencesUtils.putString(context, name_key, trimString(str,name_val));
			}
			if(str.contains(model_val)){
				PreferencesUtils.putString(context, model_key, trimString(str,model_val));
			}
			if(str.contains(cfg_val)){
				PreferencesUtils.putString(context, cfg_key, trimString(str,cfg_val));
			}
			if(str.contains(bar_code_val)){
				PreferencesUtils.putString(context, bar_code_key, trimString(str,bar_code_val));
			}
			if(str.contains(frame_number_val)){
				PreferencesUtils.putString(context, frame_number_key, trimString(str,frame_number_val));
			}
			if(str.contains(motor_val)){
				PreferencesUtils.putString(context, motor_key, trimString(str,motor_val));
				setVWPTagValue(str);
			}
			if(str.contains(date_val)){
				PreferencesUtils.putString(context, date_key, trimString(str,date_val));
			}
			
		}
	}
	
	protected void setVWPTagValue(String str){
		String[] vmpValueList = str.split("TLAN");
		if(vmpValueList.length == 2){
			String voltageValue = vmpValueList[1].substring(0, 3);
			String wheelDiameterValue = vmpValueList[1].substring(3, 4);
			String poleNumberValue = vmpValueList[1].substring(4, 5);
			PreferencesUtils.putInt(context, voltage_key, getVoltageMap().get(voltageValue));
			PreferencesUtils.putInt(context, wheel_diameter_key, getWheelDiameterMap().get(wheelDiameterValue));
			PreferencesUtils.putInt(context, pole_number_key, getPoleNumberMap().get(poleNumberValue));
		}
	}
	
	protected String trimString(String str,String trimStr){
		String result = "";
		if(str != null){
			result = str.replace(trimStr, "");
			result = result.replace(":", "");
			result = result.replace("：", "");
		}
		
		return result.trim();
	}

	/* (non-Javadoc)
	 * @see com.ananda.tailing.bike.activity.BaseActivity#initWidget()
	 */
	@Override
	protected void initWidget() {
		// TODO Auto-generated method stub
		mTitleBarView = (TitleBarView) findViewById(R.id.title_layout);
		mTitleBarView.setTitle("产品信息");
		scan_result_vehicle_code_tv = (TextView) findViewById(R.id.scan_result_vehicle_code_tv);
		scan_result_name_tv = (TextView) findViewById(R.id.scan_result_name_tv);
		scan_result_model_tv = (TextView) findViewById(R.id.scan_result_model_tv);
		scan_result_cfg_tv = (TextView) findViewById(R.id.scan_result_cfg_tv);
		scan_result_bar_code_tv = (TextView) findViewById(R.id.scan_result_bar_code_tv);
		scan_result_frame_number_tv = (TextView) findViewById(R.id.scan_result_frame_number_tv);
		scan_result_motor_tv = (TextView) findViewById(R.id.scan_result_motor_tv);
		scan_result_date_tv = (TextView) findViewById(R.id.scan_result_date_tv);
		initSpinner();
		setValues();
	}	
	
	protected void initSpinner(){
		scan_result_voltage_sp = (Spinner) findViewById(R.id.scan_result_voltage_sp);
		scan_result_wheel_diameter_sp = (Spinner) findViewById(R.id.scan_result_wheel_diameter_sp);
		scan_result_pole_number_sp = (Spinner) findViewById(R.id.scan_result_pole_number_sp);
		setSpinnerAdapter(scan_result_voltage_sp,getVoltageList(),0);
		setSpinnerAdapter(scan_result_wheel_diameter_sp,getWheelDiameterList(),1);
		setSpinnerAdapter(scan_result_pole_number_sp,getPoleNumberList(),2);
	}
	
	protected void setSpinnerAdapter(final Spinner spinner,final List<String> list,final int type){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout, list) {

            @Override
            public View getDropDownView(int position, View convertView,
                    ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                label.setText(list.get(position));
                if (spinner.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.goods_content_color));
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.goods_spinner_bg_color));
                }

                return view;
            }

        };
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(type == 0){
					PreferencesUtils.putInt(GoodsActivity.this, voltage_key, position);
				}else if(type ==1){
					PreferencesUtils.putInt(GoodsActivity.this, wheel_diameter_key, position);
				}else if(type ==2){
					PreferencesUtils.putInt(GoodsActivity.this, pole_number_key, position);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public static Map<String,Integer> getVoltageMap(){
		Map<String,Integer> voltageMap = new HashMap<String,Integer>();
		voltageMap.put("48V", 0);
		voltageMap.put("CV", 1);
		voltageMap.put("DV", 2);
		voltageMap.put("EV", 3);
		voltageMap.put("FV", 4);
		voltageMap.put("GV", 5);
		voltageMap.put("HV", 6);
		return voltageMap;
	}
	
	public static Map<String,Integer> getWheelDiameterMap(){
		Map<String,Integer> wheelDiameterMap = new HashMap<String,Integer>();
		wheelDiameterMap.put("A", 0);
		wheelDiameterMap.put("B", 1);
		wheelDiameterMap.put("C", 2);
		wheelDiameterMap.put("D", 3);
		wheelDiameterMap.put("E", 4);
		wheelDiameterMap.put("F", 5);
		return wheelDiameterMap;
	}
	
	public static Map<String,Integer> getPoleNumberMap(){
		Map<String,Integer> poleNumberMap = new HashMap<String,Integer>();
		poleNumberMap.put("A", 0);
		poleNumberMap.put("B", 1);
		poleNumberMap.put("C", 2);
		poleNumberMap.put("D", 3);
		return poleNumberMap;
	}
	
	protected List<String> getVoltageList(){
		List<String> result = new ArrayList<String>();
		result.add("48V");
		result.add("60V");
		result.add("64V");
		result.add("72V");
		result.add("80V");
		result.add("84V");
		result.add("96V");
		return result;
	}
	
	protected List<String> getWheelDiameterList(){
		List<String> result = new ArrayList<String>();
		result.add("8寸");
		result.add("10寸");
		result.add("12寸");
		result.add("14寸");
		result.add("16寸");
		result.add("18寸");
		return result;
	}

	protected List<String> getPoleNumberList(){
		List<String> result = new ArrayList<String>();
		result.add("20对极");
		result.add("23对极");
		result.add("28对极");
		result.add("30对极");
		return result;
	}
	
}
