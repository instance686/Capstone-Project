package com.reminders.location.locatoinreminder.executor;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 25/1/18.
 */

public interface MainToShouts  {
    void shoutsData(List<ChatCards_Entity> chatCardsEntities);
}
