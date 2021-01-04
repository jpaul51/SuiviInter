package com.piyou.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piyou.backend.model.Intervention;

public interface InterventionRepository extends JpaRepository<Intervention, Long>{

	public default void saveOne(Intervention d) {
		save(d);
	}

}
