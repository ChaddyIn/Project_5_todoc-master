package com.cleanup.todocCi;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cleanup.todocCi.database.dao.TodocMasterDatabase;
import com.cleanup.todocCi.model.Project;
import com.cleanup.todocCi.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {


    // FOR DATA
    private TodocMasterDatabase database;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Philippe", 0xFFEADAD1);
    private static Task TASK_DEMO = new Task(PROJECT_ID, "test", new Date().getTime());


    private static Task NEW_TASK__TO_DO = new Task(PROJECT_ID, "ok", new Date().getTime());
    private static Task NEW_TASK_IDEA = new Task(PROJECT_ID, "test2", new Date().getTime());
    private static Task NEW_TASK_1 = new Task(PROJECT_ID, "test3", new Date().getTime());


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocMasterDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        // BEFORE : Adding a new project
        this.database.projectDao().createProject(PROJECT_DEMO);
        // TEST
        Project project = LiveDataUtilTest.getValue(this.database.projectDao().getProject(PROJECT_ID));
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }


    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> items = LiveDataUtilTest.getValue(this.database.taskDao().getTask());
        assertTrue(items.isEmpty());
    }


    @Test
    public void insertAndGetTasks() throws InterruptedException {
        // BEFORE : Adding demo project & demo tasks

        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK__TO_DO);
        this.database.taskDao().insertTask(NEW_TASK_IDEA);
        this.database.taskDao().insertTask(NEW_TASK_1);

        // TEST
        List<Task> tasks = LiveDataUtilTest.getValue(this.database.taskDao().getTask());
        assertTrue(tasks.size() == 3);
    }


    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // BEFORE : Adding demo project & demo task. Next, get the task added & delete it.
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(NEW_TASK__TO_DO);
        Task taskAdded = LiveDataUtilTest.getValue(this.database.taskDao().getTask()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getTaskId());

        //TEST
        List<Task> items = LiveDataUtilTest.getValue(this.database.taskDao().getTask());
        assertTrue(items.isEmpty());
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

}
