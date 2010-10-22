import java.util.*;
import java.io.*;

public class Config {
	private static boolean isLoaded = false;
	private static List<String> printerNames;
	private static Map<String, List<String>> printQueueNames;
	private static String outputFile;
	private static int runMinutes, sleepTime;

	public static List<String> getPrinterNames() throws Exception {
		if (!isLoaded) {
			throw new Exception("Config not loaded");
		}

		return printerNames;
	}

	public static void load(String filename) throws Exception {
		printerNames = new ArrayList<String>();
		printQueueNames = new HashMap<String, List<String>>();

		Scanner fileScanner = new Scanner(new File(filename));		

		outputFile = fileScanner.nextLine().trim();
		runMinutes = Integer.valueOf(fileScanner.nextLine().trim());
		sleepTime  = Integer.valueOf(fileScanner.nextLine().trim());
		
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();

			Scanner lineScanner = new Scanner(line);
			if (!lineScanner.hasNext())
				continue;

			String printerName = lineScanner.next();
			printerNames.add(printerName);

			List<String> printQueueName = new ArrayList<String>();
			printQueueName.add(printerName);
			while (lineScanner.hasNext()) {
				printQueueName.add(lineScanner.next());
			}
			printQueueNames.put(printerName, printQueueName);
		}

		isLoaded = true;
	}

	public static List<String> getPrintQueueNames(String printerName) throws Exception {
		if (!isLoaded) {
			throw new Exception("Config not loaded");
		}

		if (!printQueueNames.containsKey(printerName)) {
			throw new Exception(String.format("Printer %s not found", printerName));
		}

		return printQueueNames.get(printerName);
	}

	public static String getOutputFile() {
		return outputFile;
	}

	public static int getRunMinutes() {
		return runMinutes;
	}

	public static int getSleepTime() {
		return sleepTime;
	}

	public static void main(String[] args) throws Exception {
		Config.load("config.txt");
		for (String printerName : Config.getPrinterNames()) {
			System.out.println(printerName);
			for (String printQueueName : Config.getPrintQueueNames(printerName)) {
				System.out.format("\t%s\n", printQueueName);
			}
		}
	}
}
