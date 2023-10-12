
package lift;

import java.util.stream.IntStream;



public class LiftThread extends Thread {

	private int currentFloor = 0;
	private Monitor monitor;
	private LiftView view;

	private final int NBR_FLOORS, MAX_PASSENGERS;

	public LiftThread(LiftView view, Monitor monitor, int nbr_floors, int maxPass) {
        this.view = view;
        this.monitor = monitor;
		NBR_FLOORS=nbr_floors;
		MAX_PASSENGERS=maxPass;
    }


	public void run(){
            while (true) {
                int totalWaiting = IntStream.of(monitor.getToEnter()).sum();

				//check that there are passengers either waiting or in lift, otherwise the lift should not move!
				if(!(totalWaiting==0&&monitor.passengersInLift()==0)){

					//if we are going up and we are not at top floor
					if (monitor.goingUp() && currentFloor < NBR_FLOORS - 1) {

					//wait for passengers at current floor, or passengers in general
                    if (((monitor.getToEnter()[currentFloor] > 0 && monitor.passengersInLift() < MAX_PASSENGERS) || monitor.getToExit()[currentFloor] > 0) ) {
                        view.openDoors(currentFloor);
                        monitor.waitForPassengers(currentFloor);
                        view.closeDoors();
                    } 
					//if no passengers to wait for move the lift until there is a passenger to wait for
					else {
                        monitor.moveLift(currentFloor, currentFloor + 1);
                        view.moveLift(currentFloor, currentFloor + 1);
                        currentFloor++;
                    }

                }
				//if we are going down 
				 else {
					//check if we are on floor 0
                    if (currentFloor > 0) {

                        if (((monitor.getToEnter()[currentFloor] > 0 && monitor.passengersInLift() < MAX_PASSENGERS) || monitor.getToExit()[currentFloor] > 0)) {
                            view.openDoors(currentFloor);
                            monitor.waitForPassengers(currentFloor);
                            view.closeDoors();
                        } 
						else {
                            monitor.moveLift(currentFloor, currentFloor - 1);
                            view.moveLift(currentFloor, currentFloor - 1);
                            currentFloor--;
                        }
                    }
                }
				}

                
            }
		}
	}
