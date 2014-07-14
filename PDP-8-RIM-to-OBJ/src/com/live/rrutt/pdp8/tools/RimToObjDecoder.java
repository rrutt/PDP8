package com.live.rrutt.pdp8.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @author Richard Rutt
 * 
 */
public class RimToObjDecoder {

	private static final String objExtension = ".OBJ";
	private static final int mask = 077; // Octal
	private static final int track7 = 0100; // Octal
	private static final int track8 = 0200; // Octal
	
	private static int inByteCount = 0;


	/**
	 * Decode a PDP-8 Emulator binary RIM loader *.RIM file into a PDP-8 emulator *.OBJ text file.
	 * 
	 * @param args
	 *            command-line arguments.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if ((args == null) || (args.length == 0)) {
			showUsage();
		} else {
			String rimFilename = args[0];
			processRimFile(rimFilename);
		}
	}

	private static void showUsage() {
		System.out.print("\n" + "Usage:\n"
				+ "  java RimToObjDecoder <file>.RIM\n"
				+ "Produces <file>.OBJ\n" + "\n");
	}

	private static void processRimFile(String rimFilename)
			throws IOException {
		File rimFile = new File(rimFilename);
		String rimPath = rimFile.getCanonicalPath();

		String objPath = removeExtension(rimPath) + objExtension;

		File objFile = new File(objPath);

		System.out.println(String.format("Processing [%s] read-in-mode to [%s] ...",
				rimPath, objPath));
		decodeRimAsObj(rimFile, objFile);

		System.out.println("Done.");
	}

	private static void decodeRimAsObj(File rimFile, File objFile)
			throws IOException {

		inByteCount = 0;
		int outLineCount = 0;

		BufferedInputStream bis = null;
		PrintWriter pw = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(rimFile));
			pw = new PrintWriter(new FileWriter(objFile));

			int addressWord;
			int dataWord;
			while ((addressWord = readAddressWord(bis)) >= 0) {
				dataWord = readDataWord(bis);
				pw.println(String.format("%04o/%04o", addressWord, dataWord));
				outLineCount++;
			}
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (pw != null) {
				pw.close();
			}
		}

		System.out.println(String.format("Processed %d bytes into %d lines.",
				inByteCount, outLineCount));
	}
	
	private static int readAddressWord(InputStream is) {
		int word = -1;
		try {
			int hiByte = readIgnoringLeader(is);
			if (hiByte >= 0) {
				int loByte = readIgnoringLeader(is);
				if (loByte >= 0) {
					if ((hiByte & track7) == 0) {
						throw new IOException(
								String.format(
										"High address byte @ x%04x does not have 7th bit set: hi = x%02x, lo = x%02x", inByteCount - 2, hiByte, loByte));
					}
					if ((loByte & track7) != 0) {
						throw new IOException(String.format(
								"Low address byte @ x%04x has 7th bit set: hi = x%02x, lo = x%02x", inByteCount - 1, hiByte, loByte));
					}
					word = ((hiByte & mask) << 6) | loByte;
				}
			}
		} catch (IOException e) {
			System.out.println(String.format("IOException on InputStream: %s",
					e.getMessage()));
			word = -1;
		}

		return word;
	}
	
	private static int readDataWord(InputStream is) {
		int word = -1;
		try {
			int hiByte = readIgnoringLeader(is);
			if (hiByte >= 0) {
				int loByte = readIgnoringLeader(is);
				if (loByte >= 0) {

					if ((hiByte & track7) != 0) {
						throw new IOException(String.format(
								"High data byte @ x%04x has 7th bit set: hi = x%02x, lo = x%02x", inByteCount - 2, hiByte, loByte));
					}
					if ((loByte & track7) != 0) {
						throw new IOException(String.format(
								"Low data byte @ x%04x has 7th bit set: hi = x%02x, lo = x%02x", inByteCount - 1, hiByte, loByte));
					}
					word = (hiByte << 6) | loByte;
				}
			}
		} catch (IOException e) {
			System.out.println(String.format("IOException on InputStream: %s",
					e.getMessage()));
			word = -1;
		}

		return word;
	}
	
	private static int readIgnoringLeader(InputStream is) throws IOException {
		int byteValue = is.read();
		if (byteValue > 0) {
//			System.out.println(String.format("Byte @ x%02x = x%02x", inByteCount, byteValue));
			inByteCount++;
			while ((byteValue > 0) && (byteValue & track8) != 0) {
				byteValue = is.read();
//				System.out.println(String.format("Skip to byte @ x%02x = x%02x", inByteCount, byteValue));
				inByteCount++;
			}
		}
		return byteValue;
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
