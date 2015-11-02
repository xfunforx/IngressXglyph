package com.xingress.xglyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xfunx on 15/8/26.
 * Patched by Cypher on 15/10/28.
 */
public class IngressGlyph {
	public static boolean ready = false;
	public static List<String> glyphSequence = new ArrayList<>();

	private static Double[] pub = {
			 0.0,  1.0, // a
			 0.8,  0.4, // b
			 0.8, -0.5, // c
			 0.0, -1.0, // d
			-0.8, -0.5, // e
			-0.8,  0.4, // f
			-0.4,  0.2, // g
			 0.4,  0.2, // h
			 0.4, -0.2, // i
			-0.4, -0.2, // j
			 0.0,  0.0  // k
	};

	public static String getIngressGlyph(float[] af) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < af.length - 1; i += 2) {
			for (int j = 0; j < pub.length - 1; j += 2) {
				double a = getDouble(af[i]);
				double b = getDouble(af[i + 1]);

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

//		StringBuffer real = new StringBuffer();
		StringBuilder real = new StringBuilder();
		String tmp = result.toString();
		real.append(tmp.charAt(0));

		for (int i = 1; i < tmp.length(); i++) {
			if (real.charAt(real.length() - 1) != tmp.charAt(i)) {
				real.append(tmp.charAt(i));
			}
		}

		Random random = new Random(System.currentTimeMillis());
		int r = random.nextInt(10);
//		System.out.println(r);
		String conver;

		if (r < 5) {
			conver = new StringBuilder(real.toString()).reverse().toString();
		} else {
			conver = real.toString();
		}

//		System.out.println(conver);
		String begin = conver.substring(0, 1);
		String end = conver.substring(conver.length() - 1, conver.length());
//		System.out.println("the begin is :" + begin);
//		System.out.println("the end is :" + end);

		if (begin.equals(end)) {
			int m = random.nextInt(conver.length());
//			System.out.println(m);
			String a = conver.substring(0, m);
			String b = conver.substring(m, conver.length() - 1);
			conver = b + a + conver.substring(m, m + 1);
		}

		return conver;
	}

	private static double getDouble(float a) {
		return (int) (a * 100) / 100.0;
	}
}
