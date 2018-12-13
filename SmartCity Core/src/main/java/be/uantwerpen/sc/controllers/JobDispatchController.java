package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.services.JobListService;
import be.uantwerpen.sc.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;


@RestController
public class JobDispatchController {

    @Autowired
    private JobListService jobListService;

    @Autowired
    private JobService jobService;

    private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    /**
     * @param jobList The jobList object to save (received from MaaS)
     */
    @RequestMapping(value = "/job/saveorder", method = RequestMethod.POST)
    public void saveOrder(@RequestBody JobList jobList) {
        logger.info("Test: Saving jobList");
        System.out.println(jobList.toString());
        System.out.println("Vehicle type in controller: " + jobList.getJobs().get(0).getTypeVehicle());
        //jobListService.saveOrder(jobList);
    }

    /**
     * Dispatching (This request comes from MaaS
     */
    @RequestMapping(value = "/job/dispatch", method = RequestMethod.POST)
    @ResponseBody
    public String dispatch() {
        logger.info("Test Dispatching");
        //jobListService.dispatchToCore();
        return "Done dispatching";
    }

    @RequestMapping(value = "/completeJob/{idJob}", method = RequestMethod.GET)
    @ResponseBody
    public String completeJob(@PathVariable Long idJob) {
        logger.info("Job " + idJob + " is complete");
        for (JobList jl : jobListService.findAll()) {
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                jl.getJobs().remove(0);
                jobService.delete(idJob);
                logger.info("Rendezvous job with id " + idJob + " is deleted because it was complete");
            } else if (jl.getJobs().size() > 1 && jl.getJobs().get(1).getId().equals(idJob)) {
                // rendezvous
                jl.getJobs().remove(1);
                jobService.delete(idJob);
                logger.info("Job " + idJob + " is deleted because it was complete");
            }

            if (jl.getJobs().isEmpty()) {
                jobListService.deleteOrder(jl.getId());
                logger.info("Delete order");
            } else {
                jobListService.dispatchToCore();
                logger.info("Dispatch next job");
            }
        }

        return "ok";
    }
}