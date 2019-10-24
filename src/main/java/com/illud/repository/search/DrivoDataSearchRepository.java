package com.illud.repository.search;

import com.illud.domain.DrivoData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DrivoData entity.
 */
public interface DrivoDataSearchRepository extends ElasticsearchRepository<DrivoData, Long> {
}
