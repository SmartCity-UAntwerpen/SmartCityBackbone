package be.uantwerpen.sc;

import be.uantwerpen.sc.controllers.JobDispatchController;
import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.jobs.JobList;
import be.uantwerpen.sc.services.JobListService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev,devdata")
@SpringApplicationConfiguration(classes = SmartCityCoreApplication.class)
@WebIntegrationTest
public class DispatchingMultipleOrdersTest {
    @Autowired
    JobListService jobListService;

    @Autowired
    JobDispatchController jobDispatchController;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testDispatch() {
        // Endpoint to execute: /job/execute/{pidstart}/{pidstop}/{jobid}


        // Dispatch first job, first list
        mockServer.expect(requestTo("http://localhost:7777/job/execute/1/5/1")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Dispatch first job, second list
        mockServer.expect(requestTo("http://localhost:7777/job/execute/4/20/3")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Check transit behaviour, first list. Uncomment if this method is used in the real environment
        //mockServer.expect(requestTo("http://localhost:8888/job/gotopoint/7")).andExpect(method(HttpMethod.POST))
                //.andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Dispatch second job, first list
        mockServer.expect(requestTo("http://localhost:8888/job/execute/7/10/2")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Check transit behaviour, second list. Uncomment if this method is used in the real environment
        //mockServer.expect(requestTo("http://localhost:8888/job/gotopoint/8")).andExpect(method(HttpMethod.POST))
                //.andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Dispatch second job, second list
        mockServer.expect(requestTo("http://localhost:8888/job/execute/8/11/4")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));


        //** Start simulation
        //Test data is loaded by databaseLoaderDevelopment
        jobListService.dispatchToBackend();

        // Add second list
        JobList list2 = new JobList();
        list2.setIdDelivery(2);

        Job job = new Job(4L,20L,11,1);
        list2.addJob(job);

        job = new Job(8L,11L,10,1);
        list2.addJob(job);
        jobListService.saveJobList(list2);

        int oldsize = jobListService.findAll().size();

        jobListService.dispatchToBackend();

        // Job 1, list 1
        //sendCloseBy(1L);
        sendDone(1L);

        // Job 1, list 2
        //sendCloseBy(3L);
        sendDone(3L);

        // Job 2, list 1
        //sendCloseBy(2L);
        sendDone(2L);

        // Job 2, list 2
        //sendCloseBy(4L);
        sendDone(4L);

        int newsize = jobListService.findAll().size();
        assert(newsize == oldsize - 2);
    }

    private void sendCloseBy(Long id) {
        jobDispatchController.vehicleCloseByToEnd(id);
    }

    private void sendDone(Long id) {
        jobDispatchController.completeJob(id);
    }
}
