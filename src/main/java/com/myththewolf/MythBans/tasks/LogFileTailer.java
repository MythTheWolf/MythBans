package com.myththewolf.MythBans.tasks;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 
 * @author http://Crunchify.com
 */

public class LogFileTailer implements Runnable {

	private boolean debug = false;

	private int crunchifyRunEveryNSeconds = 2000;
	private long lastKnownPosition = 0;
	private boolean shouldIRun = true;
	private File crunchifyFile = null;
	private static int crunchifyCounter = 0;
	private StringBuilder SB = new StringBuilder();

	public LogFileTailer(String myFile, int myInterval) {
		crunchifyFile = new File(myFile);
		this.crunchifyRunEveryNSeconds = myInterval;
	}

	private void printLine(String message) {
		System.out.println(message);
	}

	public void stopRunning() {
		shouldIRun = false;
	}

	public void run() {
		try {
			while (shouldIRun) {
				Thread.sleep(crunchifyRunEveryNSeconds);
				long fileLength = crunchifyFile.length();
				if (fileLength > lastKnownPosition) {

					// Reading and writing file
					RandomAccessFile readWriteFileAccess = new RandomAccessFile(crunchifyFile, "rw");
					readWriteFileAccess.seek(lastKnownPosition);
					String crunchifyLine = null;
					while ((crunchifyLine = readWriteFileAccess.readLine()) != null) {
						SB.append("\n"+crunchifyLine);
						crunchifyCounter++;
					}
					lastKnownPosition = readWriteFileAccess.getFilePointer();
					readWriteFileAccess.close();
				} else {
					if (debug)
						this.printLine("Hmm.. Couldn't found new line after line # " + crunchifyCounter);
				}
			}
		} catch (Exception e) {
			stopRunning();
		}
		if (debug)
			this.printLine("Exit the program...");
	}

	public String buildString() {
		String b = SB.toString();
		SB = new StringBuilder();
		return b;
		
	}

}
