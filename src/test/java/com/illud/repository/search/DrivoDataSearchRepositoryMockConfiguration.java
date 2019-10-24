package com.illud.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DrivoDataSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DrivoDataSearchRepositoryMockConfiguration {

    @MockBean
    private DrivoDataSearchRepository mockDrivoDataSearchRepository;

}
