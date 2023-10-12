

public class Time{
    public int hour;
    public int minute;
    public int second;

    @Override
    public boolean equals(Object o) {
        if(o instanceof Time) {
            final var t = (Time)o;

            return t.hour == hour && t.minute == minute && t.second == second;
        }
        return false;
    }

    Time() {}

    Time(Time t) {
        hour = t.hour;
        minute = t.minute;
        second = t.second;
    }

    public void tick() {
        if(second >= 59) {
            second  = 0;

            if(minute >= 59) {
                minute  = 0;

                if(hour >= 23) {
                    hour  = 0;
                }
                else hour += 1;
            }
            else minute += 1;
        }
        else second += 1;
    }

}