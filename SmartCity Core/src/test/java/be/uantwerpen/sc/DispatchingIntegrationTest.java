package be.uantwerpen.sc;

import be.uantwerpen.sc.controllers.JobDispatchController;
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
public class DispatchingIntegrationTest {
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
        //:8083/job/execute/{pidstart}/{pidstop}/{jobid}
        mockServer.expect(requestTo("http://localhost:7777/job/execute/1/5/1")).andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));
        // Check transit behaviour. Uncomment if this method is used in the real environment
        //mockServer.expect(requestTo("http://localhost:8888/job/gotopoint/7")).andExpect(method(HttpMethod.POST))
                //.andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        // Dispatch second job
        mockServer.expect(requestTo("http://localhost:8888/job/execute/7/10/2")).andExpect(method(HttpMethod.POST))
               .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        int oldsize = jobListService.findAll().size();

        //** Start simulation
        //Test data is loaded by databaseLoaderDevelopment
        jobListService.dispatchToBackend();

        //sendCloseBy(1L);
        sendDone(1L);

        // job 2
        //sendCloseBy(2L);
        sendDone(2L);

        int newsize = jobListService.findAll().size();
        assert(newsize == oldsize - 1);
    }

    private void sendCloseBy(Long id) {
        jobDispatchController.vehicleCloseByToEnd(id);
    }

    private void sendDone(Long id) {
        jobDispatchController.completeJob(id);
    }
}
