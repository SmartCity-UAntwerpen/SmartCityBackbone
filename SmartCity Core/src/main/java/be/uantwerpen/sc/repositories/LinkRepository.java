package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.links.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends CrudRepository<Link, Long> {
    List<Link> findAll();
}
