package Generators;

import Examples.ExampleNurseGenerator;
import Models.Nurse;
import Models.Schedule;
import Models.Week;

import java.util.*;

public class Scheduler {

    private static final int NUM_NURSE = 16;
    private static final int NUM_WEEKS = 5;
    private static final int NUM_DAYS = 7;

    private ArrayList<Nurse> nurses = new ArrayList<Nurse>();

    private HashMap<Integer, Integer> night_shift_stack = new HashMap<Integer, Integer>();

    public Scheduler() {
        ExampleNurseGenerator nurse_generator = new ExampleNurseGenerator();
        nurses = nurse_generator.getNurses();
    }

    public void generateSchedule(){
        Random rand = new Random();
        int num_shifts;
        int tmp_nurse_id;
        for (int i = 0; i < NUM_WEEKS; i++) {
            for (int j = 0; j < NUM_DAYS; j++) {
                num_shifts = (j == 6 || j == 5) ? 7 : 10;
                Integer[] day = new Integer[num_shifts];
                for (int k = 0; k < num_shifts; k++) {
                    tmp_nurse_id = rand.nextInt(16)+1;
                    if(hardConstrant(day, tmp_nurse_id, j, k)){
                        day[k] = tmp_nurse_id;//dodajemy i lecimy dalej
                    } else {
                        k--;//ponawaimy losowanie
                    }
                }
                System.out.println(Arrays.toString(day));
                System.out.print("\n");
            }
        }
    }
    /*
    * day#0 - pon * day#1 - wt * day#2 - sr * day#3 - czw * day#4 - pt * day#5 - sob * day#6 - nd
    * shift#0 - day * shift#1 - early * shift#2 - late * shift#3 - night
    */
    public int getNurseOnShift(int iday, int ishift){
        int num_nurse;
        num_nurse = (iday == 6 || iday == 5) ? (ishift == 3 ? 1 : 2) : (ishift == 3 ? 1 : 3);
        return num_nurse;
    }


    public boolean hardConstrant(Integer[] day, int id, int iday, int ishift){
        if(oneNurseInDayCons(day, id) && threeNightShiftCons(id, iday, ishift) && noneNightShiftNurseCons(id, iday, ishift)
                && workHoursCons(id, iday)){
            return true;
        }else{
            return false;
        }
    }

    public boolean oneNurseInDayCons(Integer[] day, int id){
        if(Arrays.asList(day).contains(id)) return false;
        else return true;
    }

    public boolean threeNightShiftCons(int id, int iday, int ishift){
        boolean is_night;
        is_night = (iday == 6 || iday == 5) ? (ishift == 6 ? true : false) : (ishift == 9 ? true : false);
        if(!is_night){
            return true;
        } else {
            if(night_shift_stack.containsKey(id)){
                if (night_shift_stack.get(id) > 2){
                    return false;
                } else {
                    int tmp = night_shift_stack.get(id) + 1;
                    night_shift_stack.put(id, tmp);
                    return true;
                }
            } else {
                night_shift_stack.put(id, 1);
                return true;
            }
        }
    }

    public boolean workHoursCons(int id, int iday){

        Nurse nurse = nurses.get(id-1);

        if (iday == 0){
            for (Nurse n: nurses) {
                n.regenWorkHours();
            }
        }

        int work_hours;
        if(nurse.getWorkHours() > 0){
            work_hours = nurse.getWorkHours() - 8;
            nurse.setWorkHours(work_hours);
            return true;
        } else {
            return false;
        }
    }

    public boolean noneNightShiftNurseCons(int id, int iday, int ishift){
        boolean is_night;
        is_night = (iday == 6 || iday == 5) ? (ishift == 6 ? true : false) : (ishift == 9 ? true : false);
        if(is_night && id == 12){
            return false;
        } else {
            return true;
        }

    }

    public boolean sixDayInRowCons(){
        return true;
    }







}

