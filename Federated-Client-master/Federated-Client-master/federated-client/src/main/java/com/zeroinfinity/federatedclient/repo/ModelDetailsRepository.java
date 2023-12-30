package com.zeroinfinity.federatedclient.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zeroinfinity.federatedclient.entity.ModelDetails;

@Repository
public interface ModelDetailsRepository extends JpaRepository<ModelDetails, Integer> {

	List<ModelDetails> findByModelVersionVersionAndRequestTypeIsNull(String version);
}
