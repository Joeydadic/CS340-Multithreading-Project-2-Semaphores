import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class Pickup_Order_Employee implements Runnable {

	
	private Thread pickupthread;
	static int[] pickUpArray = new int[1];
	static ArrayBlockingQueue<Integer> array = new ArrayBlockingQueue<Integer>(10);
	static Semaphore pickUpSemaphore = new Semaphore(1,true);
	public Pickup_Order_Employee() {
		this.pickupthread = new Thread(this);
	}
	
	
	
	public void start() {
		pickupthread.start();
	}
	
	public void run() {
		try {
		while(Customers.totalCustomers != 19) { //While their are customers to be served...
			
				Thread.sleep(1000);
			 {
				if(Main.semaphore.getQueueLength() >=1 ) {  //If there is a Customer thread waiting in the Semaphore queue.. 
					Main.semaphore.release();   //Release customer thread, work on their order.
					System.out.println("Pick up order employee has received Customer number " + array.element() + "'s order.");
					Random rng = new Random();
					int minTime = 1000;
					int maxTime = 10000;
					int workingOnOrder = minTime + rng.nextInt(maxTime);
					System.out.println("Pick up order employee is working on Customer number " + array.element() + "'s order.");
					Thread.sleep(workingOnOrder);
					System.out.println("Pick up order employee is finished with Customer number " + array.element() + "'s order.");
					Customers.billPay.release(); // Let the customer know they can pay their bill.
					array.remove(); //Remove them from the Pick-up employee thread.
					
				}
		}
					
				}
			System.out.println("The Pick-Up Employee is done with his work for the day and has left the restaurant.");  //Message for the pick-up employee to finish his shift after all customers have left.
			
	
	
	} catch(InterruptedException e) {
		
	}
}
}