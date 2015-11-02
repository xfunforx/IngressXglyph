package com.xingress.xglyph;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Cypher on 15/10/28.
 */
public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

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

		pref = getSharedPreferences(PREF, MODE_WORLD_READABLE);

		switch_Activate = (Switch) findViewById(R.id.switch_Activate);
		cb_debugLog = (CheckBox) findViewById(R.id.cb_debugLog);

		switch_Activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				pref.edit().putBoolean(ACTIVATE, b).apply();
			}
		});

		cb_debugLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				pref.edit().putBoolean(DEBUGLOG, b).apply();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		switch_Activate.setChecked(pref.getBoolean(ACTIVATE, true));
		cb_debugLog.setChecked(pref.getBoolean(DEBUGLOG, false));
	}
}
