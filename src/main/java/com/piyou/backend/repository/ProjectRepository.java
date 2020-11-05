package com.piyou.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piyou.backend.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

}
