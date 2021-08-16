package com.connect.user.domain.repository;

import com.connect.user.domain.entities.ElasticUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticUserRepository extends ElasticsearchRepository<ElasticUser, String>{
    boolean existsById(String id);
}
