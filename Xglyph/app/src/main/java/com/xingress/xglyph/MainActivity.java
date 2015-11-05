package com.xingress.xglyph;

import android.app.Activity;
import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Cypher on 15/10/28.
 */
public class MainActivity extends Activity {
//	private static final String TAG = MainActivity.class.getSimpleName();

	public static final String PREF = "XglyphPref";
	public static final String ACTIVATE = "XglyphActivat";
	public static final String DEBUGLOG = "XglyphDebugLog";

	SharedPreferences pref;
	Switch switch_Activate;
	CheckBox cb_debugLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences(PREF,MODE_PRIVATE);

		switch_Activate = (Switch) findViewById(R.id.switch_Activate);
		cb_debugLog = (CheckBox) findViewById(R.id.cb_debugLog);

		switch_Activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				pref.edit().putBoolean(ACTIVATE, b).apply();
				String toast;
				if (b){
					toast = "will work";
				}else{
					toast = "will not work";
				}
				Toast.makeText(getBaseContext(),toast,Toast.LENGTH_SHORT).show();
			}
		});

		cb_debugLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				String toast;
				if (b){
					toast = "will print debug log";
				}else{
					toast = "will not print debug log";
				}
				pref.edit().putBoolean(DEBUGLOG, b).apply();
				Toast.makeText(getBaseContext(),toast,Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
//		switch_Activate.setChecked(pref.getBoolean(ACTIVATE, true));
//		cb_debugLog.setChecked(pref.getBoolean(DEBUGLOG, false));
	}
}
