package Models;

import java.util.ArrayList;

public class Week {

    private ArrayList<Day> week;

    public void addDay(Day day){
        week.add(day);
    }

    public Day getDay(int i){
        return week.get(i);
    }
}
