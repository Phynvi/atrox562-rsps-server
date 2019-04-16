package com.rs.utils.pathfinder;

/* Class346 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class346 {
	public static int anInt3689 = 1024;
	public static int anInt3690 = 9;
	public static int anInt3691 = 7;
	public static int anInt3692 = 512;
	public static int anInt3693 = 511;
	public static int anInt3694 = 8;
	public static int anInt3695 = 256;
	public static int anInt3696 = 2;
	public static int anInt3697 = 128;

	static {
		Math.sqrt(131072.0);
	}

	Class346() throws Throwable {
		throw new Error();
	}

	public static RuntimeException_Sub2 method4175(Throwable throwable,
			String string) {
		try {
			RuntimeException_Sub2 runtimeexception_sub2;
			if (throwable instanceof RuntimeException_Sub2) {
				runtimeexception_sub2 = (RuntimeException_Sub2) throwable;
				StringBuilder stringbuilder = new StringBuilder();
				RuntimeException_Sub2 runtimeexception_sub2_27_ = runtimeexception_sub2;
				runtimeexception_sub2_27_.aString6308 = stringbuilder
						.append(runtimeexception_sub2_27_.aString6308)
						.append(' ').append(string).toString();
			} else
				runtimeexception_sub2 = new RuntimeException_Sub2(throwable,
						string);
			return runtimeexception_sub2;
		} catch (RuntimeException runtimeexception) {
			throw method4175(runtimeexception,
					new StringBuilder().append("ol.f(").append(')').toString());
		}
	}
}
