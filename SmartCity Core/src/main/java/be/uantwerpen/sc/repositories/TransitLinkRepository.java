package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.TransitLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransitLinkRepository extends CrudRepository<TransitLink, Long> {
    List<TransitLink> findAll();
    TransitLink findById(int id);
}
