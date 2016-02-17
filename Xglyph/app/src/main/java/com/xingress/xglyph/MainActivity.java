package com.xingress.xglyph;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Cypher on 15/10/28.
 */
public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	public static final String PREF = "Xglyph_Pref";
	public static final String ACTIVATE = "Xglyph_Activate";
	public static final String REPLACEGLYPHS = "Xglyph_ReplaceGlyphs";
	public static final String DEBUGLOG = "Xglyph_DebugLog";
	public static final String NORMALHACK = "Xglyph_NormalHack";
	public static final String NORMALHACKKEY = "Xglyph_NormalHackKey";
	public static final String GLYPHHACK = "Xglyph_GlyphHack";
	public static final String GLYPHHACKKEY = "Xglyph_GlyphHackKey";

	SharedPreferences pref;
	Switch switch_activate;
	CheckBox cb_replaceGlyphs;
	CheckBox cb_debugLog;
	CheckBox cb_normalHack;
	CheckBox cb_glyphHack;
	RadioGroup rg_normal;
	RadioGroup rg_glyph;
	RadioButton rb_normal_key;
	RadioButton rb_normal_noKey;
	RadioButton rb_glyph_key;
	RadioButton rb_glyph_noKey;
	Button button_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences(PREF, MODE_WORLD_READABLE);

		switch_activate = (Switch) findViewById(R.id.switch_activate);
		cb_debugLog = (CheckBox) findViewById(R.id.cb_debugLog);
		cb_replaceGlyphs = (CheckBox) findViewById(R.id.cb_replaceGlyphs);
		cb_normalHack = (CheckBox) findViewById(R.id.cb_normalHack);
		cb_glyphHack = (CheckBox) findViewById(R.id.cb_glyphHack);
		rg_normal = (RadioGroup) findViewById(R.id.rg_normal);
		rg_glyph = (RadioGroup) findViewById(R.id.rg_glyph);
		rb_normal_key = (RadioButton) findViewById(R.id.rb_normal_key);
		rb_normal_noKey = (RadioButton) findViewById(R.id.rb_normal_noKey);
		rb_glyph_key = (RadioButton) findViewById(R.id.rb_glyph_key);
		rb_glyph_noKey = (RadioButton) findViewById(R.id.rb_glyph_noKey);

		rb_normal_key.setEnabled(false);
		rb_normal_noKey.setEnabled(false);
		rb_glyph_key.setEnabled(false);
		rb_glyph_noKey.setEnabled(false);

		button_description = (Button) findViewById(R.id.button_description);

		switch_activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				pref.edit().putBoolean(ACTIVATE, isChecked).apply();

				String toast;

				if (isChecked) {
					toast = "Xglyph on";
				} else {
					toast = "Xglyph off";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		cb_replaceGlyphs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				pref.edit().putBoolean(REPLACEGLYPHS, isChecked).apply();

				String toast;

				if (isChecked) {
					toast = "Glyph replacement on";
				} else {
					toast = "Glyph replacement off";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		cb_debugLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				pref.edit().putBoolean(DEBUGLOG, isChecked).apply();

				String toast;

				if (isChecked) {
					toast = "Xposed debug log on";
				} else {
					toast = "Xposed debug log off";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		cb_normalHack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				pref.edit().putBoolean(NORMALHACK, isChecked).apply();

				rb_normal_key.setEnabled(isChecked);
				rb_normal_noKey.setEnabled(isChecked);

				String toast;

				if (isChecked) {
					toast = "Normal key hack on";
				} else {
					toast = "Normal key hack off";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		cb_glyphHack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				pref.edit().putBoolean(GLYPHHACK, isChecked).apply();

				rb_glyph_key.setEnabled(isChecked);
				rb_glyph_noKey.setEnabled(isChecked);

				String toast;

				if (isChecked) {
					toast = "Glyph key hack on";
				} else {
					toast = "Glyph key hack off";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		rg_normal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				String toast = "null";

				if (i == R.id.rb_normal_key) {
					pref.edit().putBoolean(NORMALHACKKEY, true).apply();

					toast = "Always hack key";
				} else if (i == R.id.rb_normal_noKey) {
					pref.edit().putBoolean(NORMALHACKKEY, false).apply();

					toast = "Never hack key";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		rg_glyph.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				String toast = "null";

				if (i == R.id.rb_glyph_key) {
					pref.edit().putBoolean(GLYPHHACKKEY, true).apply();

					toast = "Always hack key";
				} else if (i == R.id.rb_glyph_noKey) {
					pref.edit().putBoolean(GLYPHHACKKEY, false).apply();

					toast = "Never hack key";
				}

				Toast.makeText(getBaseContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});

		button_description.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog description = new Dialog(MainActivity.this);
				description.setTitle("Description");
				description.setContentView(R.layout.dialog_description);
				description.show();

				final Button button_close = (Button) description.findViewById(R.id.button_close);

				button_close.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						description.cancel();
					}
				});
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();

		pref.edit().putBoolean(ACTIVATE, switch_activate.isChecked()).apply();
		pref.edit().putBoolean(DEBUGLOG, cb_debugLog.isChecked()).apply();
		pref.edit().putBoolean(REPLACEGLYPHS, cb_replaceGlyphs.isChecked()).apply();
		pref.edit().putBoolean(NORMALHACK, cb_normalHack.isChecked()).apply();
		pref.edit().putBoolean(GLYPHHACK, cb_glyphHack.isChecked()).apply();

		int i = rg_normal.getCheckedRadioButtonId();

		if (i == R.id.rb_normal_key) {
			pref.edit().putBoolean(NORMALHACKKEY, true).apply();
		} else if (i == R.id.rb_normal_noKey) {
			pref.edit().putBoolean(NORMALHACKKEY, false).apply();
		}

		i = rg_glyph.getCheckedRadioButtonId();

		if (i == R.id.rb_glyph_key) {
			pref.edit().putBoolean(GLYPHHACKKEY, true).apply();
		} else if (i == R.id.rb_glyph_noKey) {
			pref.edit().putBoolean(GLYPHHACKKEY, false).apply();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		switch_activate.setChecked(pref.getBoolean(ACTIVATE, false));
		cb_debugLog.setChecked(pref.getBoolean(DEBUGLOG, false));
		cb_replaceGlyphs.setChecked(pref.getBoolean(REPLACEGLYPHS, false));
		cb_normalHack.setChecked(pref.getBoolean(NORMALHACK, false));
		cb_glyphHack.setChecked(pref.getBoolean(GLYPHHACK, false));
		rb_normal_key.setChecked(pref.getBoolean(NORMALHACKKEY, true));
		rb_normal_noKey.setChecked(!pref.getBoolean(NORMALHACKKEY, true));
		rb_glyph_key.setChecked(pref.getBoolean(GLYPHHACKKEY, true));
		rb_glyph_noKey.setChecked(!pref.getBoolean(GLYPHHACKKEY, true));
	}
}
