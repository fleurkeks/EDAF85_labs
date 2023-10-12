import lift.Monitor;
import lift.LiftView;
import lift.LiftThread;
import lift.PassengerThread;



public class MultiPassenger {
    public static void main(String[] args) {
        final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;

        LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        Monitor lift = new Monitor(NBR_FLOORS, MAX_PASSENGERS);

        for (int i = 0; i < 20; i++) {
           PassengerThread pass = new PassengerThread(view, lift);
            pass.start();
        }
        LiftThread liftThread = new LiftThread(view, lift, NBR_FLOORS, MAX_PASSENGERS);
        liftThread.start();
    }
}