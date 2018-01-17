package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.Bot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Not a java interface
 * <p>
 * Also used in RobotBackEnd
 */
@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
    List<Bot> findAll();
}
