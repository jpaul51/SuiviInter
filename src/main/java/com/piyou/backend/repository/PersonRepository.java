package com.piyou.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piyou.backend.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}
