package com.rs.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class CharmRates {

	public static final void main(String[] args) throws IOException {
		
		File directory = new File("dump/");
		if (!directory.exists())
			return;
		int currentLine = 0;
		File[] files = directory.listFiles();
		x : for (File file : files) {
			if (file.getName().endsWith(".txt")) {
				boolean charms = false;
				try (Stream<String> stream = Files.lines(Paths.get(directory+ "/" + file.getName()), Charset.forName("Cp1252"))) {
					for (Object str : stream.toArray()) {
						if (((String) str).startsWith("No charm")) {
							charms = true;
							continue;
						}
						if (charms) {
							boolean found = false;
						//BufferedWriter writer = new BufferedWriter(new FileWriter("Charm rate.txt", true));
						String line = null;
						/*try (Stream<String> lines = Files.lines(Paths.get("Charm rates.txt"), Charset.forName("Cp1252"))) {
							line = lines.skip(currentLine).findFirst().get();
						}*/
						//writer.write(line);
						//writer.flush();
						//writer.write(file.getName().replaceAll(".txt", " - "));
						//String[] splitedLine = ((String) str).split("\t");
						ArrayList<Double> rates = new ArrayList<Double>();
						//for (int i = 1; i < splitedLine.length; i++) {
						//	if (splitedLine[i].contains("\u2013")) {
						//		//System.out.println("contains that shit");
							//	String[] splitLine = (splitedLine[i]).split("\u2013");
								//System.out.println("added /u2013");
								//rates.add(Utils.round(((Double.valueOf(splitLine[1].replaceAll("\u0025", "")) + Double.valueOf(splitLine[0].replaceAll("\u0025", ""))) / 2), 1));
								//rates[i] = Utils.round(((Double.valueOf(splitLine[1].replaceAll("\u0025", "")) + Double.valueOf(splitLine[0].replaceAll("\u0025", ""))) / 2), 1);
							//	writer.write(" " + Utils.round(((Double.valueOf(splitLine[1].replaceAll("\u0025", "")) + Double.valueOf(splitLine[0].replaceAll("\u0025", ""))) / 2), 1));
							//} else  //{
								//rates.add(Double.valueOf(splitedLine[i].replaceAll("\u0025", "")));
								//System.out.println("added other");
							//}	//rates[i] = Double.valueOf(splitedLine[i].replaceAll("\u0025", ""));
							//	writer.write(" " + splitedLine[i]);
						//}
						if (((String) str).endsWith("dropped at a time.")) {
							//writer.write(((String) str).substring(0, 2));
							System.out.println(file.getName() + " " + ((String) str).substring(0, 2));
							//System.out.println(rates[1]);
							//System.out.println(rates.size());
							//rates.forEach(rate -> {
							//});
							//for (int i = 0; i < rates.length; i++)
							//	writer.write(rates[i].toString());
							found = true;
						} else if (!found)
							continue;
						//writer.newLine();
						//writer.close();
						currentLine++;
						continue x;
						}
					}
				} catch(MalformedInputException mie) {
			           continue;
			    } catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
