package com.cleanup.todocCi.database.dao;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.cleanup.todocCi.model.Project;
import com.cleanup.todocCi.model.Task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static com.cleanup.todocCi.injections.Injection.provideExecutor;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocMasterDatabase extends RoomDatabase {


    // --- SINGLETON ---
    private static volatile TodocMasterDatabase INSTANCE;

    // --- DAO ---
    public abstract ProjectDao projectDao();

    public abstract TaskDao taskDao();


    // --- INSTANCE ---
    public static TodocMasterDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (TodocMasterDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocMasterDatabase.class, "MyDatabase.db")
                            .fallbackToDestructiveMigration().build();
                    Executor executor = provideExecutor();
                    executor.execute(() -> {
                        populateInitialData();
                    });

                }
            }
        }
        return INSTANCE;
    }


    private static void populateInitialData() {


        try {
            if (INSTANCE != null) {
                 List<Project> allProjects = Arrays.asList(Project.getAllProjects());

                 for (int i = 0; i < allProjects.size(); i++) {
                     INSTANCE.projectDao().createProject(allProjects.get(i));
                 }
             }
        } catch (SQLiteConstraintException e) {
            System.out.println("les projets sont déjà ajoutés");
        }
    }
}




