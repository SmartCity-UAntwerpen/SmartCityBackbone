package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.Bot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Not a java interface
 * TODO: Creates repo in mysql?
 *
 * Also used in RobotBackEnd
 * Created by Niels on 16/03/2016.
 */
@Repository
public interface BotRepository extends CrudRepository<Bot, Long>
{
    List<Bot> findAll();
}
