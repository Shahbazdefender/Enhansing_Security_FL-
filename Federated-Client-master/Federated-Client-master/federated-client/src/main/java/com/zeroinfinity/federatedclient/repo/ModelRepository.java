package com.zeroinfinity.federatedclient.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zeroinfinity.federatedclient.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {

	Optional<List<Model>> findByNameInAndIsActive(List<String> name, String isActive);

	Model findByNameAndIsActive(String name, String isActive);

	List<Model> findByIsActive(String isActive);
}
