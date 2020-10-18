package com.cleanup.todocCi.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cleanup.todocCi.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("Select * FROM Task")
    LiveData<List<Task>> getTask();



    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("DELETE FROM Task WHERE taskId= :taskId")
    void deleteTask(long taskId);

}
