package be.uantwerpen.sc.services;

//import be.uantwerpen.sc.localization.astar.Astar;
import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.sc.repositories.JobListRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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

    @Autowired
    private JobService jobService;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private RestTemplate restTemplate;

    public List<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public void saveOrder(final JobList joblist) {
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

    public void dispatchToCore() {
        for (JobList jl : this.jobListRepository.findAll()) {
            // iterate over all orders
            Job job = jl.getJobs().get(0);

            // if successfully dispatched, update status of the job
            if (job.getStatus().equals(JobState.BUSY)) {
                // probably not reached rendezvous point yet: wait
                return;
            }

            if (dispatch(job)) {
                job.setStatus(JobState.BUSY);
                //job.setJoblist(jl);
                jobService.save(job);

                //if (jl.getJobs().size() > 1 && !jl.getJobs().get(1).getTypeVehicle().equals(jl.getJobs().get(0).getTypeVehicle())) {
                if (jl.getJobs().size() > 1) {
                    Job nextJob = jl.getJobs().get(1);

                    if (dispatch(nextJob)) {
                        nextJob.setStatus(JobState.BUSY);
                        //nextJob.setJoblist(jl);
                        jobService.save(nextJob);
                    }
                }
            } else {
                // An error has occurred. Rerun the calculations for paths on the MaaS
                // TODO Test this out
                //recalculatePathAfterError(jl.getJobs().get(0).getId(), jl.getIdDelivery());

                // for debug purposes
                    /* logger.info(" Lijst van Orders afdrukken");
                    for (JobList jl2: jobListRepository.findAll()) {
                        logger.info(" Order #" + jl2.getId());
                        for(int x = 0; x<jl2.getJobs().size(); x++) {
                            logger.info("jobID: " + jl2.getJobs().get(x).getId() + ";   startPos :" + jl2.getJobs().get(x).getIdStart() + ";   endPos :" + jl2.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl2.getJobs().get(x).getIdVehicle()+ ";   VehicleType :" + jl2.getJobs().get(x).getTypeVehicle()+ ";   Status :" + jl2.getJobs().get(x).getStatus());
                        }
                    }*/
            }
        }
    }

    /**
     * Communication to the Backends of the different vehicles
     *
     * @param job The job to dispatch
     * @return (boolean) True if successfully sent. False if error occurred
     */
    private Boolean dispatch(Job job) {
        logger.info("Dispatch " + job.getId() + " - vehicle ");

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

    public void moveNextVehicleToPickUpPoint(Long jobId)
    {
        Job previousVehicle = jobService.getJob(jobId);

        // Get the backendInfo object from the info service for the given Job
        BackendInfo backendInfo = backendInfoService.getInfoByMapId(previousVehicle.getIdMap());

        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/job/gotopoint/{pid}";

        logger.info("Dispatch gotopoint to backend: " + stringUrl);

        ResponseEntity<String> response = restTemplate.exchange(stringUrl,
                HttpMethod.POST,
                null,
                String.class,
                previousVehicle.getIdEnd()
        );
    }

    // TODO Nodig?
    /**
     * function to check if order is empty or not
     *
     * @param id (long) id of the order
     * @return (boolean) true if Order is empty
     */
    public boolean isEmpty(long id) {
        return this.jobListRepository.findOne(id).getJobs().isEmpty();
    }

    /**
     * Delete an order
     *
     * @param id (long) id from the order that needs to be deleted
     */
    public void deleteOrder(long id) {
        this.jobListRepository.delete(id);
    }

    public void deleteAll() {
        this.jobListRepository.deleteAll();
    }

    // TODO Implement?
    /**
     * recalculate the order for which an error occured during the dispatch2core
     *
     * @param idJob      (long) id from the job in which an error occured
     * @param idDelivery (string) id from delivery which needs to be saved when making a new order with correct input
     */
    public void recalculatePathAfterError(long idJob, String idDelivery)
    {

    }

    // TODO Nodig?
    /**
     * Function to find a delivery when a job is given
     *
     * @param idDelivery (String) Id from the delivery
     * @return Job       (Job) first job from the order that is found mathcing the delivery ID
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

    public Long deleteByIdDelivery(String id) {
        return jobListRepository.deleteByIdDelivery(id);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}



