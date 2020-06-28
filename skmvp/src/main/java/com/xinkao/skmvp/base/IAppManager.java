package com.xinkao.skmvp.base;

import android.app.Activity;

import java.util.LinkedList;

public interface IAppManager {
    LinkedList<Activity> activitys = new LinkedList<>();

    default void addActivity(Activity activity) {
        activitys.add(activity);
    }

    default Activity getLastActivity() {
        if (activitys.size() > 0)
            return activitys.getLast();
        return null;
    }

    default void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    default void exit() {
        for (Activity a : activitys) {
            if (null != a) {
                a.finish();
            }
        }

        activitys.clear();
    }
}
