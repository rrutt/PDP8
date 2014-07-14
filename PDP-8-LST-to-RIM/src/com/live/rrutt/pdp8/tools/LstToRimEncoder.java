package com.live.rrutt.pdp8.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Richard Rutt
 * 
 */
public class LstToRimEncoder {

	private static final String objExtension = ".OBJ";
	private static final String rimExtension = ".RIM";
	private static final int ASCII_RUBOUT = 0377; // Octal
	private static final int leaderByteCount = 2;
	private static final int maskHi = 07700; // Octal
	private static final int maskLo = 077; // Octal
	private static final int track7 = 0100; // Octal

	/**
	 * Encode a PDP-8 Emulator PAL assembler listing *.LST file into a binary
	 * RIM loader *.RIM file.
	 * 
	 * @param args
	 *            command-line arguments.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if ((args == null) || (args.length == 0)) {
			showUsage();
		} else {
			String listingFilename = args[0];
			processListingFile(listingFilename);
		}
	}

	private static void showUsage() {
		System.out.print("\n" + "Usage:\n"
				+ "  java LstToRimEncoder <file>.LST\n"
				+ "Produces <file>.RIM\n" + "\n");
	}

	private static void processListingFile(String listingFilename)
			throws IOException {
		File listingFile = new File(listingFilename);
		String listingPath = listingFile.getCanonicalPath();

		String objPath = removeExtension(listingPath) + objExtension;
		String rimPath = removeExtension(listingPath) + rimExtension;

		File objFile = new File(objPath);
		File rimFile = new File(rimPath);

		System.out.println(String.format("Processing [%s] listing to [%s] ...",
				listingPath, objPath));
		encodeListingAsObj(listingFile, objFile);

		System.out.println(String.format("Processing [%s] listing to [%s] ...",
				listingPath, rimPath));
		encodeListingAsRim(listingFile, rimFile);

		System.out.println("Done.");
	}

	private static void encodeListingAsObj(File listingFile, File objFile)
			throws IOException {

		int inLineCount = 0;
		int outLineCount = 0;

		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			br = new BufferedReader(new FileReader(listingFile));
			pw = new PrintWriter(new FileWriter(objFile));

			String line;
			while ((line = br.readLine()) != null) {
				inLineCount++;
				if (isStartAddressLine(line)) {
					break; // out of while loop.
				 } else if (isDataLine(line)) {
					String addressString = getAddress(line);
					String dataString = getData(line);
					pw.println(String.format("%s/%s", addressString, dataString));
					outLineCount++;
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
			if (pw != null) {
				pw.close();
			}
		}

		System.out.println(String.format("Processed %d lines into %d lines.",
				inLineCount, outLineCount));
	}

	private static void encodeListingAsRim(File listingFile, File rimFile)
			throws IOException {

		int inLineCount = 0;
		int outByteCount = 0;

		BufferedReader br = null;
		BufferedOutputStream bos = null;

		try {
			br = new BufferedReader(new FileReader(listingFile));
			bos = new BufferedOutputStream(new FileOutputStream(rimFile));
			
			outByteCount += writeLeader(bos);

			String line;
			while ((line = br.readLine()) != null) {
				inLineCount++;
				if (isStartAddressLine(line)) {
					break; // out of while loop.
				 } else if (isDataLine(line)) {
					String addressString = getAddress(line);
					outByteCount += writeAddress(addressString, bos);
					String dataString = getData(line);
					outByteCount += writeData(dataString, bos);
				}
			}
			
			outByteCount += writeLeader(bos);
		} finally {
			if (br != null) {
				br.close();
			}
			if (bos != null) {
				bos.close();
			}
		}

		System.out.println(String.format("Processed %d lines into %d bytes.",
				inLineCount, outByteCount));
	}
	
	private static boolean isStartAddressLine(String line) {
		boolean isStartAddress = false;

		if (line.length() > 0) {
			char firstChar = line.charAt(0);
			isStartAddress = (firstChar == '$');
		}
		
		return isStartAddress;
	}
	
	private static boolean isDataLine(String line) {
		boolean isData = false;

		if (line.length() >= 9) {
			char slash = line.charAt(4);
			isData = (slash == '/');
		}
		
		return isData;
	}
	
	private static String getAddress(String line) {
		return line.substring(0, 4);
	}
	
	private static String getData(String line) {
		return line.substring(5, 9);
	}
	
	private static int writeLeader(OutputStream os) throws IOException {
		for (int i = 0; i < leaderByteCount; i++) {
			os.write(ASCII_RUBOUT);			
		}
		return leaderByteCount;
	}
	
	private static int writeAddress(String value, OutputStream os) throws IOException {
		int address = Integer.decode("0" + value); // Interpret as octal;
		
		int hiAddress = (address & maskHi) >>> 6;
		int loAddress = (address & maskLo);
		
		os.write(hiAddress | track7);
		os.write(loAddress);
		
		return 2;
	}
	
	private static int writeData(String value, OutputStream os) throws IOException {
		int data = Integer.decode("0" + value); // Interpret as octal;
		
		int hiData = (data & maskHi) >>> 6;
		int loData = (data & maskLo);
		
		os.write(hiData);
		os.write(loData);
		
		return 2;
	}

	// Reference:
	// http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-extension-from-a-string-in-java
	private static String removeExtension(String s) {
		String separator = System.getProperty("file.separator");
		String folder;
		String filename;

		// Remove the path up to the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex < 0) {
			folder = "";
			filename = s;
		} else {
			folder = s.substring(0, lastSeparatorIndex + 1);
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex >= 0) {
			filename = filename.substring(0, extensionIndex);
		}

		return folder + filename;
	}
}
