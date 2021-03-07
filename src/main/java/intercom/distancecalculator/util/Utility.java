package intercom.distancecalculator.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import intercom.distancecalculator.beans.Distance;
import intercom.distancecalculator.beans.RowData;
import intercom.distancecalculator.pojo.FileDataPojo;

public class Utility {

	public static RowData convertFileRowToBlData(FileDataPojo rowData) {
		RowData newRowObject = null;
		if (null != rowData) {
			newRowObject = new RowData(rowData.getUserId(), rowData.getName(), rowData.getLatitude(), rowData.getLongitude());
		}
		return newRowObject;
	}

	public static boolean isNullOrEmptyQueue(Queue<?> data) {

		if (null == data || data.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNullOrEmptyList(List<?> data) {

		if (null == data || data.size() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * I got help from following link to get distance between two latitude and longitude points https://www.geeksforgeeks.org/program-distance-two-points-earth/ this method returns slightly different results from online distance results
	 * between latitude and longitudes
	 */
	public static double getDistanceBetweenPoints(Distance distanceObj1, Distance distanceObj2) {

		Double distance = 0D;
		Double deltaLatitude = 0D;
		Double deltaLongitude = 0D;
		Double arc = 0D;
		Double c = 0D;
		Double radLat1 = 0D;
		Double radLon1 = 0D;
		Double radLat2 = 0D;
		Double radLon2 = 0D;

		radLat1 = Math.toRadians(distanceObj1.getLatitude());
		radLon1 = Math.toRadians(distanceObj1.getLongitude());

		radLat2 = Math.toRadians(distanceObj2.getLatitude());
		radLon2 = Math.toRadians(distanceObj2.getLongitude());

		// Haversine formula
		deltaLatitude = radLat2 - radLat1;
		deltaLongitude = radLon2 - radLon1;

		arc = Math.pow(Math.sin(deltaLatitude / 2), 2) + Math.cos(distanceObj1.getLatitude()) * Math.cos(distanceObj2.getLatitude()) * Math.pow(Math.sin(deltaLongitude / 2), 2);

		c = 2 * Math.asin(Math.sqrt(arc));

		// calculate the result
		distance = (c * Constants.EARTH_RADIUS);
		return Math.round(distance * 100.0) / 100.0;
	}

	public static boolean isInviteCustomer(Distance tentativeCandidateAddress, Distance officeAddress, Double distanceRange) {
		boolean inviteCustomer = false;
		Double distanceCalculated = null;
		distanceCalculated = Utility.getDistanceBetweenPoints(officeAddress, tentativeCandidateAddress);
		if (Double.compare(distanceCalculated, distanceRange) <= 0) {
			inviteCustomer = true;
		}
		return inviteCustomer;
	}

	public static String getInvitedCustomers(List<RowData> invitedCustomer) {
		StringBuffer buffer = null;
		if (!isNullOrEmptyList(invitedCustomer)) {
			buffer = new StringBuffer("Invited Customers are:");
			for (RowData eachObject : invitedCustomer) {
				buffer.append(" Customer Name = [" + eachObject.getName() + "]");
			}
		} else {
			buffer = new StringBuffer("No customer is invited");
		}
		return buffer.toString();
	}

	public static void printInvitedCustomersToFile(List<RowData> customersToBeInvited) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(Constants.RESULT_FILE_NAME));
			if (!isNullOrEmptyList(customersToBeInvited)) {
				writer.write("Invited Customers are:\n");
				for (RowData eachObject : customersToBeInvited) {
					writer.write("Customer Name = [" + eachObject.getName() + "]");
					writer.newLine();
				}
			} else {
				writer.write("No customer is invited");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Exception occured, " + e);
		}
	}

	public static FileDataPojo convertStringToJava(String value, ObjectMapper mapper) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(value, FileDataPojo.class);
	}
}
