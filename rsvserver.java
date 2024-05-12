import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class rsvserver extends UnicastRemoteObject implements ReservationServer {
    private Map<Integer, String> businessClassSeats;
    private Map<Integer, String> economyClassSeats;
    private int soldBusinessClassSeats;
    private int soldEconomyClassSeats;

    public rsvserver() throws RemoteException {
        super();
        businessClassSeats = new HashMap<>();
        economyClassSeats = new HashMap<>();
        soldBusinessClassSeats = 0;
        soldEconomyClassSeats = 0;
    }

    // Method to list available seats
    @Override
    public String listAvailableSeats() throws RemoteException {
        StringBuilder result = new StringBuilder();

        // List business class seats
        result.append("business class:\n");
        result.append(3 - soldBusinessClassSeats > 0 ? 3 - soldBusinessClassSeats : 0).append(" seats at $500 each\n");
        result.append(soldBusinessClassSeats < 3 ? 2 : 5 - soldBusinessClassSeats ).append(" seats at $800 each\n");
        result.append("Seat numbers: ");
        listSeatsByClass(businessClassSeats, result, 1, 5);
        result.append("\n");

        // List economy class seats
        result.append("economy class:\n");
        result.append(soldEconomyClassSeats < 10 ? 10 - soldEconomyClassSeats : 0).append(" seats at $200 each\n");
        result.append(soldEconomyClassSeats >= 10 && soldEconomyClassSeats < 20 ? 20 - soldEconomyClassSeats : soldEconomyClassSeats < 10 ? 10 : 0).append(" seats at $300 each\n");
        result.append(soldEconomyClassSeats >= 20 ? 25 - soldEconomyClassSeats : 5).append(" seats at $450 each\n");
        result.append("Seat numbers: ");
        listSeatsByClass(economyClassSeats, result, 6, 30);
        return result.toString();
    }

    // Method to list seats by class
    public void listSeatsByClass(Map<Integer, String> seats, StringBuilder result, int startSeatNumber, int endSeatNumber) {
        for (int i = startSeatNumber; i <= endSeatNumber; i++) {
            if (!seats.containsKey(i)) {
                if (i != startSeatNumber) {
                    result.append(" ");
                }
                // Seat is available
                result.append(i);
            }
        }
    }

    // Method to calculate ticket price based on seat number
    private int calculateTicketPrice(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= 5) {
            if (soldBusinessClassSeats < 3) {
                return 500; // Business class, first 3 tickets
            } else {
                return 800; // Business class, next 2 tickets
            }
        } else if (seatNumber >= 6 && seatNumber <= 30) {
            if (soldEconomyClassSeats < 10) {
                return 200; // Economy class, first 10 tickets
            } else if (soldEconomyClassSeats < 20) {
                return 300; // Economy class, next 10 tickets
            } else {
                return 450; // Economy class, last 5 tickets
            }
        } else {
            return -1; // Invalid seat number
        }
    }

    // Method to reserve seats
    @Override
    public String reserveSeat(String passengerName, int seatNumber, String seatClass) throws RemoteException {
        if (seatClass.equals("business")) {
            if (seatNumber < 1 || seatNumber > 5) {
                return "Failed to reserve: invalid seat number";
            }
            if (businessClassSeats.containsKey(seatNumber)) {
                return "Failed to reserve: seat not available";
            }
            int seatPrice = calculateTicketPrice(seatNumber);
            if (seatPrice == 500 || seatPrice == 800) {
                soldBusinessClassSeats++;
            }
            // Seat is available for reservation
            businessClassSeats.put(seatNumber, passengerName);
            return "Successfully reserved seat #" + seatNumber + " for passenger " + passengerName;
        } else if (seatClass.equals("economy")) {
            if (seatNumber < 6 || seatNumber > 30) {
                return "Failed to reserve: invalid seat number";
            }
            if (economyClassSeats.containsKey(seatNumber)) {
                return "Failed to reserve: seat not available";
            }
            int seatPrice = calculateTicketPrice(seatNumber);
            if (seatPrice == 200 || seatPrice == 300 || seatPrice == 450) {
                    soldEconomyClassSeats++;
            }
            // Seat is available for reservation
            economyClassSeats.put(seatNumber, passengerName);
            return "Successfully reserved seat #" + seatNumber + " for passenger " + passengerName;
        } else {
            // Invalid seat class
            return "Failed to reserve: invalid seat class";
        }
    }

    @Override
    public String listBookedPassengers() throws RemoteException {
        StringBuilder result = new StringBuilder();
        // List passengers in both classes
        result.append(listPassengers(businessClassSeats, "business"));
        result.append(listPassengers(economyClassSeats, "economy"));
        return result.toString();
    }

    private String listPassengers(Map<Integer, String> seats, String seatClass) {
        StringBuilder result = new StringBuilder();
        // Iterate through all seats in a class and list the passengers
        for (Map.Entry<Integer, String> entry : seats.entrySet()) {
            int seatNumber = entry.getKey();
            String passengerName = entry.getValue();
            result.append(passengerName + " " + seatClass + " " + seatNumber + "\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        try {
            // Create a server and bind it to a registry
            ReservationServer server = new rsvserver();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ReservationServer", server);
            System.out.println("Reservation Server is running.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
