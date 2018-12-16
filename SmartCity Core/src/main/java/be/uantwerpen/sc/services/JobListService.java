package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.repositories.JobListRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service based off the JobListRepository (formerly known as OrderRepository) in which JobList (Orders) will be saved
 * aswell as all functions needed to dispatch jobs to the cores will be forseen.
 * NV 2018
 */
@Service
public class JobListService {
    private static final Logger logger = LogManager.getLogger(JobListService.class);

    @Autowired
    private JobListRepository jobListRepository;

    public List<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public void saveOrder(final JobList joblist) {
        this.jobListRepository.save(joblist);
    }

}



