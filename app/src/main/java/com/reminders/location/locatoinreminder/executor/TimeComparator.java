package com.reminders.location.locatoinreminder.executor;

import com.reminders.location.locatoinreminder.database.entity.ReminderContact;

import java.util.Comparator;

/**
 * Created by ayush on 15/1/18.
 */

public class TimeComparator implements Comparator<ReminderContact> {

    @Override
    public int compare(ReminderContact o1, ReminderContact o2) {
        if (o1.getCurentTimeinMilliseconds() == o2.getCurentTimeinMilliseconds())
            return 0;
        else if (o1.getCurentTimeinMilliseconds() < o2.getCurentTimeinMilliseconds())
            return 1;
        else
            return -1;
    }
}
