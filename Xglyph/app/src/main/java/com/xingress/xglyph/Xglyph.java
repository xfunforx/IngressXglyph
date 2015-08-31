package com.xingress.xglyph;

/**
 * Created by xfunx on 15/8/25.
 */
import java.util.List;
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

//        findAndHookMethod("com.nianticproject.ingress.common.utility.Turing", loadPackageParam.classLoader, "g", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("mylog: begin Calculate glyph");
//                float[] result = (float[]) param.getResult();
//                String glyphorder = IngressGlyph.getingressglyph(result);
//                XposedBridge.log("mylog: got the glyph:" + glyphorder);
//                IngressGlyph.glyphSequence.add(glyphorder);
//                XposedBridge.log("mylog:the glyphSequence size : " + IngressGlyph.glyphSequence.size());
//            }
//        });
        final Class<?> aqv = XposedHelpers.findClass("o.aqv", loadPackageParam.classLoader);
        findAndHookMethod("o.aqv", loadPackageParam.classLoader, "ʿ", aqv, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (IngressGlyph.ready) {
                    return;
                }
                IngressGlyph.glyphSequence.clear();
                List ff = (List) param.getResult();
                for (int i = 0; i < ff.size(); i++) {
                    float[] fs = (float[]) ff.get(i);
                    XposedBridge.log("mylog: the fs length is:" + fs.length);
                    String glyphorder = IngressGlyph.getingressglyph(fs);
                    XposedBridge.log("mylog: got the glyph:" + glyphorder);
                    IngressGlyph.glyphSequence.add(glyphorder);
                }
//                Change must be banned again
                IngressGlyph.ready = true;
            }
        });
        findAndHookConstructor("com.nianticproject.ingress.glyph.Glyph", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String fuck = (String) param.args[0].toString();
                param.args[0] = IngressGlyph.glyphSequence.get(0).toString();
                XposedBridge.log("mylog: set the glyph :>>" + fuck + "== to new glyph==" + IngressGlyph.glyphSequence.get(0).toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                IngressGlyph.glyphSequence.remove(0);
                XposedBridge.log("mylog: delete the used glyph.");
            }
        });
        final Class<?> cwa  = XposedHelpers.findClass("o.cwa",loadPackageParam.classLoader);
        findAndHookMethod("o.bta$ˋ", loadPackageParam.classLoader, "ˊ", cwa, Object.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//              change reay for next hack.
                IngressGlyph.ready = false;
            }
        });
    }
}
