import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ReservationServer extends Remote {
    String listAvailableSeats() throws RemoteException;
    void listSeatsByClass(Map<Integer, String> seats, StringBuilder result, int startSeat, int endSeat) throws RemoteException;
    String reserveSeat(String passengerName, int seatNumber, String seatClass) throws RemoteException;
    String listBookedPassengers() throws RemoteException;
}
