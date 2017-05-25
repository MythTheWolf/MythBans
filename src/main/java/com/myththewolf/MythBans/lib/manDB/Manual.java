package com.myththewolf.MythBans.lib.manDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.FilerException;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Manual {

	StringBuilder pageBuilder = new StringBuilder();
	private List<String> pages = new ArrayList<>();
	private String propertyData;
	private String mantitle;

	public Manual(String manName, CommandSender send, JavaPlugin i) throws IOException {
		File IN = new File(i.getDataFolder() + File.separator + "man-db" + File.separator + manName + ".man");
		if (!IN.exists()) {
			throw new FilerException("Manual page not found");
		}
		mantitle = manName;
		BufferedReader br = new BufferedReader(new FileReader(IN));
		String line;
		int current_page = 0;
		pages.add("");
		StringBuilder toRemove = new StringBuilder();
		boolean readingProps = false;

		while ((line = br.readLine()) != null) {
			if (line.equals("@Properties")) {
				readingProps = true;

			}
			if (readingProps) {
				toRemove.append(line + "\n");
			}
			if (line.equals("@page") && !readingProps) {
				pages.add(pageBuilder.toString().replace("@page", ""));
				pageBuilder = new StringBuilder("");
				current_page++;
				continue;
			}

			if (line.equals("@endProperties")) {
				toRemove.append(line);
				readingProps = false;
				String to_add = pageBuilder.toString().replace(toRemove.toString(), "");
				to_add = to_add.replace("@Properties", "");
				to_add = to_add.replace("@endProperties", "");
				pageBuilder = new StringBuilder(to_add);
				propertyData = toRemove.toString().replace("@Properties", "");
				propertyData = propertyData.replace("@endProperties", "");
				pages.set(current_page, to_add);
				continue;
			}
			if (!readingProps) {
				if (line.trim().isEmpty()) {
					continue;
				} else {
					pageBuilder.append(line + "\n");
					pages.set(current_page, pageBuilder.toString());
				}
			}
		}
		br.close();
	}

	public String getPage(int page) {
		String PP;
		if (page > pages.size()) {
			PP = pages.get(0);
		} else {
			PP = pages.get(page);
		}
		if (pages.size() > 1) {
			PP += "Viewing page " + page + " out of " + (pages.size() - 1) + "\n";
			PP += "Type /man <manual> <page number> to view more pages";
		}
		return PP;
	}

	/**
	 * 
	 * @return List: 0 - INDEX; 1 - TYPE; 2 - HEADER; 3 - FOOTER;
	 * @throws ParseException
	 */
	public List<String> getProperties() throws ParseException {
		List<String> ret = new ArrayList<>();

		return ret;

	}

	public String[] getIndex() throws ParseException {
		InputParser IN = new InputParser(propertyData);
		String[] array = IN.get("INDEX").split(";");
		int idkArray = 0;
		int idkList = 0;
		for(String A : array){
			System.out.println("A-->"+A);
			idkArray++;
		}
		for(String L : pages){
			idkList++;
			System.out.println("L-->"+idkList);
			L.trim(); //just to rid of the warning lol
		}
		if (idkArray-1 != idkList) {
			throw new ParseException("Index list doesn't match number of pages for manual " + mantitle + "\n "
					+ array.length + " " + pages.size(), 0);
		} else {
			return array;
		}
	}

	public List<String> getMap() {
		return this.pages;
	}
}
