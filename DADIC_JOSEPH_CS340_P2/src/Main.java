//Name: Joseph Dadic
//Course: CS 340
//Professor: Professor Fluture

import java.util.Random;
import java.util.concurrent.Semaphore;


public class Main {
	static Customers[] customer; //Initialization of Customer Class & Thread
	static Pickup_Order_Employee Pickupemployee; //Initialization of Pickup_Order_Employee Class & Thread
	static Table_Employee[] Tableemployee; //Initialization of Table_Employee class and threads
	static int takeOutOrDineIn;            // Variable I created to declare Picking up or dining in in the main method.
	static Semaphore semaphore = new Semaphore(1, true); //Semaphore for the pick-up thread. Set to (1,true) to allow the first customer to go through.
	static Semaphore dineInSemaphore = new Semaphore(0, true); //Semaphore for the dine-in threads. Set to 0 so they automatically "wait" and get into the queue for the Table_Employee.
	
	public static void main(String[] args) {
		
		customer = new Customers[21];  //Initialize array for Customer Threads
		Pickupemployee = new Pickup_Order_Employee();//Create Pickup_Order_Employee thread in the main
		Tableemployee = new Table_Employee[3];      //Initialize array for Table_Employees
		for(int i = 1; i < 3; i++) {                 //For-loop to start both of the Table_Employee threads, creation of object + start of Threads.
			Tableemployee[i] = new Table_Employee();  
			Tableemployee[i].start();
		}
		
		Pickupemployee.start(); //Starting Pickupemployee Thread
		
			for(int i = 1; i < 21; i++) {                  // This time around, I used the random Pick up or Dine in method
			Random pickupordinein = new Random();			// in the for-loop, so I could designate the appropriate semaphore for said thread based on
			int minPickup = 1;								// if the customer is Dining in or Picking up
			int maxDinein = 10;
			takeOutOrDineIn = minPickup + pickupordinein.nextInt(maxDinein);
			
			if(takeOutOrDineIn > 2) {
				customer[i] = new Customers(dineInSemaphore);
				customer[i].start();
			} else {
				customer[i] = new Customers(semaphore);
				customer[i].start();
			}
			
		}
		
	}

}
