package com.techelevator.controller;

import com.techelevator.dao.ExerciseDao;
import com.techelevator.model.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@CrossOrigin
@RestController
public class ExerciseController {
    @Autowired
    ExerciseDao exerciseDao;

    @RequestMapping(path = "/exercise", method = RequestMethod.GET)
    public List<Exercise> listExercises() {
        return exerciseDao.allExercises();
    }

    @RequestMapping(path = "/exercise/{id}", method = RequestMethod.GET)
    public Exercise getExerciseById(@PathVariable int id) {
        return exerciseDao.getExerciseById(id);
    }


    @PreAuthorize("hasRole('TRAINER')")
    @RequestMapping(path = "/exercise/add", method = RequestMethod.POST)
    public Exercise addNewExercise(@RequestBody Exercise exercise) {
        exerciseDao.createExercise(exercise);
        return exercise;
    }

    @RequestMapping(path = "/exercise/edit/{id}", method = RequestMethod.PUT)
    public Exercise updateExercise(@RequestBody Exercise exercise, @PathVariable int id) {
        Exercise updatedExercise = exerciseDao.updateExercise(exercise, id);
        if (updatedExercise == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such id: " + id);
        }
        else {
            return updatedExercise;
        }
    }

    @RequestMapping(path = "/exercise/delete/{id}", method = RequestMethod.DELETE)
    public void deleteExercise(@PathVariable int id) {
        Exercise exerciseToDelete = exerciseDao.getExerciseById(id);
        if (exerciseToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No such id: " + id);
        }
        else {
            exerciseDao.deleteExercise(id);
        }
    }

}