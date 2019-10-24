package com.illud.repository;

import com.illud.domain.DrivoData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DrivoData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DrivoDataRepository extends JpaRepository<DrivoData, Long> {

}
