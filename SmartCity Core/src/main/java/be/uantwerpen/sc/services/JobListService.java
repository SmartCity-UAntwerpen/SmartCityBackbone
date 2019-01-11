package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.sc.repositories.JobListRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

/**
 * Service based off the JobListRepository (formerly known as OrderRepository) in which JobLists (Orders) will be saved
 * aswell as all functions needed to dispatch jobs to the backends will be forseen.
 * NV 2018
 */
@Service
public class JobListService
{
    private static final Logger logger = LogManager.getLogger(JobListService.class);

    @Autowired
    private JobListRepository jobListRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private RestTemplate restTemplate;

    public List<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public JobList findOneByDelivery(int delivery)
    {
        long ldelivery = (long)delivery;
        return this.jobListRepository.findById(ldelivery);
    }

    /**
     * Function to find a delivery when a job is given
     *
     * @param idDelivery (String) Id from the delivery
     * @return Job       (Job) first job from the order that is found matching the delivery ID
     */
    public Job findDelivery(String idDelivery) {
        Job found = new Job();
        boolean foundUpdated = false;
        for (JobList jl : this.jobListRepository.findAll()) {
            if (idDelivery.equals(jl.getIdDelivery())) {
                foundUpdated = true;
                found = jl.getJobs().get(0);
            }
        }
        if (!foundUpdated) {
            return null;
        } else {
            return found;
        }
    }

    public void saveJobList(final JobList joblist)
    {
        this.jobListRepository.save(joblist);
    }

    /**
     * A print function, mainly written for debug purposes in the console to make certain that the objects are correctly
     * being saved & that the correct information is written into each object
     */
    public void printJobList() {
        logger.info("Print job list.");
        for (JobList jl : this.jobListRepository.findAll()) {
            logger.info(" Order #" + jl.getId());
            for (int x = 0; x < jl.getJobs().size(); x++) {
                logger.info("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }

    /**
     * This function will go through all JobLists and look at which Job should be dispatched.
     */
    public void dispatchToBackend() {
        // Iterate over all orders
        for (JobList jl : this.jobListRepository.findAll()) {
            Job job;
            try{
                job = jl.getJobs().get(0);
            }
            catch(Exception e)
            {
                logger.error("Job not found: " + e);
                return;
            }

            // Check if the first job is still busy -> if so, go to the next list to dispatch its first job
            if (job.getStatus().equals(JobState.BUSY)) {
                // Probably has not reached the rendezvous point yet: wait
                logger.info("The first job (" + job.getId() + ") of the jobList (" + jl.getId() + ") is still busy!");
            }
            else {
                    if (dispatch(job) && job.getStatus().equals(JobState.TODO))
                    {
                        // If successfully dispatched, update status of the job
                        job.setStatus(JobState.BUSY);
                        jobService.save(job);
                    }
                    else {
                        // An error has occurred.
                        logger.error("An error has occured while dispatching job with id: " + job.getId() + " to the backend!");

                        //recalculatePathAfterError(jl.getJobs().get(0).getId(), jl.getIdDelivery());

                        // For debug purposes
                        logger.debug("All joblists after dispatch error.");
                        printJobList();
                    }
            }
        }
    }

    /**
     * Communication to the Backends of the different vehicles to dispatch a job
     *
     * @param job The job to dispatch
     * @return (boolean) True if successfully sent. False if an error has occurred
     */
    private Boolean dispatch(Job job) {
        logger.info("Dispatching Job with ID: " + job.getId());

        // Get the backendInfo object from the info service
        BackendInfo backendInfo = backendInfoService.getInfoByMapId(job.getIdMap());

        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/execute/";

        boolean status;
        stringUrl += String.valueOf(job.getIdStart()) + "/" + String.valueOf(job.getIdEnd()) + "/" + String.valueOf(job.getId());
        logger.info("The full dispatch url of the Job is: " + stringUrl);

        ResponseEntity<String> response = restTemplate.exchange(stringUrl,
                HttpMethod.POST,
                null,
                String.class
        );

        if(!response.getStatusCode().is2xxSuccessful() && response.getBody().equalsIgnoreCase("false")) {
            logger.warn("Error while dispatching Job: " + job.getId());
            logger.warn("Response body: " + response.getBody());
            status = false;
        }
        else
        {
            status = true;
        }
        return status;
    }

    /**
     * TODO Future Work -> See JobDispatchController for possible use/removal
     * Communication to the Backends of the different vehicles to dispatch a job
     *
     * @param jobId id of the job that is almost at its endpoint
     */
    public void moveNextVehicleToPickUpPoint(Long jobId)
    {
        Job previousVehicle = jobService.getJob(jobId);
        Job nextJob = null;

        for (JobList jl : this.jobListRepository.findAll())
        {
            // Check if there are other jobs to do
            if (jl.getJobs().size() > 1)
            {
                if (previousVehicle.getId().equals(jl.getJobs().get(0).getId()))
                {
                    nextJob = jl.getJobs().get(1);
                    break;
                }
            }

        }

        // Check if nextJob is null which can be when there is no job left to do
        if(nextJob == null)
        {
            // no previous vehecle f.e. when got a close by when np
            if(previousVehicle == null) {
                logger.warn("JobListService: no previous vehicle");
            }else{
                logger.info("No Job left to do for this Job (id: " + previousVehicle.getId() + ").");
            }
            // Just return when there is no rendez-vous
            return;
        }

        // Get the backendInfo object from the info service for the given Job
        BackendInfo backendInfo = backendInfoService.getInfoByMapId(nextJob.getIdMap());

        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/gotopoint/" + nextJob.getIdStart();

        logger.info("Dispatch gotopoint to backend: " + stringUrl);

        ResponseEntity<String> response = restTemplate.exchange(stringUrl,
                HttpMethod.POST,
                null,
                String.class
        );
    }

    /**
     * Function to check if an order is empty or not
     *
     * @param id (long) id of the order
     * @return (boolean) true if Order is empty
     */
    public boolean isEmpty(long id) {
        return this.jobListRepository.findOne(id).getJobs().isEmpty();
    }

    /**
     * Delete an order (jobList)
     *
     * @param id (long) id from the order that needs to be deleted
     */
    public void deleteOrder(long id)
    {
        this.jobListRepository.delete(id);
        logger.info("Deleted Order/JobList: " + id);
    }

    /**
     * Delete all orders (jobLists) from the database
     */
    public void deleteAll() {
        logger.info("Deleting all JobLists!");
        this.jobListRepository.deleteAll();
    }

    /**
     * Delete order with its name
     *
     * @param id      (String) id from the jobList to delete
     */
    public Long deleteByIdDelivery(String id)
    {
        return jobListRepository.deleteByIdDelivery(id);
    }

    /**
     * TODO Future work -> What can be done after this error happens?
     * Recalculate the order for which an error occured during the dispatchToBackend
     *
     * @param idJob      (long) id from the job for which an error has occured
     * @param idDelivery (string) id from delivery which needs to be saved when making a new order with correct input
     */
    public void recalculatePathAfterError(long idJob, String idDelivery) {
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
