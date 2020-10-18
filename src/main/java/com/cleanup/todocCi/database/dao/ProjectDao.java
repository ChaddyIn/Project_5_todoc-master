package com.cleanup.todocCi.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cleanup.todocCi.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {


    @Insert
    void createProject(Project project);


    @Query("SELECT * FROM Project WHERE id = :id")
    LiveData<Project> getProject(long id);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>>getAllProjects();

}
