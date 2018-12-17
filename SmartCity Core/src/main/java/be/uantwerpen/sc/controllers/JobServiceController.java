package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job/service/")
public class JobServiceController {

    //private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    @Autowired
    private JobService jobService;

    /**
     * Asks JobService to search the database for all jobs and returns them
     *
     * @return List<Job>
     */
    @RequestMapping(value = "findalljobs",method = RequestMethod.GET)
    public List<Job> findAllJobs()
    {
        return jobService.findAll();
    }

    @RequestMapping(value = "savejob", method = RequestMethod.POST)
    public void save(@RequestBody Job job)
    {
        jobService.save(job);
    }

    @RequestMapping(value = "getjob/{id}", method = RequestMethod.GET)
    public Job getJob(@PathVariable("id") Long id)
    {
        return jobService.getJob(id);
    }

    @RequestMapping(value = "deletejob/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id) {
        jobService.delete(id);
    }

    @RequestMapping(value = "deletealljobs", method = RequestMethod.POST)
    public void deleteAll()
    {
        jobService.deleteAll();
    }
}
