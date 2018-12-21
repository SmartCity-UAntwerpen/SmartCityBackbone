package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.repositories.JobRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for the Job class. All interactions will be defined here required to perform the methods for the Job class
 * NV 2018
 */
@Service
public class JobService {

    private static final Logger logger = LogManager.getLogger(JobService.class);

    @Autowired
    private JobRepository jobRepository;

    public List<Job> findAll() {
        return this.jobRepository.findAll();
    }

    public void save(final Job job)
    {
        this.jobRepository.save(job);
    }

    public Job getJob (Long id)
    {
        return this.jobRepository.findOne(id);
    }

    public void delete(Long id)
    {
        logger.debug("Deleting Job with id: " + id);
        this.jobRepository.delete(id);
    }

    public void deleteAll()
    {
        logger.info("Deleting all jobs!");
        this.jobRepository.deleteAll();
    }
}
