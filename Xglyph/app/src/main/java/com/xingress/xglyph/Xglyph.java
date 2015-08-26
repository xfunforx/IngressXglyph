package com.xingress.xglyph;

/**
 * Created by xfunx on 15/8/25.
 */

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Xglyph implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.nianticproject.ingress")) {
            return;
        }

        XposedBridge.log("found the ingress started: " + loadPackageParam.packageName);

        final Class<?> lg = XposedHelpers.findClass("o.lg", loadPackageParam.classLoader);

        findAndHookMethod("com.nianticproject.ingress.gameentity.components.DefaultActionRange", loadPackageParam.classLoader, "inRange", lg, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(Boolean.TRUE);
            }
        });

        findAndHookMethod("com.nianticproject.ingress.common.utility.Turing", loadPackageParam.classLoader, "g", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("mylog: begin Calculate glyph");
                float[] result = (float[]) param.getResult();
                String glyphorder = IngressGlyph.getingressglyph(result);
                XposedBridge.log("mylog: got the glyph:" + glyphorder);
                IngressGlyph.glyphSequence.add(glyphorder);
                XposedBridge.log("mylog:the glyphSequence size : " + IngressGlyph.glyphSequence.size());
            }
        });

        findAndHookConstructor("com.nianticproject.ingress.glyph.Glyph", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String fuck = (String) param.args[0].toString();
                param.args[0] = IngressGlyph.glyphSequence.get(0).toString();
                XposedBridge.log("mylog: set the fuck glyph :" + fuck + ">== to ==" + IngressGlyph.glyphSequence.get(0).toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                IngressGlyph.glyphSequence.remove(0);
                XposedBridge.log("mylog: delete the used glyph.");
            }
        });
    }
}
