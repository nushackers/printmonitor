import java.util.*;
import java.io.*;

public class PrintQueue {
	private String name;
	private List<PrintJob> jobs;
	private String status;

	public PrintQueue(String name) throws Exception {
		this.name = name;
		jobs = new ArrayList<PrintJob>();
		status = "";
	}

	public String getName() {
		return name;
	}

	public List<PrintJob> getPrintJobs() {
		return jobs;
	}

	public void update() throws Exception {
		jobs = new ArrayList<PrintJob>();
		status = "";

		String command = String.format("/usr/local/bin/lpq -P%s", name);
		Process process = Runtime.getRuntime().exec(command);

		int rankLine = -1;
		int lineNumber = 0;
		String line;
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			if (line.startsWith("Rank")) {
				rankLine = lineNumber;
			}
			lines.add(line);
			lineNumber++;
		}

		if (lines.size() == 0) { // no output
			return;
		}

		if (rankLine == -1) { // no queue
			return;
		}

		if (lines.get(0).equals("no entries") || lines.get(lines.size()-1).equals("no entries")) {
			return;
		}

		if (lines.get(0).startsWith("Owner")) {
			// active
			try {
				status = lines.get(1).substring(37,54);
			} catch (Exception e) {
				System.err.println("lines:");
				for (String l : lines) {
					System.err.println(l);
				}
				throw e;
			}
		}

		for (int i = rankLine+1; i < lines.size(); i++) {
			line = lines.get(i);
			String[] words = line.split("\\s+");

			String owner = words[1];
			int ID = Integer.valueOf(words[2]);
			String file = words[3];
			for (int j = 4; j < words.length - 2; j++) {
				file += " " + words[j];
			}
			int bytes = Integer.valueOf(words[words.length-2]);

			jobs.add(new PrintJob(ID, owner, file, bytes));
		}
	}

	public int getJobCount() {
		return jobs.size();
	}

	public String getStatus() {
		return status;
	}

	public static void main(String[] args) throws Exception {
		String name = args[0];
		PrintQueue queue = new PrintQueue(name);
		System.out.println(queue.getJobCount() + " " + queue.getStatus());
	}
}
