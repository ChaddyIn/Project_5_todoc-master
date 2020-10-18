package com.cleanup.todocCi.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.cleanup.todocCi.model.Project;
import com.cleanup.todocCi.model.Task;
import com.cleanup.todocCi.repositories.ProjectDataRepository;
import com.cleanup.todocCi.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;



    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }


    // -------------
    // FOR PROJECT
    // -------------


    public LiveData<Project> getProject(long projectId) {
        return projectDataSource.getProject(projectId);
    }

    public LiveData<List<Project>> getAllProject() {


        return projectDataSource.getAllProjects();
    }


    // -------------
    // FOR TASK
    // -------------

    public LiveData<List<Task>> getTask() {
        return taskDataSource.getTask();
    }


    public void createTask(Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> {
            taskDataSource.deleteTask(taskId);
        });
    }

    public void updateTask(Task task) {
        executor.execute(() -> {
            taskDataSource.updateTask(task);
        });
    }
}
