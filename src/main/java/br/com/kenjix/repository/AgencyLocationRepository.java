package br.com.kenjix.repository;

import br.com.kenjix.model.AgencyLocation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgencyLocationRepository extends JpaRepository<AgencyLocation, Long> {
    @Query("""
    SELECT l 
    FROM AgencyLocation l
    ORDER BY ((l.posX - :x)*(l.posX - :x) + (l.posY - :y)*(l.posY - :y)) ASC
""")
    List<AgencyLocation> findAgencyNearest(@Param("x") double x, @Param("y") double y, Pageable pageable);
}
