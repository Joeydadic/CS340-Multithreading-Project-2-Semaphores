import java.util.concurrent.*;
import java.util.Random;


public class Customers implements Runnable {
	Thread customerThread;
	Semaphore customerSemaphore;
	static Semaphore billPay = new Semaphore(0 ,true);		//Semaphore created to have Pick-Up customers wait until they can pay their bill.
	static Semaphore customerWaitForFood = new Semaphore(0, true);//Semaphore created to have Dine-in customers wait for the Table_Employee to bring them their orders.
	static Semaphore customersWaitToPay = new Semaphore(0,true); //Semaphore created to have Dine-in customers wait to be able to pay for their tables bill.
	static Semaphore waitToBeSeated = new Semaphore(0,true); //Semaphore created to have the Dine-in customers wait for a Table Employee to be ready to seat them at their table.
	static int customerThreadName = 0;       //Counter, just to give the Customer Semaphores a number attached to their name. 
	static int ordersPlaced = 0;			//Counter used to collect how many dine-in customers placed their order at the table.
	static int doneEating = 0;				//Counter used to collect how many dine-in customers have finished eating.
	static int customerCount = 0;          //Counter used to organize the critical section and making sure four customers at a time get seated.
	static int tableRelease = 0;		   //Counter used to signal the table and seats of the previous preoccupied table able to be cleared.
	public static int totalCustomers = 0; //A counter, used to signal the closing time of the restaurant AND help with the remaining customers.
	static int residualOrders = 0;
	static boolean lastCall = false; //Boolean variable created for the sole purpose of helping the "remaining" customers that are not in a group of 4 be seated.
	static ArrayBlockingQueue<Integer> customerPickUpData = new ArrayBlockingQueue<Integer>(20); //queue created just for data transfer between classes.
	static ArrayBlockingQueue<Integer> customerDineInData = new ArrayBlockingQueue<Integer>(20); //queue created just for data transfer between classes.
	
	public Customers(Semaphore customerSemaphore) {
		this.customerThread = new Thread(this);
		this.customerSemaphore = customerSemaphore;
	}
	//Method numberSplit created to extract the Customer Number being served for data transfer between structures.
	public int numberSplit(Thread x) {
		String y = this.customerThread.getName();
		String splitNumber = y.replaceAll("[^0-9]","");
		return Integer.parseInt(splitNumber);
	}
	
	public void start() {
		
		try {
			Random rng = new Random();
			int minimum = 1000;
			int maximum = 10000;
			int sleepyTime = minimum + rng.nextInt(maximum);
			Thread.sleep(sleepyTime);
			customerThreadName++;
			customerThread.setName("Customer " + customerThreadName);
			customerThread.start();
			
		}   catch(InterruptedException e) {
			
		}
	}
	
	
	
	public void run() {
		try {
		System.out.println(customerThread.getName() + " has arrived.");
		
		
		if(Main.takeOutOrDineIn > 2) {
			
			System.out.println(customerThread.getName() + " is dining in.");
			
			
			
			//Customers that are dining in are automatically waiting in the queue, set by Main.dineInSemaphore.acquire();
			//This semaphore is released by the Table_Employee threads when there are four customers in the wait.
			Main.dineInSemaphore.acquire(); 
			
			customerDineInData.add(numberSplit(this.customerThread)); //Necessary for data transfer. Signals the customer name of the current thread.
			customerCount++;
			if(customerCount == 4 || lastCall == true) {
				Table_Employee.waitToSeatCustomers.release();       //When there are four customers out of the queue, Table_Employee releases to seat them.
			}
			
			waitToBeSeated.acquire(); //Customers wait for Table_Employee to seat them.
			
			//Customer threads who have entered this section have been released and seated by the Table Employee
			System.out.println(this.customerThread.getName() + " has been seated.");
			
			
			
			//Random time designation for the customers to place their order.
			Random checkMenuPlaceOrder = new Random();
			int minTime = 1000;
			int maxTime = 10000;
			int placeOrder = minTime + checkMenuPlaceOrder.nextInt(maxTime);
			Thread.sleep(placeOrder);
			System.out.println(this.customerThread.getName() + " has placed their order.");
			ordersPlaced++;
			
			
			
			
			//Once all four customers have placed their order OR the residual customers have placed their order, release Employee to get the food for the table.
			if(ordersPlaced == 4 || totalCustomers > 16 && ordersPlaced == Table_Employee.sizeHelper) {
			Table_Employee.waitForOrder.release();
			}
			//Semaphore for customers to wait for food. Once released by the employee, they eat for a random time.
			customerWaitForFood.acquire();
			Random customersEating = new Random();
			int minEat = 1000;
			int maxEat = 10000;
			int doneEat = minEat + customersEating.nextInt(maxEat);
			Thread.sleep(doneEat);
			doneEating++;
			
			System.out.println(this.customerThread.getName() + " has finished eating their dinner.");
			//Once all four customers have ate their food, signal Employee to get their bill.
			if(doneEating == 4 || totalCustomers > 16 && doneEating == Table_Employee.sizeHelper) {
				Table_Employee.waitForPayment.release();
			}
			
			//Customers wait to pay for their bill.
			customersWaitToPay.acquire();
			System.out.println(customerThread.getName() + " has paid their portion of their tables bill and left the restaurant.");
			tableRelease++;
			
			
			//Once all four customer threads have left, clear up the table that they were just residing in.
			if(tableRelease == 4 || totalCustomers > 16 && tableRelease == Table_Employee.sizeHelper) {
				for(int i = 0; i < 4; i++) {
					Table_Employee.tableOne.poll();
					tableRelease--;
				}
			}
			
			totalCustomers = totalCustomers + 1;   //Count for total customers that have went through the restaurant.
			
			
		} else {
			
			
			
			System.out.println(customerThread.getName() + " is picking up.");
			//Pick-up customers wait if there is already one customer interacting with the Pick-up Employee
			Main.semaphore.acquire();
			
			
			//Add the customer number from the Customer Thread that is picking up to a queue for data retrieval.
			customerPickUpData.add(numberSplit(this.customerThread));
			Pickup_Order_Employee.array.add(customerPickUpData.poll());
			
			//Wait for the Pick-up Employee to signal that the bill is ready to pay
			billPay.acquire();
			
			//Random sleep as the Customer waits to pay for their food.
			Random randomSleep = new Random();
			int minimum = 1000;
			int maximum = 10000;
			int customerPaying = minimum + randomSleep.nextInt(maximum);
			Thread.sleep(customerPaying);
			System.out.println(this.customerThread.getName() + " has paid for their order and is leaving now.");
			
			
			
			totalCustomers = totalCustomers + 1; //Total customer count used for residual dine in customers and closing of restaurant.
		}
		
		
		} catch(InterruptedException e) {
			
		}
		
		
	}
}
