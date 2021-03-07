package Intercom.distancecalculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.fasterxml.jackson.databind.ObjectMapper;

import intercom.distancecalculator.beans.Distance;
import intercom.distancecalculator.beans.RowData;
import intercom.distancecalculator.pojo.FileDataPojo;
import intercom.distancecalculator.util.Constants;
import intercom.distancecalculator.util.Utility;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DistanceCalculatorTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public DistanceCalculatorTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(DistanceCalculatorTest.class);
	}

	public void testJsonToJavaConversionValid() {
		String rowString = "{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}";
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			FileDataPojo fileDataPojo = new FileDataPojo();
			fileDataPojo.setUserId(1);
			fileDataPojo.setName("Alice Cahill");
			fileDataPojo.setLatitude(51.92893);
			fileDataPojo.setLongitude(-10.27699);
			assertEquals(fileDataPojo, Utility.convertStringToJava(rowString, mapper));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testJsonToJavaConversionInValid() {
		String rowString = "{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}";
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			FileDataPojo fileDataPojo = new FileDataPojo();
			fileDataPojo.setUserId(1);
			fileDataPojo.setName("Alice Cahill");
			fileDataPojo.setLatitude(51.92893);
			fileDataPojo.setLongitude(20.12); // this figure is changed from requested string
			assertNotSame(fileDataPojo, Utility.convertStringToJava(rowString, mapper));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testFileDataToRowDataValid() {
		FileDataPojo fileDataPojo = null;
		RowData businessLayerData = null;
		fileDataPojo = new FileDataPojo();
		businessLayerData = new RowData(2, "Ian McArdle", 51.8856167, -10.4240951);
		fileDataPojo.setUserId(2);
		fileDataPojo.setName("Ian McArdle");
		fileDataPojo.setLatitude(51.8856167);
		fileDataPojo.setLongitude(-10.4240951);
		assertEquals(businessLayerData, Utility.convertFileRowToBlData(fileDataPojo));
	}

	public void testFileDataToRowDataInValid() {
		FileDataPojo fileDataPojo = null;
		RowData businessLayerData = null;
		fileDataPojo = new FileDataPojo();
		businessLayerData = new RowData(2, "Ian McArdle", 51.8856167, -10.4240951);
		fileDataPojo.setUserId(2);
		fileDataPojo.setName("Messi"); // changed name here
		fileDataPojo.setLatitude(51.8856167);
		fileDataPojo.setLongitude(-10.4240951);
		assertNotSame(businessLayerData, Utility.convertFileRowToBlData(fileDataPojo));
	}

	public void testIsNullEmptyQueue() {
		Queue<Object> obj = null;
		assertTrue(Utility.isNullOrEmptyQueue(obj)); // null queue check
		obj = new ArrayBlockingQueue<>(10);
		assertTrue(Utility.isNullOrEmptyQueue(obj)); // allocating memory to queue but its still empty
		obj.add(new String());
		assertFalse(Utility.isNullOrEmptyQueue(obj)); // method should return false now.
	}

	public void testIsNullEmptyList() {
		List<Object> obj = null;
		assertTrue(Utility.isNullOrEmptyList(obj)); // null list check
		obj = new ArrayList<>();
		assertTrue(Utility.isNullOrEmptyList(obj)); // allocating memory to list but its still empty
		obj.add(new String());
		assertFalse(Utility.isNullOrEmptyList(obj));// method should return false now.
	}

	public void testCalculateDistance() {
		Distance distance1;
		Distance distance2;
		distance1 = new Distance(32.92891, -11.27000);
		distance2 = new Distance(53.209428, -6.25700);
		assertEquals(2251.1D, Utility.getDistanceBetweenPoints(distance1, distance2));// method should calculate distance between two points and return 2251.1D
	}

	public void testIsCustomerInvited() {
		Distance distance1;
		Distance distance2;
		distance1 = new Distance(32.92891, -11.27000);
		distance2 = new Distance(53.209428, -6.25700);
		assertFalse(Utility.isInviteCustomer(distance1, distance2, Constants.INVITATION_RANGE));// method should return false since invitation range is 100 km and method returns 2251.1D
		assertTrue(Utility.isInviteCustomer(distance1, distance2, 2251.1));// boundary case for invitation, invited
		assertTrue(Utility.isInviteCustomer(distance1, distance2, 2251.2));// boundary case for invitation, invited
		assertFalse(Utility.isInviteCustomer(distance1, distance2, 2251.09));// boundary case for invitation, not invited
		assertFalse(Utility.isInviteCustomer(distance1, distance2, 0D));// 0 value case, not invited
	}

	public void testPrintInvitedCustomers() {
		RowData rowData = null;
		List<RowData> customersToBeInvited = null;
		assertEquals("No customer is invited", Utility.getInvitedCustomers(customersToBeInvited));// no customer invited message for null list
		customersToBeInvited = new ArrayList<>();
		assertEquals("No customer is invited", Utility.getInvitedCustomers(customersToBeInvited));// no customer invited message for empty list
		rowData = new RowData(2, "Ian McArdle", 51.8856167, -10.4240951);
		customersToBeInvited.add(rowData);
		assertEquals("Invited Customers are: Customer Name = [Ian McArdle]", Utility.getInvitedCustomers(customersToBeInvited));// no customer details printed
	}

}
