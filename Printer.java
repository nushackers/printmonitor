import java.util.*;

public class Printer {
	private String name;
	private List<PrintQueue> printQueues;
	private int jobCount;
	
	public Printer(String name) throws Exception {
		this.name = name;
		jobCount = 0;
		printQueues = new ArrayList<PrintQueue>();
		for (String printQueueName : Config.getPrintQueueNames(name)) {
			printQueues.add(new PrintQueue(printQueueName));
		}
	}

	public void update() throws Exception {
		int jobCount = 0;
		for (PrintQueue printQueue : printQueues) {
			printQueue.update();
			jobCount += printQueue.getJobCount();
		}
		this.jobCount = jobCount;
	}

	public String getName() {
		return name;
	}

	public int getNumPrintQueues() {
		return printQueues.size();
	}

	public int getJobCount() {
		return jobCount;
	}

	public List<PrintQueue> getPrintQueues() {
		return printQueues;
	}
}
