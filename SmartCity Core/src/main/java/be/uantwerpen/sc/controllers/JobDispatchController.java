package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Job;
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

    // TODO is a test endpoint. Can be removed at the end.
    @RequestMapping(value = "/jobs/saveOrder", method = RequestMethod.POST)
    public void saveOrder()
    {
        logger.info("Test: Saving jobList -> First removing all jobs");
        jobListService.deleteAll();
        jobService.deleteAll();
        JobList list = new JobList();
        list.setIdDelivery("MaaSId1");

        Job job = new Job(1L,5L,11);
        list.addJob(job);

        job = new Job(7L,10L,10);
        list.addJob(job);
        jobListService.saveJobList(list);

        JobList list2 = new JobList();
        list2.setIdDelivery("MaaS2");

        job = new Job(4L,20L,11);
        list2.addJob(job);

        job = new Job(8L,11L,10);
        list2.addJob(job);
        jobListService.saveJobList(list2);
        jobListService.printJobList();
    }

    @RequestMapping(value = "/jobs/findAllJobLists",method = RequestMethod.GET)
    public List<JobList> findAllJobLists()
    {
        return jobListService.findAll();
    }

    @RequestMapping(value = "/jobs/findOneByDelivery/{delivery}",method = RequestMethod.GET)
    public JobList findOneByDelivery(@PathVariable("delivery") int delivery)
    {
        return jobListService.findOneByDelivery(delivery);
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

    // TODO Future work -> See if this can be done by the backend and remove it from here
    @RequestMapping(value = "/jobs/vehiclecloseby/{idjob}", method = RequestMethod.POST)
    public void vehicleCloseByToEnd(@PathVariable("idjob") Long idJob)
    {
        // When the first vehicle is near it's transit/endpoint, move the next vehicle to that transit/endpoint
        logger.info("Vehicle with Job ID - " + idJob + " is close to it's endpoint.");
        jobListService.moveNextVehicleToPickUpPoint(idJob);
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
                logger.info("Job with id: " + idJob + " is deleted because it was completed");
            } else if (jl.getJobs().size() > 1 && jl.getJobs().get(1).getId().equals(idJob)) {
                // Rendezvous
                jl.getJobs().remove(1);
                jobService.delete(idJob);
                logger.info("Job " + idJob + " is deleted because it was completed");
            }

            if (jl.getJobs().isEmpty())
            {
                // TODO Mag dit wel? Verwijderen terwijl we erover iteraten
                jobListService.deleteOrder(jl.getId());
            } else {
                jobListService.dispatchToBackend();
            }
        }
        return "Ok";
    }
}