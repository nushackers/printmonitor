import java.util.*;
import java.io.*;

public class PrintMonitor {
	private static List<Printer> printers;
	public static void main(String[] args) throws Exception {
		long now, startTime;
		now = startTime = System.currentTimeMillis();

		Config.load("config.txt");
		int runMinutes = Config.getRunMinutes();
		int sleepTime = Config.getSleepTime();
		long endTime = startTime + runMinutes * 60000;

		printers = new ArrayList<Printer>();
		for (String printerName : Config.getPrinterNames()) {
			printers.add(new Printer(printerName));
		}

		do {
			updatePrinters();
			writeOutputToFile();
			Thread.sleep(sleepTime);
			now = System.currentTimeMillis();
		} while (now < endTime);
	}

	public static void updatePrinters() throws Exception {
		for (Printer printer : printers) {
			printer.update();
		}
	}

	public static void writeOutputToFile() throws Exception {
		PrintWriter summary = new PrintWriter(Config.getOutputFile());
		summary.print(printers.size() + " ");
		for (Printer printer : printers) {
			summary.format("%s %d %d ", printer.getName(), printer.getNumPrintQueues(), printer.getJobCount());
			for (PrintQueue printQueue : printer.getPrintQueues()) {
				summary.format("%s %d ", printQueue.getName(), printQueue.getJobCount());
			}
		}
		summary.close();
	}
}
