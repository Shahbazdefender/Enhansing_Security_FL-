package com.zeroinfinity.federatedserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zeroinfinity.federatedserver.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

	Model findByNameAndIsActive(String name, String isActive);

	List<Model> findByIsActive(String isActive);
}
