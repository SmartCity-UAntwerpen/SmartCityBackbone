package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Delivery;
import be.uantwerpen.sc.models.jobs.JobList;
import be.uantwerpen.sc.services.DeliveryService;
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

    @Autowired
    private DeliveryService deliveryService;

    private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    @RequestMapping(value = "/jobs/findAllJobLists",method = RequestMethod.GET)
    public List<JobList> findAllJobLists()
    {
        return jobListService.findAll();
    }

    @RequestMapping(value = "/jobs/findOneByOrder/{orderId}",method = RequestMethod.GET)
    public JobList findOneByOrder(@PathVariable("orderId") int orderId)
    {
        return  deliveryService.getByOrderID(orderId).getJobLists().get(0);
    }

    @RequestMapping(value = "/jobs/deleteOrder/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id)
    {
        jobListService.deleteOrder(id);
    }

    @RequestMapping(value = "/jobs/deleteAllLists",method = RequestMethod.POST)
    public void deleteAllJobs()
    {
        jobService.deleteAll();
        jobListService.deleteAll();
    }

    // TODO Future work -> See if this can be done by all backends or remove it (Currently only F1 uses this).
    @RequestMapping(value = "/jobs/vehiclecloseby/{idjob}", method = RequestMethod.POST)
    public void vehicleCloseByToEnd(@PathVariable("idjob") Long idJob)
    {
        //When the first vehicle is near it's transit/endpoint, move the next vehicle to that transit/endpoint
        logger.info("Vehicle with Job ID - " + idJob + " is close to it's endpoint.");
        //jobListService.moveNextVehicleToPickUpPoint(idJob);
    }

    // TODO Future work -> Do we delete the job or set its status to DONE (so that we have an history of jobs)?
    @RequestMapping(value = "/jobs/complete/{idjob}", method = RequestMethod.POST)
    @ResponseBody
    public String completeJob(@PathVariable("idjob") Long idJob)
    {
        logger.info("Job " + idJob + " is complete");
        for (JobList jl : jobListService.findAll()) {
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                jl.getJobs().remove(0);
                jobService.delete(idJob);
                logger.info("Job with id: " + idJob + " is deleted because it was completed.");
            } else if (jl.getJobs().size() > 1 && jl.getJobs().get(1).getId().equals(idJob)) {
                // Rendezvous
                jl.getJobs().remove(1);
                jobService.delete(idJob);
                logger.info("Job with id:" + idJob + " is deleted because it was completed.");
            }

            if (jl.getJobs().isEmpty())
            {
                //When all jobs of the delivery are finished -> Delete the JobList
                jobListService.deleteOrder(jl.getId());
            } else {
                jobListService.dispatchToBackend();
            }
        }
        return "Ok";
    }
}