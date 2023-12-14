package com.dawidfrankiewicz.todo.service;

import com.dawidfrankiewicz.todo.api.model.Task;
import com.dawidfrankiewicz.todo.database.DatabaseConnection;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

@Service
public class TaskService {
    // TODO: User id is hardcoded for now
    private final int USER_ID = 1;
    private Connection connection;

    public TaskService() {
        connection = new DatabaseConnection().getConnection();
    }

    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = " + USER_ID;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Task newTask = new Task();
                newTask.setId(resultSet.getInt("id"));
                newTask.setTitle(resultSet.getString("title"));
                newTask.setDescription(resultSet.getString("description"));
                newTask.setIsDone(resultSet.getBoolean("isDone"));

                taskList.add(newTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskList;
    }

    public Task getTask(int id) {
        Task task = new Task();
        String query = "SELECT * FROM tasks WHERE user_id = " + USER_ID + " AND id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setIsDone(resultSet.getBoolean("isDone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }

    public void addTask(Task task) {
        String query = "INSERT INTO tasks (user_id, title, description, isDone) VALUES (" + USER_ID + ", ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setBoolean(3, task.getIsDone());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String query = "DELETE FROM tasks WHERE user_id = " + USER_ID + " AND id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editTask(int id, Task task) {
        String query = "UPDATE tasks SET title = ?, description = ? WHERE user_id = " + USER_ID + " AND id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setInt(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}