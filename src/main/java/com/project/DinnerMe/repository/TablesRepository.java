package com.project.DinnerMe.repository;

import com.project.DinnerMe.entity.Tables;
import com.project.DinnerMe.entity.TablesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TablesRepository extends JpaRepository<Tables, TablesId> {
}
