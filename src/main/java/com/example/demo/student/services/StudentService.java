package com.example.demo.student.services;


import com.example.demo.student.models.Student;
import com.example.demo.student.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {

        Optional<Student> studentByEmail =
                studentRepository.findStudentByEmail(student.getEmail());

        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        //System.out.println(student);
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        boolean exists = studentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("student with id " +id+" does not exist");
        }
        studentRepository.deleteById(id);
    }
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {

    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalStateException(
                    "student with id "+studentId+" does not exist"));

    if (name != null &&
            name.length() > 0 &&
            !Objects.equals(student.getName(), name)){
        student.setName(name);

    }

    if (email != null &&
            email.length() > 0 &&
    !Objects.equals(student.getEmail(), email)){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        student.setEmail(email);
    }

    }
}
