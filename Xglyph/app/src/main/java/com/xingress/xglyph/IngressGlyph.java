package com.xingress.xglyph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfunx on 15/8/26.
 */
public class IngressGlyph {


    public static List glyphSequence = new ArrayList();
    //                                 a         b         c          d           e          f           g         h         i           j          k
    private static Double[] pub = {0.0, 1.0, 0.8, 0.4, 0.8, -0.5, 0.0, -1.0, -0.8, -0.5, -0.8, 0.4, -0.4, 0.2, 0.4, 0.2, 0.4, -0.2, -0.4, -0.2, 0.0, 0.0};

    public static String getingressglyph(float[] af) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < af.length - 1; i += 2) {
            for (int j = 0; j < pub.length - 1; j += 2) {
                double a = getdouble(af[i]);
                double b = getdouble(af[i + 1]);
                if (Math.abs(pub[j] - a) < 0.1 && Math.abs(pub[j + 1] - b) < 0.1) {
                    switch (j) {
                        case 0:
                            result.append("a");
                            break;
                        case 2:
                            result.append("b");
                            break;
                        case 4:
                            result.append("c");
                            break;
                        case 6:
                            result.append("d");
                            break;
                        case 8:
                            result.append("e");
                            break;
                        case 10:
                            result.append("f");
                            break;
                        case 12:
                            result.append("g");
                            break;
                        case 14:
                            result.append("h");
                            break;
                        case 16:
                            result.append("i");
                            break;
                        case 18:
                            result.append("j");
                            break;
                        case 20:
                            result.append("k");
                            break;
                    }
                }
            }
        }
        StringBuffer real = new StringBuffer();
        String tmp = result.toString();
        real.append(tmp.charAt(0));
        for (int i = 1; i < tmp.length(); i++) {
            if (real.charAt(real.length() - 1) != tmp.charAt(i)) {
                real.append(tmp.charAt(i));
            }
        }
        return real.toString();
    }

    private static double getdouble(float a) {
        float b = a;
        double c = (int) (b * 100) / 100.0;
        return c;
    }
}

