package com.xingress.xglyph;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import static de.robv.android.xposed.XposedBridge.log;

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

	// for all versions
	private void getGlyph(MethodHookParam param) {
		if (IngressGlyph.ready) {
			return;
		}

		IngressGlyph.glyphSequence.clear();
		List ff = (List) param.getResult();

		for (int i = 0; i < ff.size(); i++) {
			float[] fs = (float[]) ff.get(i);
			debugLog("fs.length: " + fs.length);
			String glyph = IngressGlyph.getIngressGlyph(fs);
			debugLog("glyph: " + glyph);
			IngressGlyph.glyphSequence.add(glyph);
		}

		// Change must be banned again
		IngressGlyph.ready = true;
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.nianticproject.ingress")) {
			return;
		}

		log(TAG + "Ingress loaded");

/*		findAndHookMethod("com.nianticproject.ingress.common.utility.Turing", lpparam.classLoader, "g", String.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				debugLog("begin Calculate glyph");
				float[] result = (float[]) param.getResult();
				String glyph = IngressGlyph.getIngressGlyph(result);
				debugLog("glyph: " + glyph);
				IngressGlyph.glyphSequence.add(glyph);
				debugLog("glyphSequence.size(): " + IngressGlyph.glyphSequence.size());
			}
		}); // FIXME: 15/8/31 for some xposed native hook fail
*/

//======================================= for version 1.85.1 =======================================
		try {
			final Class<?> aqz = findClass("o.aqz", lpparam.classLoader);

			findAndHookMethod("o.aqz", lpparam.classLoader, "ʿ", aqz, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					getGlyph(param);
				}
			});

			final Class<?> cyh = findClass("o.cyh", lpparam.classLoader);

			findAndHookMethod("o.bud$ˋ", lpparam.classLoader, "ˊ", cyh, Object.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					IngressGlyph.ready = false; // change ready for next hack
				}
			}); // FIXME: 15/10/26 1.85.1
		} catch (NoSuchMethodError e) {
			log(TAG + "1.85.1: NoSuchMethodError");
		} catch (ClassNotFoundError e) {
			log(TAG + "1.85.1: ClassNotFoundError");
		}
//==================================================================================================


//======================================= for version 1.83.1 =======================================
		try {
			final Class<?> aqw = findClass("o.aqw", lpparam.classLoader);

			findAndHookMethod("o.aqw", lpparam.classLoader, "ʿ", aqw, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					getGlyph(param);
				}
			});

			final Class<?> cxh = findClass("o.cxh", lpparam.classLoader);

			findAndHookMethod("o.btm$ˋ", lpparam.classLoader, "ˊ", cxh, Object.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					IngressGlyph.ready = false; // change ready for next hack
				}
			}); // FIXME: 15/9/28 ingress 1.83.1
		} catch (NoSuchMethodError e) {
			log(TAG + "1.83.1: NoSuchMethodError");
		} catch (ClassNotFoundError e) {
			log(TAG + "1.83.1: ClassNotFoundError");
		}
//==================================================================================================


//====================================== for version <1.83.1 =======================================
		try {
			final Class<?> aqv = findClass("o.aqv", lpparam.classLoader);

			findAndHookMethod("o.aqv", lpparam.classLoader, "ʿ", aqv, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					getGlyph(param);
				}
			});

			final Class<?> cwa = findClass("o.cwa", lpparam.classLoader);

			findAndHookMethod("o.bta$ˋ", lpparam.classLoader, "ˊ", cwa, Object.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					IngressGlyph.ready = false; // change ready for next hack
				}
			});
		} catch (NoSuchMethodError e) {
			log(TAG + "<1.83.1: NoSuchMethodError");
		} catch (ClassNotFoundError e) {
			log(TAG + "<1.83.1: ClassNotFoundError");
		}
//==================================================================================================


//======================================== for all versions ========================================
		try {
			findAndHookConstructor("com.nianticproject.ingress.glyph.Glyph", lpparam.classLoader, String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					pref.reload();

					if (pref.getBoolean(MainActivity.ACTIVATE, true)) {
						String oldglyph = param.args[0].toString();
						// FIXME: 15/9/2 glyph "imperfect"
						String tmpglyph = IngressGlyph.glyphSequence.get(0);

						if (tmpglyph.equals("khkjkgj")) {
							tmpglyph = "kgjkhj";
						}

						if (tmpglyph.equals("jgkjkhk")) {
							tmpglyph = "jhkjgk";
						}

						param.args[0] = tmpglyph;

						log(TAG + "replaced glyph input '" + oldglyph + "' with new glyph " + tmpglyph);
					} else {
						log(TAG + "deactivated, nothing replaced");
					}
				}

				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					IngressGlyph.glyphSequence.remove(0);
					debugLog("delete the used glyph");
				}
			});
		} catch (NoSuchMethodError error) {
			log(TAG + "Glyph: NoSuchMethodError");
		}
//==================================================================================================
	}
}
