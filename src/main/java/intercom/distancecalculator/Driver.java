package intercom.distancecalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import intercom.distancecalculator.beans.Distance;
import intercom.distancecalculator.beans.RowData;
import intercom.distancecalculator.threads.DataConsumerThread;
import intercom.distancecalculator.threads.FileReaderThread;
import intercom.distancecalculator.util.Constants;
import intercom.distancecalculator.util.Utility;

/**
 * Muhammad Munib Tahir
 *
 */
public class Driver {
	static Distance officeAddress = null;

	public static void main(String[] args) {

		BlockingQueue<RowData> dataQueue = null;
		List<RowData> customersToBeInvited = null;
		FileReaderThread producer = null;
		DataConsumerThread consumer = null;
		Thread threadProducer = null;
		Thread threadConsumer = null;
		String invitedCustomers = null;
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			Constants.FILE_PATH = System.getProperty("user.dir");
			System.out.println("Enter file name to process..");
			Constants.FILE_NAME = sc.nextLine();
			officeAddress = new Distance(Constants.OFFICE_LATITUDE, Constants.OFFICE_LONGITUDE);
			dataQueue = new ArrayBlockingQueue<>(10); // initializing Queue to 10 size...
			customersToBeInvited = new ArrayList<>();
			producer = new FileReaderThread(dataQueue);
			consumer = new DataConsumerThread(dataQueue, customersToBeInvited, officeAddress);
			threadProducer = new Thread(producer);
			threadConsumer = new Thread(consumer);

			threadProducer.start();
			threadConsumer.start();

			threadProducer.join();
			threadConsumer.join();
			invitedCustomers = Utility.getInvitedCustomers(customersToBeInvited);
			System.out.println(invitedCustomers);
			Utility.printInvitedCustomersToFile(customersToBeInvited);
		} catch (InterruptedException e) {
			System.err.println("Thread interrupted..." + e);
		} catch (Exception e) {
			System.err.println("exception occured..." + e);
		} finally {
			sc.close();
			System.out.println("Application shutting down...");
		}
	}

}
