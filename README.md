# CS340-Multithreading-Project-2-Semaphores
This project was done for my CS 340 Operating Systems Principles course at CUNY Queens College. Dr. Simina Fluture gave us an arbitrary scenario in which each student individually was required to implement using Multithreading in Java with the usage of Semaphores.

Below is the example of the arbitrary scenario in which we were given for the assignment.

DO NOT SUBMIT BOTH TYPES OF IMPLEMENTATION: EITHER YOU SUBMIT THE 
PSEUDO-CODE, OR YOU SUBMIT THE JAVA-CODE.

Directions: You are asked to synchronize the threads of the following story using semaphores
and operations on semaphores. Do NOT use busy waiting or any other synchronization tools 
besides the methods (specified below) of the semaphore class. Please refer to and read the 
project notes carefully, tips and guidelines before starting the project.
Plagiarism is not accepted. Receiving and/or giving answers and code from/to other students 
enrolled in this or a previous semester, or a third source including the Internet is academic 
dishonesty subject to standard University policies. 

                                  Dining out or just Pick up
Finally, the work day has ended. It is time to eat. Some are dining out, some will just pick up 
their order.

Customers commute to the diner in different ways (simulated by sleep of random time). Once 
they get to the restaurant, they decide if they will pick up the order or dine in (by generating a 
random number between 1 and 10; if the number is greater than 2 – dine in, else just pick up). 
If the customer decided to pick up, (s)he will get on the pickup line and wait to place the order. 
At the pickup line there is only one employee, let’s name it pickup_order_employee. He will 
work on the order (simulated by sleep of random time). The pickup_order_employee will signal
the customer when the order and the bill are ready. Next the customer will pay (simulated by 
sleep of random time) while the employee will wait to be paid. After that, (s)he will leave. The 
pickup_order_employee should have a while loop in its code. It will be able to terminate if the 
restaurant closes.

If the customer decided to dine in, it will wait to be seated. The restaurant is very popular so 
every table needs to be in use before another table can be used. There are numTables tables. 
Each table has numSeats seats. Customers are grouping in groups of size numSeats and let 
the table_employee know that they are ready (waiting) to be seated. When a table is or 
becomes available and there is a complete group of customers waiting, the table_employee will 
invite that group of customers to the table. Note: There is a possibility that the very last 
group/table may not be complete, make sure you consider this situation as well. 
Once seated, the customer will check the menu, place the order (simulate by sleep of random 
time). The table_employee who seated the customers, will wait until all the orders are in place. 

CSCI 340, SP22 – p2
Instructor: Simina Fluture, PhD
Next the table_employee will go to the kitchen, and get the food while the customers are 
waiting. The table_employee will get back with the food and customers will stop the initial wait 
and start a new wait simulating the dining experience. 
Now is the time for the table_employee to check if there is another group to be seated. If there 
is another group to be seated then the table_employee will take care of that group. Everything is 
repeated until the orders are in place. 

Customers of the same table eat (sleep of random time). Once all the customer at the table are 
done eating (you might want to use a counter and mutex) , they let the table_employee know
they are ready to pay. Here it is your choice of using a semaphore or a shared variable.
There will be only one bill. Once the table_employee takes care of the payment, the customer(s)
will leave (use the implementation of a source graph). Make sure that the table is also released.
The restaurant closes after all customer threads have been served and have paid (one way or 
another). 
-------------------------------------------------------------------------------------------------------------------------------
Default values
numCustomers = 20
numTables = 3
numSeats = 4
num_table_employee = 2
