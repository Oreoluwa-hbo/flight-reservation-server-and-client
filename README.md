# flight-reservation-server-and-client

#RPC/RMI
## File Descriptions:

rsvserver.java: This file contains the server code, which implements the reservation system. It includes methods for listing available seats, reserving seats, and listing booked passengers.

rsvclient.java: This file contains the client code, which allows users to interact with the reservation system. It provides a command-line interface for listing available seats, viewing the passenger list and reserving seats.

ReservationServer.java: This interface defines the methods implemented by the server to provide reservation functionality and defines the methods that the client can call on the server to interact with the reservation system. 
#-Important note: ReservationServer.java should be added to the src of both Programs-#

## Usage Instructions:
### Compiling:
To compile the server and client programs, you can use the `javac` command:

javac rsvserver.java
javac rsvclient.java


Start the Server: Run the server by executing the rsvserver class. This will create a reservation server that listens for client requests.

java rsvserver

Run the Client: Run the client by executing the rsvclient class with appropriate command-line arguments. You need to specify the server's name (IP) address and the command. For example, to list available seats, use the following command:

java rsvclient list <server_name>

To Reserve a seat:
java rsvclient reserve <server_name> <class> <passenger_name> <seat_number>

To list booked passengers:
java rsvclient passengerlist <server_name>


Example Usage

List available seats:
java rsvclient list 10.19.9.2

Reserve a seat:
java rsvclient reserve 10.19.9.2 economy Alice 3

list booked passengers:
java rsvclient passengerlist 10.19.9.2


