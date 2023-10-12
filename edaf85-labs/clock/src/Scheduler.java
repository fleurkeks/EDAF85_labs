import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Scheduler extends Thread{
    private final ArrayList<Semaphore> m_signals = new ArrayList<>();

    private final Shared m_shared;

    Scheduler(Shared shared) {
        m_shared = shared;
    }

    public Semaphore register() {
        synchronized (m_signals) {
            m_signals.add(new Semaphore(0));

            return m_signals.get(m_signals.size() - 1);
        }
    }

    private void signal() {
        synchronized (m_signals) {
            for (var s : m_signals) {
                s.release();
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            final var time = System.currentTimeMillis();
            //m_shared.tick();
            signal();

            try {
                Thread.sleep(1000 - time % 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }




}
