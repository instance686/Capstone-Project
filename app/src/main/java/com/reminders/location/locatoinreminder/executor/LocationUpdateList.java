package com.reminders.location.locatoinreminder.executor;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public interface LocationUpdateList {

    void locationList(List<ChatCards_Entity> chatCardsEntityList);
}
