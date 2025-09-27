package com.kapil.library.library_management.services;

import com.kapil.library.library_management.entities.Student;
import com.kapil.library.library_management.exceptions.ResourceNotFoundException;
import com.kapil.library.library_management.repositories.StudentRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class StudentService {

    private final StudentRepository studentRepository;

    public Student createStudent(Student inputStudent) {
        return studentRepository.save(inputStudent);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    public String addMoneyToWallet(Long id, Double amount) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        student.setWalletBalance(student.getWalletBalance() + amount);
        studentRepository.save(student);
        return "Added " + amount + " to wallet. New balance: " + student.getWalletBalance();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAllByOrderByIdAsc();
    }
}
