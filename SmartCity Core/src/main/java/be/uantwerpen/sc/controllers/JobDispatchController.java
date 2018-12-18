package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.services.JobListService;
import be.uantwerpen.sc.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class JobDispatchController {

    @Autowired
    private JobListService jobListService;

    @Autowired
    private JobService jobService;

    private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    /**
     * @param jobList The jobList object to save (received from MaaS)
     */
    @RequestMapping(value = "/jobs/saveOrder", method = RequestMethod.POST)
    public void saveOrder(@RequestBody JobList jobList) {
        logger.info("Test: Saving jobList");
        //System.out.println(jobList.toString());
        jobListService.saveOrder(jobList);
    }

    /**
     * Dispatching (This request comes from MaaS)
     */
    @RequestMapping(value = "/jobs/dispatch", method = RequestMethod.POST)
    @ResponseBody
    public String dispatch() {
        logger.info("Test Dispatching");
        jobListService.dispatchToCore();
        return "Done dispatching";
    }

    @RequestMapping(value = "/jobs/findAllJobLists",method = RequestMethod.GET)
    public List<JobList> findAllJobLists()
    {
        return jobListService.findAll();
    }

    @RequestMapping(value = "/jobs/findOneByDelivery/{delivery}",method = RequestMethod.GET)
    public JobList findOneByDelivery(@PathVariable("delivery") String delivery)
    {
        return jobListService.findOneByDelivery(delivery);
    }

    @RequestMapping(value = "/jobs/deleteOrder/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id) {
        jobListService.deleteOrder(id);
    }

    @RequestMapping(value = "/jobs/deleteAllLists",method = RequestMethod.POST)
    public void deleteAllJobs()
    {
        jobService.deleteAll();
        jobListService.deleteAll();
    }

    @RequestMapping(value = "/jobs/vehiclecloseby/{idjob}", method = RequestMethod.POST)
    public void vehicleCloseByToEnd(@PathVariable("idjob") Long idJob)
    {
        // When the first vehicle is near it's transit/endpoint, move the next vehicle to that transit/endpoint
        logger.info("Vehicle with Job ID - " + idJob + " is close to it's endpoint.");
        jobListService.moveNextVehicleToPickUpPoint(idJob);
    }

    @RequestMapping(value = "/jobs/complete/{idjob}", method = RequestMethod.POST)
    @ResponseBody
    public String completeJob(@PathVariable("idjob") Long idJob)
    {
        logger.info("Job " + idJob + " is complete");
        for (JobList jl : jobListService.findAll()) {
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                jl.getJobs().remove(0);
                jobService.delete(idJob);
                logger.info("Rendezvous job with id " + idJob + " is deleted because it was complete");
            } else if (jl.getJobs().size() > 1 && jl.getJobs().get(1).getId().equals(idJob)) {
                // Rendezvous
                jl.getJobs().remove(1);
                jobService.delete(idJob);
                logger.info("Job " + idJob + " is deleted because it was complete");
            }

            if (jl.getJobs().isEmpty()) {
                jobListService.deleteOrder(jl.getId()); // TODO Mag dit wel? Verwijderen terwijl we erover iteraten
                logger.info("Deleted order: " + jl.getId());
            } else {
                logger.info("Dispatching next job");
                jobListService.dispatchToCore();
            }
        }
        return "Ok";
    }
}