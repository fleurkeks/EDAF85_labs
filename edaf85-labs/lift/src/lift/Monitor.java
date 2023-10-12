package lift;


public class Monitor {
    private int[] toEnter;
    private int[] toExit;
    private int currentFloor;
    private int passengersInLift;

    private boolean doorsOpen = false;
    private boolean isMoving = false;
    private boolean goingUp = true;

    //nbr people walking on or off lift
    private int peopleMoving;

    private final int NBR_FLOORS, MAX_PASSENGERS;

//initialise monitor
    public Monitor(int nbrFloors, int maxPass) {
        toEnter = new int[nbrFloors];
        toExit = new int[nbrFloors];
        NBR_FLOORS = nbrFloors;
        MAX_PASSENGERS=maxPass;
    }

// controls movement from one floor to another
    public synchronized void moveLift(int fromFloor, int toFloor) {
        //while not moving wait (ex if someone enters or exits)
        while (!isMoving) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //determine direction
        goingUp = (toFloor > fromFloor);
        currentFloor = fromFloor;

        //increment or decrement until destination is reached
        while (currentFloor != toFloor) {
            if (goingUp) {
                currentFloor++;
            } else {
                if (--currentFloor == 0) {
                    goingUp = true;
                }
            }
        }
    }

    //open lift doors and keep them open while there are people entering or exiting
    public synchronized void waitForPassengers(int currentFloor) {
        doorsOpen = true;
        notifyAll();
        while ((toEnter[currentFloor] > 0 && passengersInLift < MAX_PASSENGERS) || toExit[currentFloor] > 0 || peopleMoving > 0 ) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //close doors and allow lift to move
        isMoving = true;
        doorsOpen = false;
        notifyAll();
    }

// person enters lift
    public synchronized void enterLift(int fromFloor, int toFloor) {
        toEnter[fromFloor]--;
        passengersInLift++;
        toExit[toFloor]++;
    
        //person is now standing in the lift
        peopleMoving--;
        notifyAll();
        moving(toFloor);
    }

    //a person is moving to floor and then exits the lift
    private synchronized void moving(int floor) {

        //wait until we have reached floor
        while (floor != currentFloor) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        peopleMoving++;
        notifyAll();
    }

    //person exits lift
    public synchronized void exitLift(int floor) {
        toExit[floor]--;
        //lift is allowed to move
        isMoving = true;
        //person has left lift
        passengersInLift--;
        peopleMoving--;
        notifyAll();
    }

    //passenger waits for lift doors to open at start 
    public synchronized void waitForLift(int floor) {
        //wait until the lift has stopped somewhere
        if(!isMoving) notifyAll();
        toEnter[floor]++;

        //wait until: doors open, there is space in lift and its at start floor
        while (!doorsOpen || passengersInLift >= MAX_PASSENGERS || currentFloor != floor || peopleMoving + passengersInLift >= MAX_PASSENGERS) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //passenger is now moving onto the lift
        peopleMoving++;
        notifyAll();
    }

    public boolean goingUp() {
        return goingUp;
    }

    public int passengersInLift() {
        return passengersInLift;
    }

    public int[] getToEnter() {
        return toEnter;
    }

    public int[] getToExit() {
        return toExit;
    }

    public boolean doorsOpen() {
        return doorsOpen;
    }
}