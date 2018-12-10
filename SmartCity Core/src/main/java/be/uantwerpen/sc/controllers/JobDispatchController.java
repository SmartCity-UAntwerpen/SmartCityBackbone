package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.model.JobList;
import be.uantwerpen.sc.services.JobListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/job/")
public class JobDispatchController {

    @Autowired
    private JobListService jobListService;

//    @Autowired
//    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(JobDispatchController.class);

    /**
     * @param jobList The jobList object to save (received from MaaS)
     */
    @RequestMapping(value = "saveorder", method = RequestMethod.POST)
    public void saveOrder(@RequestBody JobList jobList) {
        logger.info("Test: Saving jobList");
        System.out.println(jobList.toString());
        System.out.println("Vehicle type in controller: " + jobList.getJobs().get(0).getTypeVehicle());
        //jobListService.saveOrder(jobList);
    }

    /**
     * Dispatching (This request comes from MaaS
     */
    @RequestMapping(value = "dispatch", method = RequestMethod.POST)
    public void dispatch() {
        logger.info("Test Dispatching");
        //jobListService.dispatchToCore();
    }

//    // Define REST bean
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}