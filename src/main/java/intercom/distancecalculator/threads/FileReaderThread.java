package intercom.distancecalculator.threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import com.fasterxml.jackson.databind.ObjectMapper;

import intercom.distancecalculator.beans.RowData;
import intercom.distancecalculator.pojo.FileDataPojo;
import intercom.distancecalculator.util.Constants;
import intercom.distancecalculator.util.Utility;

public class FileReaderThread implements Runnable {

	private BlockingQueue<RowData> dataQueue;

	public FileReaderThread(BlockingQueue<RowData> q) {
		this.dataQueue = q;
	}

	public void run() {
		readDataFromFile(dataQueue, Constants.FILE_NAME);
	}

	public static boolean readDataFromFile(BlockingQueue<RowData> queue, String fileName) {
		FileDataPojo tempRowData = null;
		ObjectMapper mapper = null;
		Scanner scanner = null;
		boolean fileReadSuccessful = false;
		try {
			System.out.println("Going to read content of file = " + fileName + " from path = " + Constants.FILE_PATH);
			scanner = new Scanner(new File(fileName));
			mapper = new ObjectMapper();
			while (scanner.hasNextLine()) {
				tempRowData = Utility.convertStringToJava(scanner.nextLine(), mapper);
				queue.put(Utility.convertFileRowToBlData(tempRowData));
			}
			scanner.close();
			fileReadSuccessful = true;
		} catch (FileNotFoundException e) {
			System.out.println("invalid file name provided, " + e);
		} catch (Exception e) {
			System.out.println("Exception occured, " + e);
		} finally {
			tempRowData = new FileDataPojo();
			tempRowData.setUserId(-1);// Delimiter object to show this object is last item in list
			try {
				queue.put(Utility.convertFileRowToBlData(tempRowData));
			} catch (InterruptedException e) {
				System.out.println("Exception occured, " + e);
			}
		}
		return fileReadSuccessful;
	}

}