package com.cleanup.todoc.database.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import static com.cleanup.todoc.injections.Injection.provideExecutor;

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





    /**private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {




                super.onCreate(db);


              db.execSQL("INSERT INTO Project VALUES(1, 'test', '0xFFEADAD1');");

               /** ContentValues contentValues = new ContentValues();

                contentValues.put("id", 1);
                contentValues.put("name", "test");
                contentValues.put("color", 0xFFEADAD1);

                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);*/

                         /**     ContentValues contentValues1 = new ContentValues();
                contentValues1.put("taskId", 1);
                contentValues1.put("projectId", 1);
                contentValues1.put("name", "ok");
                contentValues1.put("date", 18);

                db.insert("Task", OnConflictStrategy.IGNORE, contentValues);

            }
        };
    }*/

    private static void populateInitialData() {

        if(INSTANCE == null){
        List<Project> allProjects = Arrays.asList( Project.getAllProjects());

        for (int i = 0; i < allProjects.size(); i++) {
            INSTANCE.projectDao().createProject(allProjects.get(i));
        }
        }
        }
    }




