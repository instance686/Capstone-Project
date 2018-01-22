package com.reminders.location.locatoinreminder.executor;

import com.reminders.location.locatoinreminder.database.entity.ChatCards_Entity;

import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public interface ShoutsListUpdate {

public void shoutListUpdate(List<ChatCards_Entity> chatCardsEntities);
}
