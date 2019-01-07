package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.sc.services.BackendInfoService;
import be.uantwerpen.sc.services.JobService;
import org.json.simple.JSONObject;
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
    public JSONObject getJobProgress(@PathVariable("id") Long id)
    {
        JSONObject response = new JSONObject();

        Job job = jobService.getJob(id);
        int progress = 0;

        // If job is not found return 100 because this means that the job was completed and deleted from the database.
        if(job == null)
        {
            response.put("JobId", id);
            response.put("Progress", progress);
            response.put("JobState", JobState.DONE);
            return response;
        }

        switch(job.getStatus())
        {
            case DONE:
                progress = 100;
                break;
            case TODO:
                // Do nothing -> returns 0 progress
                break;
            case BUSY:
                // Get the actual progress from the backend of the vehicle

                // Get the backendInfo object from the info service
                BackendInfo backendInfo = backendInfoService.getInfoByMapId(job.getIdMap());

                String stringUrl = "http://";
                stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/getprogress/" + job.getId();

                ResponseEntity<Integer> responseBackEnd = restTemplate.exchange(stringUrl,
                        HttpMethod.GET,
                        null,
                        Integer.class
                );
                logger.warn("Job progress from job with id " + job.getId() + " is " + responseBackEnd.getBody());
                // TODO Check what we receive here
                progress = Integer.parseInt(responseBackEnd.getBody().toString());
                break;
            default:
                progress = 0;
                break;
        }

        response.put("JobId", id);
        response.put("Progress", progress);
        response.put("JobState", job.getStatus());
        return response;
    }

//    @RequestMapping(value = "testEndPoint/{id}", method = RequestMethod.GET)
//    public JSONObject testEndPoint(@PathVariable("id") Long id) {
//        JSONObject response = new JSONObject();
//
//        int progress = 50;
//        response.put("JobId", id);
//        response.put("Progress", progress);
//        response.put("JobState", JobState.DONE);
//        return response;
//
//    }

    // TODO Future work -> Backends use this endpoint to let the backbone know when a job failed to execute
    @RequestMapping(value = "fail/{id}", method = RequestMethod.POST)
    public void jobFailed(@PathVariable("id") Long id)
    {
        // Remove the job or resend it to the correct backend
    }
}
