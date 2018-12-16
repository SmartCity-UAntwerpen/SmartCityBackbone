package be.uantwerpen.sc.services;

import be.uantwerpen.sc.SmartCityCoreApplication;
import be.uantwerpen.sc.models.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartCityCoreApplication.class)
@ActiveProfiles("dev")
public class JobServiceTest {

    @Autowired
    JobService jobService;

    @Test
    public void findNext() {
        Job nextJob =  jobService.findNextJob(1L);

        Long nextId = nextJob.getId();
        assert(nextId==2L);

        assert(nextJob.equals(jobService.getJob(nextId)));

        assert(jobService.findNextJob(120) == null);

        //check if no overflow to next order
        nextJob =  jobService.findNextJob(2L);
        assert(nextJob == null);
}
}
