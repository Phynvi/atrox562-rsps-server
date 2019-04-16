package com.rs.cache.loaders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.cache.CacheFileManager;
import com.rs.io.InputStream;

public class ObjectDefinitions {

	public boolean aBoolean2651;
	public int[] anIntArray2653;
	public int anInt2654 = 0;
	public int[] anIntArray2655;
	public short[] aShortArray2656;
	public int[] anIntArray2657;
	public int anInt2659;
	public boolean aBoolean2660;
	public short[] modelColour;
	public int anInt2663;
	public boolean aBoolean2664;
	public int[] anIntArray2665;
	public int varbit;
	public int anInt2667;
	public int sizeY;
	public int objectAnimation;
	public boolean aBoolean2671 = false;
	public int anInt2672 = 0;
	public boolean aBoolean2673;
	public short[] aShortArray2675;
	public boolean aBoolean2676 = true;
	public boolean aBoolean2678;
	public boolean aBoolean2680;
	public String[] options;
	public int[] anIntArray2684;
	public int anInt2685;
	public int anInt2686;
	public boolean aBoolean2687;
	public int anInt2688;
	public int anInt2689;
	public int anInt2690;
	public boolean ignoreClipOnAlternativeRoute;
	public short[] modifiedColours;
	public int clipType;
	public int anInt2694;
	public int anInt2695;
	public int anInt2696;
	public int anInt2697;
	public int anInt2698;
	public int anInt2699;
	public int[] anIntArray2700;
	public int anInt2701;
	public int sizeX;
	public int anInt2703;
	public int anInt2705;
	public int anInt2708;
	public int anInt2710;
	public int anInt2712;
	public int anInt2713;
	public short aShort2716;
	public int anInt2718;
	public boolean aBoolean2719;
	public int[] models;
	public boolean aBoolean2721;
	public boolean aBoolean2722;
	public boolean notClipped;
	public byte[] aByteArray2725;
	public int anInt2726;
	public String name;
	public int varp;
	public int anInt2733;
	public boolean aBoolean2735;
	public int anInt2736;
	public byte aByte2737;
	private int cflag;

	public ObjectDefinitions() {
		objectAnimation = 16;
		aBoolean2664 = true;
		options = new String[5];
		aBoolean2678 = false;
		aBoolean2687 = false;
		anInt2685 = 0;
		anInt2667 = 128;
		anInt2695 = 0;
		aBoolean2673 = false;
		anInt2697 = 0;
		aBoolean2660 = false;
		ignoreClipOnAlternativeRoute = true;
		anInt2696 = 255;
		varbit = -1;
		anInt2659 = -1;
		anIntArray2657 = null;
		anInt2663 = -1;
		anInt2698 = 0;
		aBoolean2680 = false;
		sizeX = 1;
		anInt2686 = 0;
		anInt2694 = -1;
		anIntArray2700 = null;
		notClipped = false;
		anInt2699 = 128;
		anInt2703 = -1;
		aBoolean2719 = false;
		anInt2689 = 0;
		sizeY = 1;
		clipType = 2;
		anInt2710 = 0;
		anInt2726 = -1;
		aShort2716 = (short) -1;
		aBoolean2721 = false;
		anInt2708 = -1;
		anInt2688 = -1;
		anInt2701 = 128;
		aBoolean2722 = false;
		aBoolean2651 = false;
		anInt2712 = -1;
		anInt2718 = -1;
		anInt2733 = 0;
		anInt2705 = 0;
		anInt2690 = -1;
		varp = -1;
		aBoolean2735 = true;
		anInt2736 = -1;
		name = "null";
		aByte2737 = (byte) 0;
	}

	public static int id;

	private static final ConcurrentHashMap<Integer, ObjectDefinitions> OBJECT_DEFINITIONS = new ConcurrentHashMap<Integer, ObjectDefinitions>();

	public void printFields() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(id + ".txt", true));
		for (Field field : getClass().getDeclaredFields()) {
			if ((field.getModifiers() & 8) != 0) {
				continue;
			}
			try {
				writer.flush();
				writer.write(field.getName() + ": " + getValue(field));
				writer.newLine();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		writer.close();
	}

	private Object getValue(Field field) throws Throwable {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(this));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(this));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(this));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(this));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(this));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(this));
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(this));
		}
		return field.get(this);
	}
	
	public boolean containsOption(int i, String option) {
		if (options == null || options[i] == null || options.length <= i)
			return false;
		return options[i].equals(option);
	}

	public boolean containsOption(String o) {
		if (options == null)
			return false;
		for (String option : options) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase(o))
				return true;
		}
		return false;
	}

	public String getOption(int option) {
		if (options == null || options.length < option || option == 0)
			return "";
		return options[option - 1];
	}

	public static ObjectDefinitions getObjectDefinitions(int id) {
		ObjectDefinitions def = OBJECT_DEFINITIONS.get(id);
		if (def == null) {
			def = new ObjectDefinitions();
			def.id = id;
			byte[] data = Cache.getCacheFileManagers()[16].getFileData(id >>> 1998118472, id & 0xff);
			if (data != null)
				def.readValueLoop(new InputStream(data));
            if (def.notClipped || id == 0 || id == -1) {
            	def.ignoreClipOnAlternativeRoute = true;
            	def.clipType = 0;
            }
			OBJECT_DEFINITIONS.put(id, def);
		}
		return def;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			method2567(stream, opcode);
		}
	}

	public void method2567(InputStream arg0, int arg2) {
		if ((arg2 ^ 0xffffffff) == -2) {
			int i = arg0.readUnsignedByte();
			if (i > 0) {
				if (models == null) {
					models = new int[i];
					anIntArray2665 = new int[i];
					for (int i_0_ = 0; (i_0_ ^ 0xffffffff) > (i ^ 0xffffffff); i_0_++) {
						models[i_0_] = arg0.readUnsignedShort();
						anIntArray2665[i_0_] = arg0.readUnsignedByte();
					}
				} else
					arg0.skip(i * 3);
			}
		} else if (arg2 != 2) {
			if ((arg2 ^ 0xffffffff) == -6) {
				int i = arg0.readUnsignedByte();
				if (i > 0) {
					if (models == null) {
						anIntArray2665 = null;
						models = new int[i];
						for (int i_1_ = 0; i > i_1_; i_1_++)
							models[i_1_] = arg0.readUnsignedShort();
					} else
						arg0.skip(i * 2);
				}
			} else if ((arg2 ^ 0xffffffff) == -15)
				sizeX = arg0.readUnsignedByte();
			else if ((arg2 ^ 0xffffffff) == -16)
				sizeY = arg0.readUnsignedByte();
			else if ((arg2 ^ 0xffffffff) == -18) {
				ignoreClipOnAlternativeRoute = false;
				clipType = 0;
			} else if (arg2 == 18)
				ignoreClipOnAlternativeRoute = false;
			else if (arg2 != 19) {
				if (arg2 == 21)
					aByte2737 = (byte) 1;
				else if (arg2 == 22)
					aBoolean2673 = true;
				else if (arg2 != 23) {
					if (arg2 != 24) {
						if (arg2 == 27)
							clipType = 1;
						else if ((arg2 ^ 0xffffffff) != -29) {
							if (arg2 == 29)
								anInt2705 = arg0.readByte();
							else if (arg2 == 39)
								anInt2697 = arg0.readByte() * 5;
							else if ((arg2 ^ 0xffffffff) > -31 || arg2 >= 35) {
								if ((arg2 ^ 0xffffffff) != -41) {
									if ((arg2 ^ 0xffffffff) != -42) {
										if ((arg2 ^ 0xffffffff) == -43) {
											int i = arg0.readUnsignedByte();
											aByteArray2725 = new byte[i];
											for (int i_2_ = 0; i > i_2_; i_2_++)
												aByteArray2725[i_2_] = (byte) (arg0.readByte());
										} else if ((arg2 ^ 0xffffffff) != -63) {
											if (arg2 == 64)
												aBoolean2664 = false;
											else if ((arg2 ^ 0xffffffff) != -66) {
												if ((arg2 ^ 0xffffffff) == -67)
													anInt2699 = (arg0.readUnsignedShort());
												else if (arg2 != 67) {
													if ((arg2 ^ 0xffffffff) == -70)
														anInt2698 = (arg0.readUnsignedByte());
													else if (arg2 == 70)
														anInt2710 = (arg0.readShort());
													else if (arg2 == 71)
														anInt2686 = (arg0.readShort());
													else if ((arg2 ^ 0xffffffff) != -73) {
														if ((arg2 ^ 0xffffffff) == -74)
															aBoolean2651 = true;
														else if ((arg2 ^ 0xffffffff) == -75)
															notClipped = true;
														else if ((arg2 ^ 0xffffffff) != -76) {
															if (((arg2 ^ 0xffffffff) != -78) && (arg2 != 92)) {
																if (arg2 == 78) {
																	anInt2726 = (arg0.readUnsignedShort());
																	anInt2689 = (arg0.readUnsignedByte());
																} else if (arg2 == 79) {
																	anInt2672 = (arg0.readUnsignedShort());
																	anInt2685 = (arg0.readUnsignedShort());
																	anInt2689 = (arg0.readUnsignedByte());
																	int i = (arg0.readUnsignedByte());
																	anIntArray2684 = (new int[i]);
																	for (int i_3_ = 0; (i_3_ < i); i_3_++)
																		anIntArray2684[i_3_] = arg0.readUnsignedShort();
																} else if (arg2 == 81) {
																	aByte2737 = (byte) 2;
																	aShort2716 = (short) (256 * arg0.readUnsignedByte());
																} else if (arg2 != 82) {
																	if ((arg2 ^ 0xffffffff) != -89) {
																		if ((arg2 ^ 0xffffffff) != -90) {
																			if ((arg2 ^ 0xffffffff) == -91)
																				aBoolean2678 = true;
																			else if ((arg2 ^ 0xffffffff) != -92) {
																				if (arg2 != 93) {
																					if (arg2 == 94)
																						aByte2737 = (byte) 4;
																					else if ((arg2 ^ 0xffffffff) == -96)
																						aByte2737 = (byte) 5;
																					else if ((arg2 ^ 0xffffffff) == -97)
																						aBoolean2680 = true;
																					else if (arg2 != 97) {
																						if (arg2 != 98) {
																							if ((arg2 ^ 0xffffffff) == -100) {
																								anInt2712 = arg0.readUnsignedByte();
																								anInt2659 = arg0.readUnsignedShort();
																							} else if (arg2 != 100) {
																								if ((arg2 ^ 0xffffffff) == -102)
																									anInt2695 = arg0.readUnsignedByte();
																								else if ((arg2 ^ 0xffffffff) != -103) {
																									if (arg2 == 103)
																										anInt2690 = 0;
																									else if ((arg2 ^ 0xffffffff) != -105) {
																										if ((arg2 ^ 0xffffffff) == -106)
																											aBoolean2722 = true;
																										else if ((arg2 ^ 0xffffffff) != -107) {
																											if (arg2 != 107) {
																												if (arg2 < 150 || (arg2 ^ 0xffffffff) <= -156) {
																													if (arg2 != 160) {
																														if (arg2 == 249) {
																															int i = arg0.readUnsignedByte();

																															for (int i_5_ = 0; i_5_ < i; i_5_++) {
																																boolean bool = arg0.readUnsignedByte() == 1;
																																arg0.read24BitInt();
																																if (!bool)
																																	arg0.readInt();
																																else
																																	arg0.readString();
																															}
																														}
																													} else {
																														int i = arg0.readUnsignedByte();
																														anIntArray2655 = new int[i];
																														for (int i_7_ = 0; (i_7_ ^ 0xffffffff) > (i ^ 0xffffffff); i_7_++)
																															anIntArray2655[i_7_] = arg0.readUnsignedShort();
																													}
																												} else {
																													options[-150 + arg2] = arg0.readString();
																												}
																											} else
																												anInt2688 = arg0.readUnsignedShort();
																										} else {
																											int i = arg0.readUnsignedByte();
																											anIntArray2700 = new int[i];
																											anIntArray2657 = new int[i];
																											for (int i_8_ = 0; (i_8_ ^ 0xffffffff) > (i ^ 0xffffffff); i_8_++) {
																												anIntArray2657[i_8_] = arg0.readUnsignedShort();
																												int i_9_ = arg0.readUnsignedByte();
																												anIntArray2700[i_8_] = i_9_;
																												anInt2733 += i_9_;
																											}
																										}
																									} else
																										anInt2696 = arg0.readUnsignedByte();
																								} else
																									anInt2708 = arg0.readUnsignedShort();
																							} else {
																								anInt2694 = arg0.readUnsignedByte();
																								anInt2736 = arg0.readUnsignedShort();
																							}
																						} else
																							aBoolean2660 = true;
																					} else
																						aBoolean2719 = true;
																				} else {
																					aByte2737 = (byte) 3;
																					aShort2716 = (short) arg0.readUnsignedShort();
																				}
																			} else
																				aBoolean2721 = true;
																		} else
																			aBoolean2676 = false;
																	} else
																		aBoolean2735 = false;
																} else
																	aBoolean2671 = true;
															} else {
																varbit = (arg0.readUnsignedShort());
																if (varbit == 65535)
																	varbit = -1;
																varp = (arg0.readUnsignedShort());
																if (varp == 65535)
																	varp = -1;
																int i = -1;
																if ((arg2 ^ 0xffffffff) == -93) {
																	i = (arg0.readUnsignedShort());
																	if (i == 65535)
																		i = -1;
																}
																int i_10_ = (arg0.readUnsignedByte());
																anIntArray2653 = (new int[2 + i_10_]);
																for (int i_11_ = 0; (i_10_ >= i_11_); i_11_++) {
																	anIntArray2653[i_11_] = (arg0.readUnsignedShort());
																	if ((anIntArray2653[i_11_]) == 65535)
																		anIntArray2653[i_11_] = -1;
																}
																anIntArray2653[1 + i_10_] = i;
															}
														} else
															anInt2703 = (arg0.readUnsignedByte());
													} else
														anInt2654 = (arg0.readShort());
												} else
													anInt2701 = (arg0.readUnsignedShort());
											} else
												anInt2667 = arg0.readUnsignedShort();
										} else
											aBoolean2687 = true;
									} else {
										int i = arg0.readUnsignedByte();
										aShortArray2675 = new short[i];
										aShortArray2656 = new short[i];
										for (int i_12_ = 0; ((i_12_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_12_++) {
											aShortArray2656[i_12_] = (short) (arg0.readUnsignedShort());
											aShortArray2675[i_12_] = (short) (arg0.readUnsignedShort());
										}
									}
								} else {
									int i = arg0.readUnsignedByte();
									modelColour = new short[i];
									modifiedColours = new short[i];
									for (int i_13_ = 0; ((i_13_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_13_++) {
										modifiedColours[i_13_] = (short) arg0.readUnsignedShort();
										modelColour[i_13_] = (short) arg0.readUnsignedShort();
									}
								}
							} else {
								options[arg2 + -30] = arg0.readString();
							}
						} else {
							objectAnimation = arg0.readUnsignedByte() << 2;
						}
					} else {
						anInt2718 = arg0.readUnsignedShort();
						if ((anInt2718 ^ 0xffffffff) == -65536)
							anInt2718 = -1;
					}
				} else
					anInt2690 = 1;
			} else
				anInt2663 = arg0.readUnsignedByte();
		} else
			name = arg0.readString();
	}
	
	public static void clearObjectDefinitions() {
		OBJECT_DEFINITIONS.clear();
	}
	
	public int getClipType() {
		return clipType;
	}
	
	public boolean isProjectileClipped() {
		return ignoreClipOnAlternativeRoute;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}
	
	public int getAccessBlockFlag() {
		return cflag;
	}
	
	public String getName() {
		return name;
	}
}
