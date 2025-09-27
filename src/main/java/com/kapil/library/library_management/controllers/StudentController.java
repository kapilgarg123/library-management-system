package com.kapil.library.library_management.controllers;

import com.kapil.library.library_management.entities.Student;
import com.kapil.library.library_management.services.StudentService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping()
    public Student createStudent(@RequestBody Student inputStudent) {
        return studentService.createStudent(inputStudent);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping()
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/{id}/wallet/add")
    public String addMoneyToWallet(@PathVariable Long id, @RequestParam Double amount) {
        return studentService.addMoneyToWallet(id, amount);
    }
}
