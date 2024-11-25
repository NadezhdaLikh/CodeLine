package ru.codeline.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.codeline.dto.NewTeacherRequest;
import ru.codeline.dto.AllTeachersWithDetailsResponse;
import ru.codeline.services.AdminService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/teacher")
    public ResponseEntity<String> addTeacher(@RequestBody NewTeacherRequest request) {
        adminService.addTeacher(request);
        return ResponseEntity.ok("New teacher added successfully!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/teachers")
    public ResponseEntity<List<AllTeachersWithDetailsResponse>> getAllTeachersWithDetails() {
        List<AllTeachersWithDetailsResponse> allTeachersWithDetails = adminService.getAllTeachersWithDetails();
        return ResponseEntity.ok(allTeachersWithDetails);
    }
}

