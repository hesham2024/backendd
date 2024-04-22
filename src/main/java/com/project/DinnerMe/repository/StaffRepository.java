package com.project.DinnerMe.repository;

import com.project.DinnerMe.entity.Client;
import com.project.DinnerMe.entity.Staff;
import com.project.DinnerMe.entity.StaffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StaffRepository extends JpaRepository<Staff, StaffId> {
    Staff findByEmailAndPassword(String email, String password);

    Staff findByEmail(String email);

    void deleteById(Long staffId);
}
