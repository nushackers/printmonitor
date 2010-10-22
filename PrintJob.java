import java.util.*;
import java.io.*;

public class PrintJob {
	private int ID;
	private String owner;
	private String file;
	private int bytes;

	public PrintJob(int ID, String owner, String file, int bytes) {
		this.ID = ID;
		this.owner = owner;
		this.file = file;
		this.bytes = bytes;
	}
}
