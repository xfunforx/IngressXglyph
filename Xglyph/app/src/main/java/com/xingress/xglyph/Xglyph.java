package com.xingress.xglyph;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getLongField;
import static de.robv.android.xposed.XposedHelpers.newInstance;

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
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.nianticproject.ingress")) {
			return;
		}

		debugLog("Ingress loaded");

		pref.reload();

		if (pref.getBoolean(MainActivity.ACTIVATE, false)) {
			final String portalHackingParamsClassName = "com.nianticproject.ingress.shared.rpc.PortalHackingParams";
			final String userInputGlyphSequenceClassName = "com.nianticproject.ingress.glyph.UserInputGlyphSequence";
			final String glyphClassName = "com.nianticproject.ingress.glyph.Glyph";
			final String turingClassName = "com.nianticproject.ingress.common.utility.Turing";
			final Class<?> portalHackingParamsClass;
			final Class<?> userInputGlyphSequenceClass;
			final Class<?> glyphClass;
			final Class<?> turingClass;

			try {
				portalHackingParamsClass = findClass(portalHackingParamsClassName, lpparam.classLoader);
			} catch (XposedHelpers.ClassNotFoundError e) {
				debugLog(portalHackingParamsClassName + ": ClassNotFoundError");
				return;
			}

			try {
				userInputGlyphSequenceClass = findClass(userInputGlyphSequenceClassName, lpparam.classLoader);
			} catch (XposedHelpers.ClassNotFoundError e) {
				debugLog(userInputGlyphSequenceClassName + ": ClassNotFoundError");
				return;
			}

			try {
				glyphClass = findClass(glyphClassName, lpparam.classLoader);
			} catch (XposedHelpers.ClassNotFoundError e) {
				debugLog(glyphClassName + ": ClassNotFoundError");
				return;
			}

			try {
				turingClass = findClass(turingClassName, lpparam.classLoader);
			} catch (XposedHelpers.ClassNotFoundError e) {
				debugLog(turingClassName + ": ClassNotFoundError");
				return;
			}

			try {
				findAndHookConstructor(portalHackingParamsClass, String.class, boolean.class, boolean.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						debugLog("PortalHackingParams: constructor with (String, boolean, boolean) called");

						String arg0 = (String) param.args[0];
						boolean glyphGameRequested = (boolean) param.args[1];
						boolean hackNoKey = (boolean) param.args[2];

						debugLog("arg0 = " + arg0 + ", glyphGameRequested = " + glyphGameRequested + ", hackNoKey = " + hackNoKey);

						if (!glyphGameRequested) {
							pref.reload();

							if (pref.getBoolean(MainActivity.NORMALHACK, false)) {
								if (pref.getBoolean(MainActivity.NORMALHACKKEY, true)) {
									param.args[2] = false;
									debugLog("Normal Hack key request set");
								} else if (!pref.getBoolean(MainActivity.NORMALHACKKEY, true)) {
									param.args[2] = true;
									debugLog("Normal Hack no key request set");
								}
							}
						}
					}
				});

				findAndHookConstructor(portalHackingParamsClass, String.class, userInputGlyphSequenceClass, userInputGlyphSequenceClass, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						debugLog("PortalHackingParams: constructor with (String, UserInputGlyphSequence, UserInputGlyphSequence) called");

						String uigs1 = "null";
						String uigs2 = "null";

						if (param.args[1] != null) {
							uigs1 = param.args[1].toString();

							debugLog("original uigs1: " + uigs1);

							pref.reload();

							if (pref.getBoolean(MainActivity.REPLACEGLYPHS, false)) {
								List<Object> glyphList = new ArrayList<>();

								for (int i = 0; i < IngressGlyph.glyphSequence.size(); i++) {
									glyphList.add(newInstance(glyphClass, IngressGlyph.glyphSequence.get(i)));
								}

								long inputTimeMs = getLongField(param.args[1], "inputTimeMs");

								debugLog("inputTimeMs = " + inputTimeMs);

								Object uigs = newInstance(userInputGlyphSequenceClass, glyphList, false, inputTimeMs);
								uigs1 = uigs.toString();

								param.args[1] = uigs;
							}
						} else {
							debugLog("original uigs1: " + uigs1);
						}

						boolean hookKeyHack = true;

						if (param.args[2] != null) {
							uigs2 = param.args[2].toString();

							debugLog("original uigs2: " + uigs2);

							int glyphStart = uigs2.indexOf("glyphOrder=") + 11;
							int glyphStop  = uigs2.indexOf("}]");

							String glyphString = uigs2.substring(glyphStart, glyphStop);

							debugLog("command glyph input: " + glyphString);

							switch (glyphString) {
								case "ikj":
									hookKeyHack = false;
									break;
								case "jki":
									hookKeyHack = false;
									break;
								case "gkh":
									hookKeyHack = false;
									break;
								case "hkg":
									hookKeyHack = false;
									break;
							}
						} else {
							debugLog("original uigs2: " + uigs2);
						}

						if (hookKeyHack) {
							pref.reload();

							if (pref.getBoolean(MainActivity.GLYPHHACK, false)) {
								Object commandGlyph = null;

								if (pref.getBoolean(MainActivity.GLYPHHACKKEY, true)) {
									commandGlyph = newInstance(glyphClass, "ikj");
									debugLog("Glyph Hack key request set");
								} else if (!pref.getBoolean(MainActivity.GLYPHHACKKEY, true)) {
									commandGlyph = newInstance(glyphClass, "gkh");
									debugLog("Glyph Hack no key request set");
								}

								if (commandGlyph != null) {
									List<Object> glyphList = new ArrayList<>();
									glyphList.add(commandGlyph);

									int min = 400;
									int max = 500;
									Random rand = new Random();
									long randomNum = (long) rand.nextInt((max - min) + 1) + min;

									Object uigs = newInstance(userInputGlyphSequenceClass, glyphList, false, randomNum);
									uigs2 = uigs.toString();

									param.args[2] = uigs;
								}
							}
						}

						debugLog("patched uigs1 = " + uigs1);
						debugLog("patched uigs2 = " + uigs2);
					}
				});
			} catch (NoSuchMethodError e) {
				debugLog(portalHackingParamsClassName + ": constructor not found");
			}

			try {
				findAndHookMethod(turingClass, "g", String.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						debugLog("begin Calculate glyph");

						float[] result = (float[]) param.getResult();
						String glyph = IngressGlyph.getIngressGlyph(result);

						// correct glyph "imperfect"
						if (glyph.equals("khkjkgj")) {
							glyph = "kgjkhj";
						} else if (glyph.equals("jgkjkhk")) {
							glyph = "jhkjgk";
						}

						debugLog("glyph: " + glyph);

						IngressGlyph.glyphSequence.add(glyph);

						debugLog("glyphSequence.size(): " + IngressGlyph.glyphSequence.size());
					}
				});
			} catch (NoSuchMethodError error) {
				debugLog("Turing.g: NoSuchMethodError");
			}

			try {
				findAndHookMethod(turingClass, "l", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						IngressGlyph.glyphSequence.clear();
					}
				});
			} catch (NoSuchMethodError error) {
				debugLog("Turing.l: NoSuchMethodError");
			}
		} else {
			debugLog("Xglyph switched off");
		}

// ============================== HideX ==============================

		final String apmClassName = "android.app.ApplicationPackageManager";
		final Class<?> apmClass;

		try {
			apmClass = findClass(apmClassName, lpparam.classLoader);
		} catch (XposedHelpers.ClassNotFoundError e) {
			debugLog(apmClassName + ": ClassNotFoundError");
			return;
		}

		try {
			findAndHookMethod(apmClass, "getInstalledApplications", int.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					List installedApplications = (List) param.getResult();

					ArrayList<ApplicationInfo> sortedOutApplications = new ArrayList<>();

					for (Object application : installedApplications) {
						ApplicationInfo applicationInfo = (ApplicationInfo) application;

						if (!applicationInfo.packageName.contains(Xglyph.class.getPackage().getName())) {
							sortedOutApplications.add(applicationInfo);
						}
					}

					param.setResult(sortedOutApplications);
				}
			});
		} catch (NoSuchMethodError error) {
			debugLog("ApplicationPackageManager.getInstalledApplications: NoSuchMethodError");
		}

		try {
			findAndHookMethod(apmClass, "getInstalledPackages", int.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					List installedPackages = (List) param.getResult();

					ArrayList<PackageInfo> sortedOutPackages = new ArrayList<>();

					for (Object installedPackage : installedPackages) {
						PackageInfo packageInfo = (PackageInfo) installedPackage;

						if (!packageInfo.packageName.contains(Xglyph.class.getPackage().getName())) {
							sortedOutPackages.add(packageInfo);
						}
					}

					param.setResult(sortedOutPackages);
				}
			});
		} catch (NoSuchMethodError error) {
			debugLog("ApplicationPackageManager.getInstalledPackages: NoSuchMethodError");
		}
	}
}
