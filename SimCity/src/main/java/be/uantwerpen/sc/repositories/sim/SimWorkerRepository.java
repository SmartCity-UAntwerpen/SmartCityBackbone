package be.uantwerpen.sc.repositories.sim;

import be.uantwerpen.sc.models.sim.SimWorker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thomas on 03/04/2016.
 */
@Repository
public interface SimWorkerRepository extends CrudRepository<SimWorker,Long>
{
    List<SimWorker> findAll();

    SimWorker findByWorkerName(String workerName);

    SimWorker findByWorkerId(Long workerId);
}
