import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rsvclient {
    public rsvclient() {
    }

    public static void main(String[] args) {
        // Check if the correct number of command-line arguments is provided
        if (args.length < 2) {
            System.err.println("Usage: java rsvclient <command> <server_name> [other parameters]");
            System.exit(1);
        }

        String command = args[0];
        String serverName = args[1];

        try {
            // Get the RMI registry on the specified server host
            Registry registry = LocateRegistry.getRegistry(serverName);

            // Look up the remote ReservationServer object from the RMI registry.
            ReservationServer server = (ReservationServer) registry.lookup("ReservationServer");
            String result;

            // Use the 'list' command to list available seats
            if (command.equals("list")) {
                result = server.listAvailableSeats();
                System.out.println(result);
            }
            // Use the 'reserve' command to reserve a seat
            else if (command.equals("reserve")) {
                if (args.length < 5) {
                    System.err.println("Usage: java rsvclient reserve <server_name> <class> <passenger_name> <seat_number>");
                    System.exit(1);
                }

                String seatClass = args[2];
                String passengerName = args[3];
                int seatNumber = Integer.parseInt(args[4]);
                result = server.reserveSeat(passengerName, seatNumber, seatClass);
                System.out.println(result);

                // Use the 'passengerlist' command to list booked passengers
            } else if (command.equals("passengerlist")) {
                result = server.listBookedPassengers();
                System.out.println(result);
            } else {
                System.err.println("Unknown command: " + command);
            }
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
