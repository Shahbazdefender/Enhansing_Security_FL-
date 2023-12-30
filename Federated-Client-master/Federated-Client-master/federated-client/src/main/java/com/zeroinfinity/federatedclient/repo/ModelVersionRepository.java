package com.zeroinfinity.federatedclient.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zeroinfinity.federatedclient.entity.ModelVersion;

@Repository
public interface ModelVersionRepository extends JpaRepository<ModelVersion, Integer> {

	ModelVersion findByStatus(String status);

	ModelVersion findByVersionAndStatus(String version, String status);

}
