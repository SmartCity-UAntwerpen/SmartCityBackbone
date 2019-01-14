package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.sc.services.BackendInfoService;
import be.uantwerpen.sc.services.BackendService;
import be.uantwerpen.sc.services.JobService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/job/service/")
public class JobServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private BackendService backendService;

    @Value("${backends.enabled}")
    boolean backendsEnabled;

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

        int progress = 0;

        // If backends are disabled return a random progress to test visualiation
        if(!backendsEnabled){
            progress = (int)Math.floor(Math.random()*101);
            response = buildProgressResponse(id, progress, JobState.BUSY.toString());
            return  response;
        }

        Job job = jobService.getJob(id);

        // If job is not found return 100 because this means that the job was completed and deleted from the database.
        if(job == null)
        {
//            response.put("JobId", id);
//            response.put("Progress", progress);
//            response.put("JobState", JobState.DONE);
            response = buildProgressResponse(id, progress, JobState.TODO.toString());
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
                    progress = getProgressFromBackend(job);
                break;
            default:
                progress = 0;
                break;
        }
        response = buildProgressResponse(id, progress, job.getStatus().toString());

        return response;
    }

    private int getProgressFromBackend(Job job){
        // Get the actual progress from the backend of the vehicle
        int progress = 0;
        // Get the backendInfo object from the info service
        BackendInfo backendInfo = backendInfoService.getInfoByMapId(job.getIdMap());

        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/getprogress/" + job.getId();

        JSONObject response = backendService.requestJsonObject(stringUrl);
        try {
            if (response != null & job.getId() != null) {
                logger.info("Job progress from job with id " + job.getId() + " is " + response.get("progress").toString());
                progress = Integer.parseInt(response.get("progress").toString());
            } else {
                logger.warn("Couldn't retrieve progress!");
                progress = 0;
            }
        }catch(Exception e){
            logger.warn("Exception: Couldn't retrieve progress!" + e);
            progress = 0;
        }
        return progress;
    }

    // TODO Future work -> Backends use this endpoint to let the backbone know when a job failed to execute
    @RequestMapping(value = "fail/{id}", method = RequestMethod.POST)
    public void jobFailed(@PathVariable("id") Long id)
    {
        // Remove the job or resend it to the correct backend
    }

    private JSONObject buildProgressResponse(long id, int progress, String status)
    {
        JSONObject response = new JSONObject();

        response.put("id", id);
        response.put("progress", progress);
        response.put("status", status);

        return response;
    }
}
