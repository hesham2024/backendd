package com.project.DinnerMe.repository;

import com.project.DinnerMe.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByEmail(String email);

}
