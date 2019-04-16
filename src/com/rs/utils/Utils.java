package com.rs.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.io.OutputStream;
import com.rs.utils.bzip2.BZip2Decompressor;
import com.rs.utils.gzip.GZipCompressor;
import com.rs.utils.gzip.GZipDecompressor;

public final class Utils {

	private static long timeCorrection;
	private static long lastTimeUpdate;
	public static final Random RANDOM = new Random();
	private static Random random;
	private static Date date;
	public static DateFormat dateFormat;
	
	public static final byte[][] DIRS = new byte[][] { { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };
	
	public static String getDateTime() {
		if (date == null || dateFormat == null) {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5:00"));
			date = new Date();
		}
		date.setTime(System.currentTimeMillis());
		return dateFormat.format(date);
	}

	
	public static synchronized long currentTimeMillis() {
		long l = System.currentTimeMillis();
		if (l < lastTimeUpdate)
			timeCorrection += lastTimeUpdate - l;
		lastTimeUpdate = l;
		return l + timeCorrection;
	}
	
	public static String formatNumber(int num) {
		return NumberFormat.getInstance().format(num);
	}
	
	public static String formatDoubledAmount(long amount) {
		String regularFormat = getFormattedNumber(amount, ',');
		String thousandFormat = getFormattedNumber(amount / 1_000, ',') + "k";
		String millionFormat = getFormattedNumber(amount * 0.000_001, ',') + "m";
		if (amount >= 1_000_000_000L) {
			return "(" + (new DecimalFormat(".##").format(amount * 0.000_000_001)).replace(',', '.') + "b) - " + millionFormat;
		} else if (amount >= 1_000_000) {
			return "(" + (new DecimalFormat(".#").format(amount * 0.000_001)).replace(',', '.') + "m) - " + regularFormat;
		} else if (amount >= 100_000) {
			return "(" + (getFormattedNumber(amount / 1_000, ',')) + "k) - " + regularFormat;
		}
		return (amount < 100_000 ? regularFormat + "" : (getFormattedNumber(amount, ',') + " - " + regularFormat));
	}
	
	public static String getTimeLeft(long time) {
		long timeLeft = (time - currentTimeMillis());
		long min = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft));
		long sec = TimeUnit.MILLISECONDS.toSeconds(timeLeft)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft));
		return min + " minutes & " + sec + " seconds.";
	}
	
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static String getNumberInWords(int id) {
		switch (id) {
		case 1:
			return "one";
		case 2:
			return "two";
		case 3:
			return "three";
		case 4:
			return "four";
		case 5:
			return "five";
		case 6:
			return "six";
		case 7:
			return "seven";
		case 8:
			return "eight";
		case 9:
			return "nine";
		case 10:
			return "ten";
		default:
			return null;
		}
	}
	
	public static boolean startsWithVowel(String name) {
		String[] VOWELS = { "a", "e", "i", "o", "u" };
		for (String n : VOWELS) {
			if (name.toLowerCase().startsWith(n)) {
				return true;
			}
		}
		return false;
	}
	
	private static final Object ALGORITHM_LOCK = new Object();
	
	public static final byte[] encryptUsingMD5(byte[] buffer) {
		//prevents concurrency problems with the algorithm
		synchronized(ALGORITHM_LOCK) {
			try {
				MessageDigest algorithm = MessageDigest.getInstance("MD5");
				algorithm.update(buffer);
				byte[]  digest = algorithm.digest();
				algorithm.reset();
				return digest;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}
	
	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};

	public static final int getFaceDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}
	
	public static int[] anIntArray233 = { 0, 1024, 2048, 3072, 4096, 5120, 6144, 8192, 9216, 12288, 10240, 11264, 16384,
			18432, 17408, 20480, 21504, 22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696, 30720, 31744, 32768,
			33792, 34816, 35840, 36864, 536870912, 16777216, 37888, 65536, 38912, 131072, 196608, 33554432, 524288,
			1048576, 1572864, 262144, 67108864, 4194304, 134217728, 327680, 8388608, 2097152, 12582912, 13631488,
			14680064, 15728640, 100663296, 101187584, 101711872, 101974016, 102760448, 102236160, 40960, 393216, 229376,
			117440512, 104857600, 109051904, 201326592, 205520896, 209715200, 213909504, 106954752, 218103808,
			226492416, 234881024, 222298112, 224395264, 268435456, 272629760, 276824064, 285212672, 289406976,
			223346688, 293601280, 301989888, 318767104, 297795584, 298844160, 310378496, 102498304, 335544320,
			299892736, 300941312, 301006848, 300974080, 39936, 301465600, 49152, 1073741824, 369098752, 402653184,
			1342177280, 1610612736, 469762048, 1476395008, -2147483648, -1879048192, 352321536, 1543503872, -2013265920,
			-1610612736, -1342177280, -1073741824, -1543503872, 356515840, -1476395008, -805306368, -536870912,
			-268435456, 1577058304, -134217728, 360710144, -67108864, 364904448, 51200, 57344, 52224, 301203456, 53248,
			54272, 55296, 56320, 301072384, 301073408, 301074432, 301075456, 301076480, 301077504, 301078528, 301079552,
			301080576, 301081600, 301082624, 301083648, 301084672, 301085696, 301086720, 301087744, 301088768,
			301089792, 301090816, 301091840, 301092864, 301093888, 301094912, 301095936, 301096960, 301097984,
			301099008, 301100032, 301101056, 301102080, 301103104, 301104128, 301105152, 301106176, 301107200,
			301108224, 301109248, 301110272, 301111296, 301112320, 301113344, 301114368, 301115392, 301116416,
			301117440, 301118464, 301119488, 301120512, 301121536, 301122560, 301123584, 301124608, 301125632,
			301126656, 301127680, 301128704, 301129728, 301130752, 301131776, 301132800, 301133824, 301134848,
			301135872, 301136896, 301137920, 301138944, 301139968, 301140992, 301142016, 301143040, 301144064,
			301145088, 301146112, 301147136, 301148160, 301149184, 301150208, 301151232, 301152256, 301153280,
			301154304, 301155328, 301156352, 301157376, 301158400, 301159424, 301160448, 301161472, 301162496,
			301163520, 301164544, 301165568, 301166592, 301167616, 301168640, 301169664, 301170688, 301171712,
			301172736, 301173760, 301174784, 301175808, 301176832, 301177856, 301178880, 301179904, 301180928,
			301181952, 301182976, 301184000, 301185024, 301186048, 301187072, 301188096, 301189120, 301190144,
			301191168, 301193216, 301195264, 301194240, 301197312, 301198336, 301199360, 301201408, 301202432 };

	public static byte[] aByteArray235 = { 22, 22, 22, 22, 22, 22, 21, 22, 22, 20, 22, 22, 22, 21, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13, 16, 7, 10, 6,
			16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12,
			11, 10, 10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13, 21, 4, 7, 6, 5, 3, 6, 6, 5,
			4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4, 5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21, 22, 22 };
	
	public static int encryptPlayerChat(byte[] is, int i_25_, int i_26_, int i_27_, byte[] is_28_) {
		try {
			i_27_ += i_25_;
			int i_29_ = 0;
			int i_30_ = i_26_ << -2116795453;
			for (; i_27_ > i_25_; i_25_++) {
				int i_31_ = 0xff & is_28_[i_25_];
				int i_32_ = anIntArray233[i_31_];
				int i_33_ = aByteArray235[i_31_];
				int i_34_ = i_30_ >> -1445887805;
				int i_35_ = i_30_ & 0x7;
				i_29_ &= (-i_35_ >> 473515839);
				i_30_ += i_33_;
				int i_36_ = ((-1 + (i_35_ - -i_33_)) >> -1430991229) + i_34_;
				i_35_ += 24;
				is[i_34_] = (byte) (i_29_ = (i_29_ | (i_32_ >>> i_35_)));
				if ((i_36_ ^ 0xffffffff) < (i_34_ ^ 0xffffffff)) {
					i_34_++;
					i_35_ -= 8;
					is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
					if (i_36_ > i_34_) {
						i_34_++;
						i_35_ -= 8;
						is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
						if (i_36_ > i_34_) {
							i_35_ -= 8;
							i_34_++;
							is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
							if (i_34_ < i_36_) {
								i_35_ -= 8;
								i_34_++;
								is[i_34_] = (byte) (i_29_ = i_32_ << -i_35_);
							}
						}
					}
				}
			}
			return -i_26_ + ((7 + i_30_) >> -662855293);
		} catch (RuntimeException runtimeexception) {
		}
		return 0;
	}
	
	public static final int getObjectDefinitionsSize() {
		int lastContainerId = Cache.getCacheFileManagers()[16].getContainersSize()-1;
		return lastContainerId * 256 + Cache.getCacheFileManagers()[16].getFilesSize(lastContainerId);
	}
	
	public static final int getNPCDefinitionsSize() {
		int lastContainerId = Cache.getCacheFileManagers()[18].getContainersSize()-1;
		return lastContainerId * 128 + Cache.getCacheFileManagers()[18].getFilesSize(lastContainerId);
	}
	
	public static final int getItemDefinitionsSize() {
		int lastContainerId = Cache.getCacheFileManagers()[19].getContainersSize()-1;
		return lastContainerId * 256 + Cache.getCacheFileManagers()[19].getFilesSize(lastContainerId);
	}
	
	public static final int getInterfaceDefinitionsSize() {
		return Cache.getCacheFileManagers()[3].getContainersSize();
	}
	
	public static final int getInterfaceDefinitionsComponentsSize(int interfaceId) {
		return Cache.getCacheFileManagers()[3].getFilesSize(interfaceId);
	}
	
	public static String formatPlayerNameForProtocol(String name) {
		if(name == null)
			return "";
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}
	
	public static final void shuffleIntegerArray(int[] array) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = array.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');		
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();			
			dirs.add(new File(resource.getFile().replaceAll("%20", " ").replaceAll("%c3%a5", "�")));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {			
			//System.out.println(directory);
			classes.addAll(findClasses(directory, packageName));
		}		
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) {		
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {			
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {			
			if (file.isDirectory()) {		
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {					
					classes.add(Class
							.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				} catch (Throwable e) {

				}
			}
		}
		return classes;
	}

	public static String formatPlayerNameForDisplay(String name) {
		if(name == null)
			return "";
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}
	
	
	public static final String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	
	public enum EntityDirection {
		NORTH(8192), 
		SOUTH(0), 
		EAST(12288), 
		WEST(4096), 
		NORTHEAST(10240), 
		SOUTHEAST(14366), 
		NORTHWEST(6144), 
		SOUTHWEST(2048);
	        
	        private int value;
	        
	        public int getValue() {
	        	return value;
	        }
	        
	        private EntityDirection(int value) {
	        	this.value = value;
	        }
	}


	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
	
	public static final long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}
	
	public static final int packGJString2(int position, byte[] buffer, String String) {
		int length = String.length();
		int offset = position;
		for (int index = 0; length > index; index++) {
			int character = String.charAt(index);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}
	

	public static final int calculateGJString2Length(String String) {
		int length = String.length();
		int gjStringLength = 0;
		for (int index = 0; length > index; index++) {
			char c = String.charAt(index);
			if (c > '\u007f') {
				if (c <= '\u07ff')
					gjStringLength += 2;
				else
					gjStringLength += 3;
			} else
				gjStringLength++;
		}
		return gjStringLength;
	}
	
	public static int getDirectionBetweenTiles(WorldTile from, WorldTile to) {
    	int multiplier;
    	if (from.getX() > to.getX() && from.getY() > to.getY())
    		multiplier = 1;
    	else if (from.getX() > to.getX() && from.getY() < to.getY())
    		multiplier = 2;
    	else if (from.getX() < to.getX() && from.getY() < to.getY())
    		multiplier = 3;
    	else
    		multiplier = 4;
    	if (((from.getX() - to.getX()) + from.getY() - to.getY()) > 0)
    		return Math.abs(multiplier * 4086 / ((from.getX() - to.getX()) + from.getY() - to.getY()));
    	else
    		return Math.abs(multiplier * 4086);
	}
	
	public static final byte[] packContainer(byte[] unpackedData, int compression) {
		OutputStream stream = new OutputStream();
		stream.writeByte(compression);
		byte[] compressedData = null;
		if(compression == 0)
			compressedData = unpackedData;
		else if (compression == 1)
			compressedData = null;
		else
			compressedData = GZipCompressor.compress(unpackedData);
		stream.writeInt(compressedData.length);
		if(compression > 0)
			stream.writeInt(unpackedData.length);
		for(int index = 0; index < compressedData.length; index++)
			stream.writeByte(compressedData[index]);
		byte[] packedData = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(packedData, 0, packedData.length);
		return packedData;
	}
	
	public static final byte[] unpackCacheContainer(byte[] packedData) {
		InputStream stream = new InputStream(packedData);
		int compression = stream.readUnsignedByte();
		int containerSize = stream.readInt();
		if (containerSize < 0 || containerSize > 5000000)
			return null;
			//throw new RuntimeException();
		if (compression == 0) {
			byte unpacked[] = new byte[containerSize];
			stream.readBytes(unpacked, 0, containerSize);
			return unpacked;
		}
		int decompressedSize = stream.readInt();
		if (decompressedSize < 0 || decompressedSize > 20000000)
			return null;
			//throw new RuntimeException();
		byte decompressedData[] = new byte[decompressedSize];
		if(compression == 1) {
			BZip2Decompressor.decompress(decompressedData, packedData, containerSize, 9);
		}else{
			GZipDecompressor.decompress(stream, decompressedData);
		}
		return decompressedData;
	}
	
	public static final int getNameHash(String name) {
		name = name.toLowerCase();
		int hash = 0;
		for (int index = 0; index < name.length(); index++)
			hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
		return hash;
	}
	
	private static final byte method1258(char c) {
		byte charByte;
		if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
			charByte = (byte) c;
		} else if (c != '\u20AC') {
			if (c != '\u201A') {
				if (c != '\u0192') {
					if (c == '\u201E') {
						charByte = -124;
					} else if (c != '\u2026') {
						if (c != '\u2020') {
							if (c == '\u2021') {
								charByte = -121;
							} else if (c == '\u02C6') {
								charByte = -120;
							} else if (c == '\u2030') {
								charByte = -119;
							} else if (c == '\u0160') {
								charByte = -118;
							} else if (c == '\u2039') {
								charByte = -117;
							} else if (c == '\u0152') {
								charByte = -116;
							} else if (c != '\u017D') {
								if (c == '\u2018') {
									charByte = -111;
								} else if (c != '\u2019') {
									if (c != '\u201C') {
										if (c == '\u201D') {
											charByte = -108;
										} else if (c != '\u2022') {
											if (c == '\u2013') {
												charByte = -106;
											} else if (c == '\u2014') {
												charByte = -105;
											} else if (c == '\u02DC') {
												charByte = -104;
											} else if (c == '\u2122') {
												charByte = -103;
											} else if (c != '\u0161') {
												if (c == '\u203A') {
													charByte = -101;
												} else if (c != '\u0153') {
													if (c == '\u017E') {
														charByte = -98;
													} else if (c != '\u0178') {
														charByte = 63;
													} else {
														charByte = -97;
													}
												} else {
													charByte = -100;
												}
											} else {
												charByte = -102;
											}
										} else {
											charByte = -107;
										}
									} else {
										charByte = -109;
									}
								} else {
									charByte = -110;
								}
							} else {
								charByte = -114;
							}
						} else {
							charByte = -122;
						}
					} else {
						charByte = -123;
					}
				} else {
					charByte = -125;
				}
			} else {
				charByte = -126;
			}
		} else {
			charByte = -128;
		}
		return charByte;
	}
	
    public static char[] aCharArray6385
	= { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020',
	    '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0',
	    '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c', '\u201d',
	    '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161',
	    '\u203a', '\u0153', '\0', '\u017e', '\u0178' };
    
    public static final int[] ROTATION_DIR_X = { -1, 0, 1, 0 };

	public static final int[] ROTATION_DIR_Y = { 0, 1, 0, -1 };
	
    public static final String getUnformatedMessage(int messageDataLength, int messageDataOffset, byte[] messageData) {
	    char[] cs = new char[messageDataLength];
	    int i = 0;
	    for (int i_6_ = 0; i_6_ < messageDataLength; i_6_++) {
		int i_7_ = 0xff & messageData[i_6_ + messageDataOffset];
		if ((i_7_ ^ 0xffffffff) != -1) {
		    if ((i_7_ ^ 0xffffffff) <= -129
			&& (i_7_ ^ 0xffffffff) > -161) {
			int i_8_
			    = aCharArray6385[i_7_ - 128];
			if (i_8_ == 0)
			    i_8_ = 63;
			i_7_ = i_8_;
		    }
		    cs[i++] = (char) i_7_;
		}
	    }
	    return new String(cs, 0, i);
    }
	
    public static final byte[] getFormatedMessage(String message) {
	    int i_0_ = message.length();
	    byte[] is = new byte[i_0_];
	    for (int i_1_ = 0; (i_1_ ^ 0xffffffff) > (i_0_ ^ 0xffffffff);
		 i_1_++) {
		int i_2_ = message.charAt(i_1_);
		if (((i_2_ ^ 0xffffffff) >= -1 || i_2_ >= 128)
		    && (i_2_ < 160 || i_2_ > 255)) {
		    if ((i_2_ ^ 0xffffffff) != -8365) {
			if ((i_2_ ^ 0xffffffff) == -8219)
			    is[i_1_] = (byte) -126;
			else if ((i_2_ ^ 0xffffffff) == -403)
			    is[i_1_] = (byte) -125;
			else if (i_2_ == 8222)
			    is[i_1_] = (byte) -124;
			else if (i_2_ != 8230) {
			    if ((i_2_ ^ 0xffffffff) != -8225) {
				if ((i_2_ ^ 0xffffffff) != -8226) {
				    if ((i_2_ ^ 0xffffffff) == -711)
					is[i_1_] = (byte) -120;
				    else if (i_2_ == 8240)
					is[i_1_] = (byte) -119;
				    else if ((i_2_ ^ 0xffffffff) == -353)
					is[i_1_] = (byte) -118;
				    else if ((i_2_ ^ 0xffffffff) != -8250) {
					if (i_2_ == 338)
					    is[i_1_] = (byte) -116;
					else if (i_2_ == 381)
					    is[i_1_] = (byte) -114;
					else if ((i_2_ ^ 0xffffffff) == -8217)
					    is[i_1_] = (byte) -111;
					else if (i_2_ == 8217)
					    is[i_1_] = (byte) -110;
					else if (i_2_ != 8220) {
					    if (i_2_ == 8221)
						is[i_1_] = (byte) -108;
					    else if ((i_2_ ^ 0xffffffff)
						     == -8227)
						is[i_1_] = (byte) -107;
					    else if ((i_2_ ^ 0xffffffff)
						     != -8212) {
						if (i_2_ == 8212)
						    is[i_1_] = (byte) -105;
						else if ((i_2_ ^ 0xffffffff)
							 != -733) {
						    if (i_2_ != 8482) {
							if (i_2_ == 353)
							    is[i_1_]
								= (byte) -102;
							else if (i_2_
								 != 8250) {
							    if ((i_2_
								 ^ 0xffffffff)
								== -340)
								is[i_1_]
								    = (byte) -100;
							    else if (i_2_
								     != 382) {
								if (i_2_
								    == 376)
								    is[i_1_]
									= (byte) -97;
								else
								    is[i_1_]
									= (byte) 63;
							    } else
								is[i_1_]
								    = (byte) -98;
							} else
							    is[i_1_]
								= (byte) -101;
						    } else
							is[i_1_] = (byte) -103;
						} else
						    is[i_1_] = (byte) -104;
					    } else
						is[i_1_] = (byte) -106;
					} else
					    is[i_1_] = (byte) -109;
				    } else
					is[i_1_] = (byte) -117;
				} else
				    is[i_1_] = (byte) -121;
			    } else
				is[i_1_] = (byte) -122;
			} else
			    is[i_1_] = (byte) -123;
		    } else
			is[i_1_] = (byte) -128;
		} else
		    is[i_1_] = (byte) i_2_;
	    }
	    return is;
    }
    
	private Utils() {
		
	}
/*
    public static char method2782(byte value) {
	    int byteChar = 0xff & value;
	    if (byteChar == 0)
		throw new IllegalArgumentException("Non cp1252 character 0x"
						   + Integer.toString(byteChar, 16)
						   + " provided");
	    if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
		int i_4_ = aCharArray6385[-128 + byteChar];
		if ((i_4_ ^ 0xffffffff) == -1)
		    i_4_ = 63;
		byteChar = i_4_;
	    }
	    return (char) byteChar;
    }
    
    public static int getHashMapSize(int size) {
	    size--;
	    size |= size >>> -1810941663;
	    size |= size >>> 2010624802;
	    size |= size >>> 10996420;
	    size |= size >>> 491045480;
	    size |= size >>> 1388313616;
	    return 1 + size;
    }*/

	public static final int getRandom(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}
	public static final int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	public static final double random(double min, double max) {
		final double n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
	}
	public static final int next(int max, int min) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public static final double getRandomDouble(double maxValue) {
		return (Math.random() * (maxValue + 1));

	}
	
	public static final int random(int maxValue) {
		if(maxValue <= 0)
			return  0;
		return RANDOM.nextInt(maxValue);
	}

	public static final int getDistance(WorldTile t1, WorldTile t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}

	public static final int[][] getCoordOffsetsNear(int size) {
		int[] xs = new int[4 + (4 * size)];
		int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] { xs, xy };
	}

/*
	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}*/

	public static boolean inCircle(WorldTile location, WorldTile center, int radius) {
		return getDistance(center, location) < radius;
	}
	public static int getNpcMoveDirection(int dd) {
		if (dd < 0)
			return -1;
		return getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
	}
	public static int getNpcMoveDirection(int dx, int dy) {
		if (dx == 0 && dy > 0)
			return 0;
		if (dx > 0 && dy > 0)
			return 1;
		if (dx > 0 && dy == 0)
			return 2;
		if (dx > 0 && dy < 0)
			return 3;
		if (dx == 0 && dy < 0)
			return 4;
		if (dx < 0 && dy < 0)
			return 5;
		if (dx < 0 && dy == 0)
			return 6;
		if (dx < 0 && dy > 0)
			return 7;
		return -1;
	}
	public static final int getAngle(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}
	

	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}
	public static final Random getRandomGenerator() {
		return random == null ? random = new Random() : random;
	}

	public static String getCompleted(String[] cmd, int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < cmd.length; i++) {
			if (i == cmd.length - 1 || cmd[i + 1].startsWith("+")) {
				return sb.append(cmd[i]).toString();
			}
			sb.append(cmd[i]).append(" ");
		}
		return "null";
	}
	
    public static char method2782(byte value) {
	    int byteChar = 0xff & value;
	    if (byteChar == 0)
		throw new IllegalArgumentException("Non cp1252 character 0x"
						   + Integer.toString(byteChar, 16)
						   + " provided");
	    if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
		int i_4_ = aCharArray6385[-128 + byteChar];
		if ((i_4_ ^ 0xffffffff) == -1)
		    i_4_ = 63;
		byteChar = i_4_;
	    }
	    return (char) byteChar;
    }
    
    public static int getHashMapSize(int size) {
	    size--;
	    size |= size >>> -1810941663;
	    size |= size >>> 2010624802;
	    size |= size >>> 10996420;
	    size |= size >>> 491045480;
	    size |= size >>> 1388313616;
	    return 1 + size;
    }

    public static String fixChatMessagebackup(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		boolean exception = false;
		for (int i = 0; i < message.length(); i++) {
			if(!exception) {
				if (wasSpace) {
					newText.append(("" + message.charAt(i)).toUpperCase());
					if (!String.valueOf(message.charAt(i)).equals(" "))
						wasSpace = false;
				} else {
					newText.append(("" + message.charAt(i)).toLowerCase());
				}
			} else {
				newText.append(("" + message.charAt(i)));
			}
			if (String.valueOf(message.charAt(i)).contains(":"))
				exception = true;
			else
			if (String.valueOf(message.charAt(i)).contains(".")
					|| String.valueOf(message.charAt(i)).contains("!")
					|| String.valueOf(message.charAt(i)).contains("?"))
				wasSpace = true;
		}
		return newText.toString();
	}
    
    public static String fixChatMessage(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < message.length(); i++) {
			if (wasSpace) {
				newText.append(("" + message.charAt(i)).toUpperCase());
				if (!String.valueOf(message.charAt(i)).equals(" "))
					wasSpace = false;
			} else
				newText.append(("" + message.charAt(i)).toLowerCase());
			if (String.valueOf(message.charAt(i)).contains(".")
					|| String.valueOf(message.charAt(i)).contains("!")
					|| String.valueOf(message.charAt(i)).contains("?"))
				wasSpace = true;
			for (String strings : Settings.UNWANTED_WORDS) {
				String replace = "";
				int l = Settings.UNWANTED_WORDS.length;
					for(int ii= 0; ii < l; ii++) {
						replace += "*";
					}
				message = message.replaceAll(strings, replace);
			}
		}
		return newText.toString();
	}

	public static int random(int[] droppedItems) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static String formatTypicalInteger(int integer) {
		return new DecimalFormat("#,###,##0").format(integer).toString();
	}

	public static boolean isOnRange(int x1, int y1, int size1, int x2, int y2,
			int size2, int maxDistance) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		if (distanceX > size2 + maxDistance || distanceX < -size1 - maxDistance
				|| distanceY > size2 + maxDistance
				|| distanceY < -size1 - maxDistance)
			return false;
		return true;
	}
	
	public static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
	}


	public static String getFormattedNumber(double amount, char seperator) {
		String str = new DecimalFormat("#,###,###").format(amount);
		char[] rebuff = new char[str.length()];
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9')
				rebuff[i] = c;
			else
				rebuff[i] = seperator;
		}
		return new String(rebuff);
	}
	public static String getFormattedNumber(int amount) {
		return (new DecimalFormat("#,###,###").format(amount));
	}


	public static WorldTile getFreeTile(WorldTile center, int distance) {
		WorldTile tile = center;
		for (int i = 0; i < 10; i++) {
			tile = new WorldTile(center, distance);
			if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), 1))
				return tile;
		}
		return center;
	}


	public static void print(String line) {
		System.out.println(line);
		
	}

	/*public static boolean itemExists(int id) {
		if (id >= getItemDefinitionsSize()) // set because of custom items
			return false;
		return Cache.STORE.getIndexes()[19].fileExists(id >>> 8, 0xff & id);
	}*/
	
}
