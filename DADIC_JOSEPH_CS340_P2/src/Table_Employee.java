import java.util.Random;
import java.util.concurrent.*;




public class Table_Employee implements Runnable {
		private Thread tableEmployee;
		
		static Semaphore waitForOrder = new Semaphore(0, true);  //Semaphore to wait for Customer Threads orders.
		static Semaphore waitForPayment = new Semaphore(0, true); //Semaphore to wait for Customer Payment.
		static Semaphore waitForCustomers = new Semaphore(0,true); //Semaphore to wait for Customers to be in groups of four.
		static Semaphore waitToSeatCustomers = new Semaphore(0,true); //Semaphore to wait for all four customers to be walking together to the table.
		static Boolean tableEmployeeGo = false; //Boolean used to distinguish between customers in groups of 4 and the last residual customers that cant be in groups of 4.
		static int totalCustomers = 0;  //Count for all of the customers that have left the restaurant after paying for their food.
		static int sizeHelper;         //Size helper int. Created to see how many odd-ball customers are left that aren't in groups of four.
		static ArrayBlockingQueue<Integer> tableOne = new ArrayBlockingQueue<Integer>(4); //Table one, with four seats.
		static ArrayBlockingQueue<Integer> tableTwo = new ArrayBlockingQueue<Integer>(4); //Table two, with four seats.
		static ArrayBlockingQueue<Integer> tableThree = new ArrayBlockingQueue<Integer>(4); //Table three, with four seats. 
		static int employeeCount = 0;  //Counter just used to name the Table_Employee threads and give them an int value to their name.
		
		
		public Table_Employee() {
		this.tableEmployee = new Thread(this);
		}
		
		
		
		
		
		public void start() {
			employeeCount++;
			this.tableEmployee.setName("Table Employee " + employeeCount);
			this.tableEmployee.setPriority(employeeCount);
			System.out.println(this.tableEmployee.getName() + " has arrived to work.");
			this.tableEmployee.start();
		}
		
		
		
		
		
		public void run() {
			try {
			while(Customers.totalCustomers != 19) {
				Thread.sleep(1000);
				
				if(tableEmployeeGo == true) {             //Let the first employee handle the first interaction with customers.
					this.tableEmployee.setPriority(1);
				}
				
				//If there are four customers in the queue, release all four of them out of their wait to be seated.
				if(Main.dineInSemaphore.getQueueLength() >= 4 && tableEmployee.getPriority() != 2) {
				for(int i  = 0 ; i < 4 ; i++) {
					Main.dineInSemaphore.release();
				}
				
				
				//Wait for all four customers to walk to their table.
				waitToSeatCustomers.acquire();
				
				//Collect data from the four customers to add to the table.
				int customerSeatOne = Customers.customerDineInData.poll();
				int customerSeatTwo = Customers.customerDineInData.poll();
				int customerSeatThree = Customers.customerDineInData.poll();
				int customerSeatFour = Customers.customerDineInData.poll();
				
				//Initialize the customerCount back to 0.
				
				Customers.customerCount = 0;
				
				
				
				if(tableOne.isEmpty() && tableOne.size() != 4) {
					
					System.out.println(tableEmployee.getName() + " has invited Customer's " + customerSeatOne +
							 ", " + customerSeatTwo + ", " + customerSeatThree + ", " + customerSeatFour + " to Table 1.");
					
					tableOne.add(customerSeatOne);
					tableOne.add(customerSeatTwo);
					tableOne.add(customerSeatThree);
					tableOne.add(customerSeatFour);
					
					
					customerSeatOne = 0;
					customerSeatTwo = 0;
					customerSeatThree = 0;
					customerSeatFour = 0;
					tableEmployeeGo = true;
					
					//After all of the customers have been given to the table, release the customers so they can sit, look at the menu, and place their orders.
					
					
					for(int i = 0; i < 4; i++) {
						Customers.waitToBeSeated.release();
					}
					
					
					//Table Employee waits for the customers to order.
					
					
					waitForOrder.acquire();
					Customers.ordersPlaced = 0;
					
					System.out.println(tableEmployee.getName() + " is going to the kitchen to retrieve the orders.");
					Random orderGet = new Random();
					int minGet = 1000;
					int maxGet = 10000;
					int retrieveOrder = minGet + orderGet.nextInt(maxGet);
					Thread.sleep(retrieveOrder);
					
					//After the Table Employee has received the food, release the customers so that they may eat.
					System.out.println(tableEmployee.getName() + " has received the orders for this table.");
					for(int i = 0; i < 4; i++) {
						Customers.customerWaitForFood.release();
					}
					
					
					//Wait for the customers to finish eating, and wait until they are done.
					//Release them to be able to pay for their bill after they have been given their bill.
					waitForPayment.acquire();
					Customers.doneEating = 0;
					System.out.println(tableEmployee.getName() + " has given the table their bill.");
					for(int i = 0; i < 4; i++) {
						Customers.customersWaitToPay.release();
					}
					
					
					//Else-if statement is the same as above except for Table Two.
					
				} else if(tableTwo.isEmpty() && tableTwo.size() != 4) {
					System.out.println(tableEmployee.getName() + " has invited Customer's " + customerSeatOne +
							 ", " + customerSeatTwo + ", " + customerSeatThree + ", " + customerSeatFour + " to Table 2.");
					tableTwo.add(customerSeatOne);
					tableTwo.add(customerSeatTwo);
					tableTwo.add(customerSeatThree);
					tableTwo.add(customerSeatFour);
					
					for(int i = 0; i < 4; i++) {
						Customers.waitToBeSeated.release();
					}
					tableEmployeeGo = true;
					waitForOrder.acquire();
					Customers.ordersPlaced = 0;
					
					System.out.println(tableEmployee.getName() + " is going to the kitchen to retrieve the orders for table 2.");
					Random orderGet = new Random();
					int minGet = 1000;
					int maxGet = 10000;
					int retrieveOrder = minGet + orderGet.nextInt(maxGet);
					Thread.sleep(retrieveOrder);
					
					System.out.println(tableEmployee.getName() + " has received the orders for Table 2.");
					for(int i = 0; i < 4; i++) {
						Customers.customerWaitForFood.release();
					}
					
						waitForPayment.acquire();
						Customers.doneEating = 0;
						System.out.println(tableEmployee.getName() + " has given Table 2 Their bill.");
					for(int i = 0; i < 4; i++) {
						Customers.customersWaitToPay.release();
					}
					
					
					//Same as above and the previous table, but for Table Three.
					
					
				}else if(tableThree.isEmpty() && tableTwo.size() !=4) {
					System.out.println(tableEmployee.getName() + " has invited Customer's " + customerSeatOne +
							 ", " + customerSeatTwo + ", " + customerSeatThree + ", " + customerSeatFour + " to Table 3.");
					tableThree.add(customerSeatOne);
					tableThree.add(customerSeatTwo);
					tableThree.add(customerSeatThree);
					tableThree.add(customerSeatFour);
					
					for(int i = 0; i < 4; i++) {
						Customers.waitToBeSeated.release();
					}
					tableEmployeeGo = true;
					waitForOrder.acquire();
					Customers.ordersPlaced = 0;
					
					System.out.println(tableEmployee.getName() + " is going to the kitchen to retrieve the orders for Table 3.");
					Random orderGet = new Random();
					int minGet = 1000;
					int maxGet = 10000;
					int retrieveOrder = minGet + orderGet.nextInt(maxGet);
					Thread.sleep(retrieveOrder);
					
					System.out.println(tableEmployee.getName() + " has received the orders for Table 3.");
					for(int i = 0; i < 4; i++) {
						Customers.customerWaitForFood.release();
					}
					
						waitForPayment.acquire();
						Customers.doneEating = 0;
						System.out.println(tableEmployee.getName() + " has given Table 3 their bill.");
					for(int i = 0; i < 4; i++) {
						Customers.customersWaitToPay.release();
					}
				}
				
				
				
				}	
				
					//This portion was created for the residual customers. If their have been at least 17 or more total customers and people are still dining in,
					//This portion handles that scenario.
				
					if(Main.dineInSemaphore.getQueueLength() < 4 && Main.dineInSemaphore.getQueueLength() > 0 && Customers.totalCustomers >= 17) {
						//sizeHelper is used to distinguish how many people are actually waiting, since it won't be four as the program was designed to handle.
						
						sizeHelper = Main.dineInSemaphore.getQueueLength();
					
					//Release the residual customers based off how many of them are in the queue.
						
					for(int i = 0; i < Main.dineInSemaphore.getQueueLength(); i++) {
						Main.dineInSemaphore.release();
					}
					
					//Boolean lastCall used to allow them to break through the barrier of the counters requiring "4" for the earlier
					//Customers.
					Customers.lastCall = true;
					
					
					//Wait for the residual customers to be seated, place them at whatever table is empty.
					waitToSeatCustomers.acquire();
					for(int i = 0; i < sizeHelper; i++) {
						int x = Customers.customerDineInData.poll();
						if(tableOne.isEmpty()) {
						tableOne.add(x);
					} else if(tableTwo.isEmpty()) {
						tableTwo.add(x);
					} else if(tableThree.isEmpty()) {
						tableThree.add(x);
					}
						
					}
					
					//Release the residual customers.
					
					System.out.println(this.tableEmployee.getName() + " has seated the residual customers at an open table.");
					for(int i = 0; i < sizeHelper; i++) {
						Customers.waitToBeSeated.release();
					}
					
					
					waitForOrder.acquire();
					Customers.ordersPlaced = 0;
					
					System.out.println(tableEmployee.getName() + " is going to the kitchen to retrieve the orders for the remaining customers at this table.");
					Random orderGet = new Random();
					int minGet = 1000;
					int maxGet = 10000;
					int retrieveOrder = minGet + orderGet.nextInt(maxGet);
					Thread.sleep(retrieveOrder);
					
					System.out.println(tableEmployee.getName() + " has received the orders for the remaining customers at this table.");
					for(int i = 0; i < sizeHelper ; i++) {
						Customers.customerWaitForFood.release();
					}
					
						waitForPayment.acquire();
						Customers.doneEating = 0;
						System.out.println(tableEmployee.getName() + " has given the remainding customers at the table their bill.");
						
					for(int i = 0; i < sizeHelper; i++) {
						Customers.customersWaitToPay.release();
					}
					sizeHelper = 0;
				}
					
			}	
			System.out.println(this.tableEmployee.getName() + " has finished their shift and has left the restaurant."); // Closing message after all of the customers have left.
			
			}catch(InterruptedException e) {
				
			}
		}
}
