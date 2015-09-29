package com.xingress.xglyph;

/**
 * Created by xfunforx on 15/8/25.
 */
import java.util.List;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Xglyph implements IXposedHookLoadPackage {
//    for all version
    private void getglyph(MethodHookParam param){
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
//        });// FIXME: 15/8/31 for some xposed native hook fail
//

        try {

//        ==========================================for version 1.83.1===========================================
            final Class<?> aqw = XposedHelpers.findClass("o.aqw", loadPackageParam.classLoader);
            findAndHookMethod("o.aqw", loadPackageParam.classLoader, "ʿ", aqw, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    getglyph(param);
                }
            });
            final Class<?> cxh = XposedHelpers.findClass("o.cxh", loadPackageParam.classLoader);
            findAndHookMethod("o.btm$ˋ", loadPackageParam.classLoader, "ˊ", cxh, Object.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//              change ready for next hack.
                    IngressGlyph.ready = false;
                }
            });//// FIXME: 15/9/28 ingress 1.83.1
//        =======================================================================================================
//        ==========================================for version <1.83.1==========================================
            final Class<?> aqv = XposedHelpers.findClass("o.aqv", loadPackageParam.classLoader);
            findAndHookMethod("o.aqv", loadPackageParam.classLoader, "ʿ", aqv, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    getglyph(param);
                }
            });
            final Class<?> cwa = XposedHelpers.findClass("o.cwa", loadPackageParam.classLoader);
            findAndHookMethod("o.bta$ˋ", loadPackageParam.classLoader, "ˊ", cwa, Object.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//              change reay for next hack.
                    IngressGlyph.ready = false;
                }
            });
//        ===================================================for all version====================================================


        }catch (NoSuchMethodError e){

        }catch (XposedHelpers.ClassNotFoundError e){

        }
        try {
            findAndHookConstructor("com.nianticproject.ingress.glyph.Glyph", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String oldglyph = param.args[0].toString();
//                // FIXME: 15/9/2 glyph "imperfect"
                    String tmptlyph = IngressGlyph.glyphSequence.get(0).toString();
                    if (tmptlyph.equals("khkjkgj")) {
                        tmptlyph = "kgjkhj";
                    }
                    if (tmptlyph.equals("jgkjkhk")) {
                        tmptlyph = "jhkjgk";
                    }
                    param.args[0] = tmptlyph;
                    XposedBridge.log("mylog: set the glyph :>>" + oldglyph + "== to new glyph==" + tmptlyph);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    IngressGlyph.glyphSequence.remove(0);
                    XposedBridge.log("mylog: delete the used glyph.");
                }
            });
        }catch (NoSuchMethodError error){

        }

    }
}
