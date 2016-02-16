package com.xingress.xglyph;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by xfunx on 15/8/25.
 * Patched by Cypher on 15/10/28.
 */
public class Xglyph implements IXposedHookLoadPackage {
	private static final String TAG = Xglyph.class.getSimpleName() + ": ";

	final XSharedPreferences pref = new XSharedPreferences(Xglyph.class.getPackage().getName(), MainActivity.PREF);

	private void debugLog(String message) {
		pref.reload();

		if (pref.getBoolean(MainActivity.DEBUGLOG, false)) {
			log(TAG + "[DEBUG] " + message);
		}
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.nianticproject.ingress")) {
			return;
		}

		debugLog("Ingress loaded");

		Class<?> glyphClass = findClass("com.nianticproject.ingress.glyph.Glyph", lpparam.classLoader);
		Class<?> turingClass = findClass("com.nianticproject.ingress.common.utility.Turing", lpparam.classLoader);

		try {
			findAndHookConstructor(glyphClass, String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					pref.reload();

					if (pref.getBoolean(MainActivity.ACTIVATE, true)) {
						String oldglyph = param.args[0].toString();
						// FIXME: 15/9/2 glyph "imperfect"
						String tmpglyph = IngressGlyph.glyphSequence.remove(0);

						if (tmpglyph.equals("khkjkgj")) {
							tmpglyph = "kgjkhj";
						}

						if (tmpglyph.equals("jgkjkhk")) {
							tmpglyph = "jhkjgk";
						}

						param.args[0] = tmpglyph;

						debugLog("replaced glyph input '" + oldglyph + "' with new glyph '" + tmpglyph + "'");
					} else {
						debugLog("deactivated, nothing replaced");
					}
				}
			});
		} catch (NoSuchMethodError error) {
			debugLog("Glyph: NoSuchMethodError");
		}

		try {
			findAndHookMethod(turingClass, "l", new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					IngressGlyph.glyphSequence.clear();
				}
			});
		} catch (NoSuchMethodError error) {
			debugLog("Turing: NoSuchMethodError");
		}

		try {
			findAndHookMethod(turingClass, "g", String.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					debugLog("begin Calculate glyph");
					float[] result = (float[]) param.getResult();
					String glyph = IngressGlyph.getIngressGlyph(result);
					debugLog("glyph: " + glyph);
					IngressGlyph.glyphSequence.add(glyph);
					debugLog("glyphSequence.size(): " + IngressGlyph.glyphSequence.size());
				}
			});
		} catch (NoSuchMethodError error) {
			debugLog("Turing: NoSuchMethodError");
		}
	}
}
