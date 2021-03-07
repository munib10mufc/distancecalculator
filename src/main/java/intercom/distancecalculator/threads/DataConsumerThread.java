package intercom.distancecalculator.threads;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import intercom.distancecalculator.beans.Distance;
import intercom.distancecalculator.beans.RowData;
import intercom.distancecalculator.util.Constants;
import intercom.distancecalculator.util.Utility;

public class DataConsumerThread implements Runnable {

	private BlockingQueue<RowData> fileData;
	private List<RowData> invitedCustomersList;
	private Distance officeAddress;

	public DataConsumerThread(BlockingQueue<RowData> fileData, List<RowData> invitedClients, Distance officeAddress) {
		this.fileData = fileData;
		this.invitedCustomersList = invitedClients;
		this.officeAddress = officeAddress;
	}

	public void run() {
		try {
			RowData tentativeCandidate = null;
			while (true) {
				tentativeCandidate = fileData.take();
				if (null == tentativeCandidate || tentativeCandidate.getId() == -1) {
					System.out.println("Last element received in queue, no need to process it");
					break;
				}
				if (Utility.isInviteCustomer(tentativeCandidate.getDistanceDetails(), officeAddress, Constants.INVITATION_RANGE)) {
					invitedCustomersList.add(tentativeCandidate);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception occured, " + e);
		}
	}

}