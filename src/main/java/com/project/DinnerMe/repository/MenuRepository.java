package com.project.DinnerMe.repository;

import com.project.DinnerMe.entity.Menu;
import com.project.DinnerMe.entity.MenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, MenuId> {
}
