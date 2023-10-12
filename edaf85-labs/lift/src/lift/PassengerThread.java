package lift;


public class PassengerThread extends Thread {
    private LiftView view;
    private Monitor monitor;

    public PassengerThread(LiftView view, Monitor monitor) {
        this.view = view;
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            Passenger pass = view.createPassenger();
            int fromFloor = pass.getStartFloor();
            int toFloor = pass.getDestinationFloor();
            pass.begin();
            monitor.waitForLift(fromFloor);
            pass.enterLift();
            monitor.enterLift(fromFloor, toFloor);
            pass.exitLift();
            monitor.exitLift(toFloor);
            pass.end();
        }
        }
    
}