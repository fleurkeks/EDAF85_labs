
import clock.AlarmClockEmulator;
import clock.io.Choice;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;
import java.util.concurrent.Semaphore;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput in = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        final var shared = new Shared();
        final var scheduler = new Scheduler(shared);
        scheduler.start();

        //updating clock thread

        final var updateClock = new Thread(() -> {
            final var signal = scheduler.register();

            try {
                while (true) {
                    signal.acquire();
                    shared.tick();
                    final var time = shared.get_time();
                    out.displayTime(time.hour, time.minute, time.second);
                     if (shared.alarm_armed()) {
                            System.out.print("hej");
                        if (shared.get_alarm().equals(time)) {
                            out.alarm();
                            System.out.print("alarm");
                        }
                    }
                    
                }
            } catch (InterruptedException e) {
                System.err.println("Timer thread caught an InterruptedException exception:");
                System.err.println(e.getMessage());
            }
        
        });

        updateClock.start();

        //setting alarm thread
        final var updateAlarm = new Thread(() -> {
            final var signal = scheduler.register();
            

            while (true) {
                try {
                    signal.acquire();

                    if (shared.alarm_armed()) {
                        if (shared.get_alarm().equals(shared.get_time())) {
                            out.alarm();
                        }
                       
                    } 
                } catch (InterruptedException e) {
                    System.err.println("Alarm thread caught an InterruptedException exception:");
                    System.err.println(e.getMessage());
                }
            }
        });

        //updateAlarm.start();

        //main thread, choices for user input

        while (true) {
            in.getSemaphore().acquire();
            UserInput userInput = in.getUserInput();
            Choice c = userInput.choice();
            switch (c) {
                case SET_TIME: {
                    shared.set_time(userInput.hours(), userInput.minutes(), userInput.seconds());
                }
                    break;
                case SET_ALARM: {
                    shared.set_alarm(userInput.hours(), userInput.hours(), userInput.seconds());
                }
                    break;
                case TOGGLE_ALARM: {
                    shared.toggle_alarm();
                    out.setAlarmIndicator(shared.alarm_armed());
                }
                    break;
                default:
            }
        }
    }
}
