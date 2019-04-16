package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import com.rs.utils.Utils;

public class ReportAbuse {

	public static void handle(String reportName, String username, int offense, boolean muted) {
		Calendar c = Calendar.getInstance();
		String date= "[" + ((c.get(c.MONTH))+1) + "/" + c.get(c.DATE) + "/" + c.get(c.YEAR) + "]";// You may want to add time
		
			String filepath = "data/report/reports.txt";
			BufferedWriter out;
			try {
				out = new BufferedWriter(new FileWriter(filepath, true));
				out.write(date + " Report by: " + Utils.formatPlayerNameForDisplay(reportName) + " - Offender: " + Utils.formatPlayerNameForDisplay(username) + ", Offense: " + getType(offense) + ".");
				out.newLine();
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	public static String getType(int id) {
		switch(id) {
		case 6:return "Buying or selling account";
		case 9:return "Encourage rule breaking";
		case 5:return "Staff impersonation";
		case 7:return "Macroing/use of bots";
		case 15:return "Scamming";
		case 4:return "Exploiting a bug";
		case 16:return "Seriously offensive language";
		case 17:return "Solicitation";
		case 18:return "Disruptive behaviour";
		case 19:return "Offensive account name";
		case 20:return "Real life threats";
		case 13:return "Asking for real life info";
		case 21:return "Breaking real world laws";
		case 11:return "Advertising websites";
		}
		return "unknown";
	}
	
}