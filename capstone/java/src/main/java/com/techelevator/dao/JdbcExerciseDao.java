package com.techelevator.dao;

import com.techelevator.model.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcExerciseDao implements ExerciseDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcExerciseDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Exercise> allExercises() {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercise";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Exercise exercise = mapRowToExerciseList(results);
            exercises.add(exercise);

        }

        return exercises;


    }

    @Override
    public Exercise getExerciseById(int exercise_id) {
        String sql = "SELECT * FROM exercise WHERE exercise_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, exercise_id);
        if(results.next()) {
            return mapRowToExerciseList(results);

        } else {
            return null;
        }

    }

    @Override
    public boolean createExercise(Exercise exercise) {
        String sql = "insert into exercise(exercise_name, exercise_description, suggested_weight, num_of_reps, exercise_duration, target_area) VALUES (?,?,?,?,?,?) RETURNING exercise_id";
        Integer newId;
        try{
            newId = jdbcTemplate.queryForObject(sql,
                    Integer.class, exercise.getExercise_name(), exercise.getExercise_description(), exercise.getSuggested_weight(), exercise.getNum_of_reps(), exercise.getExercise_duration(), exercise.getTarget_area());
            exercise.setExercise_id(newId);
        }catch (DataAccessException e){
            return false;
        }
        return true;
    }

    @Override
    public Exercise updateExercise(Exercise exercise, int id) {
        try {
            String sql = "UPDATE exercise SET exercise_name = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getExercise_name(), id);
            sql = "UPDATE exercise SET exercise_description = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getExercise_description(), id);
            sql = "UPDATE exercise SET suggested_weight = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getSuggested_weight(), id);
            sql = "UPDATE exercise SET num_of_reps = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getNum_of_reps(), id);
            sql = "UPDATE exercise SET duration = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getExercise_duration(), id);
            sql = "UPDATE exercise SET target_area = ? WHERE exercise_id = ?";
            jdbcTemplate.update(sql, exercise.getTarget_area(), id);
        } catch (DataAccessException e){
            System.out.println("Failed to update exercise " + id + "!");
        }
        return exercise;
    }

    @Override
    public void deleteExercise(int id) {
        String sql = "DELETE FROM exercise WHERE exercise_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e){
            System.out.println("Failed to delete exercise " + id + "!");
        }
    }

    private Exercise mapRowToExerciseList(SqlRowSet rs) {
        Exercise exercise = new Exercise();
        exercise.setExercise_id(rs.getInt("exercise_id"));
        exercise.setExercise_name(rs.getString("exercise_name"));
        exercise.setExercise_description(rs.getString("exercise_description"));
        exercise.setSuggested_weight(rs.getInt("suggested_weight"));
        exercise.setNum_of_reps(rs.getInt("num_of_reps"));
        exercise.setExercise_duration(rs.getInt("exercise_duration"));
        exercise.setTarget_area(rs.getString("target_area"));

        return exercise;
    }




}
