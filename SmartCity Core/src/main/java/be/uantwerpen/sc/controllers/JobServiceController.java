package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.sc.services.BackendInfoService;
import be.uantwerpen.sc.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/job/service/")
public class JobServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private RestTemplate restTemplate;

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

    @RequestMapping(value = "getJobProgress/{id}", method = RequestMethod.GET)
    public int getJobProgress(@PathVariable("id") Long id)
    {
        Job job = jobService.getJob(id);
        int progress = 0;

        // If job is not found return 100 because this means that the job was completed and deleted from the database.
        if(job == null)
        {
            return 100;
        }

        switch(job.getStatus())
        {
            case DONE:
                progress = 100;
                break;
            case TODO:
                // Do nothing -> returns 0
                break;
            case BUSY:
                // Get the actual progress from the backend of the vehicle

                // Get the backendInfo object from the info service
                BackendInfo backendInfo = backendInfoService.getInfoByMapId(job.getIdMap());

                String stringUrl = "http://";
                stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/getprogress/" + job.getId();

                ResponseEntity<Integer> response = restTemplate.exchange(stringUrl,
                        HttpMethod.GET,
                        null,
                        Integer.class
                );
                logger.info("Job progress from job with id " + job.getId() + " is " + response.getBody());
                // TODO check what we receive here
                progress = Integer.parseInt(response.getBody().toString());
                break;
            default:
                progress = 0;
                break;
        }
        return progress;
    }
}
