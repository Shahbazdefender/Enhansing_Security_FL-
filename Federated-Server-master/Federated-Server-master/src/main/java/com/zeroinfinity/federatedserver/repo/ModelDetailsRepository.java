package com.zeroinfinity.federatedserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zeroinfinity.federatedserver.entity.ModelDetails;

@Repository
public interface ModelDetailsRepository extends JpaRepository<ModelDetails, Integer> {

	List<ModelDetails> findByModelVersionVersionAndRequestTypeIsNull(String version);

	List<ModelDetails> findByModelVersionVersionAndRequestType(String version, String requestType);
}
