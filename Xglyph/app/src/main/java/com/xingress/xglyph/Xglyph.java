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
 *   last updated on 16/08/04.
 */
public class Xglyph implements IXposedHookLoadPackage {
	private static final String TAG = Xglyph.class.getSimpleName() + ": ";

	final XSharedPreferences pref = new XSharedPreferences(Xglyph.class.getPackage().getName(), MainActivity.PREF);

	private static boolean glyphSpeedTriggered = false;

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
			final String moreGlyph1 = "ikj";
			final String moreGlyph2 = "jki";
			final String lessGlyph1 = "gkh";
			final String lessGlyph2 = "hkg";
			final String complexGlyph1 = "jkgh";
			final String complexGlyph2 = "hgkj";
			final String simpleGlyph1 = "ji";
			final String simpleGlyph2 = "ij";

			final String portalHackingParamsClassName = "com.nianticproject.ingress.shared.rpc.PortalHackingParams";
			final String userInputGlyphSequenceClassName = "com.nianticproject.ingress.glyph.UserInputGlyphSequence";
			final String glyphClassName = "com.nianticproject.ingress.glyph.Glyph";
			final String turingClassName = "com.nianticproject.ingress.common.utility.Turing";
			final String turingClassMethodName1 = "g";
			final String turingClassMethodName2 = "l";
			//final String mqClassName = "o.mq"; // FIXME: this class name is for Ingress v1.99.1 - v1.104.1
			final String mqClassName = "o.ms"; // FIXME: this class name is for Ingress v1.105.1 and maybe future versions
			final String mqClassMethodName = "ˊ"; // FIXME: this method name is for Ingress v1.99.1 - v1.105.1 and maybe future versions

			final Class<?> portalHackingParamsClass;
			final Class<?> userInputGlyphSequenceClass;
			final Class<?> glyphClass;
			final Class<?> turingClass;
			final Class<?> mqClass;

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
				mqClass = findClass(mqClassName, lpparam.classLoader);
			} catch (XposedHelpers.ClassNotFoundError e) {
				debugLog(mqClassName + ": ClassNotFoundError");
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

						pref.reload();

						String uigs1 = "null";
						String uigs2 = "null";

						if (param.args[1] != null) {
							uigs1 = param.args[1].toString();

							debugLog("original uigs1: " + uigs1);

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

						Object commandGlyphKey = null;
						Object commandGlyphSpeed = null;

						if (param.args[2] != null) {
							uigs2 = param.args[2].toString();

							debugLog("original uigs2: " + uigs2);

							List<String> glyphStringList = filterGlyphStrings(uigs2);

							debugLog("command glyph inputs: " + glyphStringList);

							if (glyphStringList.contains(moreGlyph1) || glyphStringList.contains(moreGlyph2)) {
								commandGlyphKey = newInstance(glyphClass, moreGlyph1);
							} else if (glyphStringList.contains(lessGlyph1) || glyphStringList.contains(lessGlyph2)) {
								commandGlyphKey = newInstance(glyphClass, lessGlyph1);
							}

							if (glyphStringList.contains(complexGlyph1) || glyphStringList.contains(complexGlyph2)) {
								commandGlyphSpeed = newInstance(glyphClass, complexGlyph1);
							} else if (glyphStringList.contains(simpleGlyph1) || glyphStringList.contains(simpleGlyph2)) {
								commandGlyphSpeed = newInstance(glyphClass, simpleGlyph1);
							}
						} else {
							debugLog("original uigs2: " + uigs2);
						}

						if (pref.getBoolean(MainActivity.GLYPHHACK, false)) {
							if (commandGlyphKey == null) {
								if (pref.getBoolean(MainActivity.GLYPHHACKKEY, true)) {
									commandGlyphKey = newInstance(glyphClass, moreGlyph1);
									debugLog("Glyph Hack key request set");
								} else if (!pref.getBoolean(MainActivity.GLYPHHACKKEY, true)) {
									commandGlyphKey = newInstance(glyphClass, lessGlyph1);
									debugLog("Glyph Hack no key request set");
								}
							}
						}

						if (pref.getBoolean(MainActivity.GLYPHHACKSPEED, false)) {
							if (commandGlyphSpeed == null) {
								if (pref.getBoolean(MainActivity.GLYPHHACKSPEEDFAST, true)) {
									commandGlyphSpeed = newInstance(glyphClass, complexGlyph1);
									debugLog("Glyph Hack fast set");
								} else if (!pref.getBoolean(MainActivity.GLYPHHACKSPEEDFAST, true)) {
									commandGlyphSpeed = newInstance(glyphClass, simpleGlyph1);
									debugLog("Glyph Hack slow set");
								}
							}
						}

						if (commandGlyphKey != null || commandGlyphSpeed != null) {
							List<Object> glyphList = new ArrayList<>();

							int min = 0;
							int max = 0;

							if (commandGlyphKey != null) {
								glyphList.add(commandGlyphKey);

								min += 400; // average input time for more/less
								max += 500;
							}

							if (commandGlyphSpeed != null && glyphSpeedTriggered) {
								glyphList.add(commandGlyphSpeed);

								if (min != 0) {
									min += 300; // average time gap between two glyphs
									max += 300;
								}

								min += 600; // average input time for complex (simple takes less, but who cares)
								max += 800;

								glyphSpeedTriggered = false;
							}

							Random rand = new Random();
							long randomNum = (long) rand.nextInt((max - min) + 1) + min;

							Object uigs = newInstance(userInputGlyphSequenceClass, glyphList, false, randomNum);
							uigs2 = uigs.toString();

							param.args[2] = uigs;
						}

						debugLog("patched uigs1: " + uigs1);
						debugLog("patched uigs2: " + uigs2);
					}
				});
			} catch (NoSuchMethodError e) {
				debugLog(portalHackingParamsClassName + ": constructor not found");
			}

			try {
				findAndHookMethod(turingClass, turingClassMethodName1, String.class, new XC_MethodHook() {
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
				debugLog(turingClassName + "." + turingClassMethodName1 + ": NoSuchMethodError");
			}

			try {
				findAndHookMethod(turingClass, turingClassMethodName2, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						IngressGlyph.glyphSequence.clear();
					}
				});
			} catch (NoSuchMethodError error) {
				debugLog(turingClassName + "." + turingClassMethodName2 + ": NoSuchMethodError");
			}

			try {
				findAndHookMethod(mqClass, mqClassMethodName, String.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						debugLog("mq." + mqClassMethodName + " called");

						String glyphString = (String) param.args[0];

						debugLog("param[0]: " + glyphString);

						glyphSpeedTriggered = true;

						pref.reload();

						if (pref.getBoolean(MainActivity.GLYPHHACKSPEED, false)) {
							String commandGlyphString = "";

							if (glyphString.equals(complexGlyph1) || glyphString.equals(complexGlyph2)) {
								commandGlyphString = complexGlyph1;
							} else if (glyphString.equals(simpleGlyph1) || glyphString.equals(simpleGlyph2)) {
								commandGlyphString = simpleGlyph1;
							}

							if (commandGlyphString.equals("")) {
								if (pref.getBoolean(MainActivity.GLYPHHACKSPEEDFAST, true)) {
									param.args[0] = complexGlyph1;
									debugLog("Glyph Hack fast set");
								} else if (!pref.getBoolean(MainActivity.GLYPHHACKSPEEDFAST, true)) {
									param.args[0] = simpleGlyph1;
									debugLog("Glyph Hack slow set");
								}
							}
						}
					}
				});
			} catch (NoSuchMethodError error) {
				debugLog(mqClassName + "." + mqClassMethodName + ": NoSuchMethodError");
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

	private List<String> filterGlyphStrings(String glyphSequence) {
		List<String> glyphStringList = new ArrayList<>();

		int glyphStart;
		int glyphStop = 0;
		String glyphString;

		while(!glyphSequence.substring(glyphStop, glyphStop + 2).equals("}]")) {
			glyphStart = glyphSequence.substring(glyphStop).indexOf("glyphOrder=") + 11 + glyphStop;
			glyphStop  = glyphSequence.substring(glyphStart).indexOf("}") + glyphStart;

			glyphString = glyphSequence.substring(glyphStart, glyphStop);

			glyphStringList.add(glyphString);
		}

		return glyphStringList;
	}
}
