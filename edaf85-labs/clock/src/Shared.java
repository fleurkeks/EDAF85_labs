
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



public class Shared {
    private final ReadWriteLock m_lock = new ReentrantReadWriteLock();

    private final Time m_currentTime = new Time();
    private final Time m_alarmTime = new Time();
    private boolean m_alarmArmed = false;

    public Time get_time() {
        m_lock.readLock().lock();
        var copy  = new Time(m_currentTime);
        m_lock.readLock().unlock();

        return copy;
    }

    public void set_time(int h, int m, int s) {
        m_lock.writeLock().lock();

        m_currentTime.hour = h;
        m_currentTime.minute = m;
        m_currentTime.second = s;

        m_lock.writeLock().unlock();
    }

    public void tick() {
        m_lock.writeLock().lock();

        m_currentTime.tick();

        m_lock.writeLock().unlock();
    }

    public Time get_alarm() {
        m_lock.readLock().lock();
        var copy  = new Time(m_alarmTime);
        m_lock.readLock().unlock();

        return copy;
    }

    public void set_alarm(int h, int m, int s) {
        m_lock.writeLock().lock();

        m_alarmTime.hour = h;
        m_alarmTime.minute = m;
        m_alarmTime.second = s;

        m_lock.writeLock().unlock();
    }

    public boolean alarm_armed() {
        m_lock.readLock().lock();
        var copy  = m_alarmArmed;
        m_lock.readLock().unlock();

        return copy;
    }
    public void toggle_alarm() {
        m_lock.writeLock().lock();
        m_alarmArmed = !m_alarmArmed;
        m_lock.writeLock().unlock();
    }
}








