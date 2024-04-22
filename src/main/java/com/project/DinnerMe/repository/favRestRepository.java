package com.project.DinnerMe.repository;

import com.project.DinnerMe.entity.favrest;
import com.project.DinnerMe.entity.favrestid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface favRestRepository extends JpaRepository<favrest, favrestid> {
    List<favrest> findByClientId(long l);
}
