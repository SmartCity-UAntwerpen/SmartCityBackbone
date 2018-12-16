package be.uantwerpen.sc.data;


import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.services.BackendInfoService;
import be.uantwerpen.sc.services.JobListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Profile("dev")
public class DatabaseLoaderDevelopment {

    @Autowired
    JobListService jobListService;

    @Autowired
    BackendInfoRepository backendInfoRepository;

    @PostConstruct
    public void initDatabase() {
        initJobLists();
        initBackends();
       test();
    }

    private void initJobLists() {
        JobList list = new JobList();
        list.setIdDelivery("MaaSId1");

        Job job = new Job(1L,5L,10);
        list.addJob(job);

        job = new Job(7L,10L,11);
        list.addJob(job);

        jobListService.saveOrder(list);
    }

    private void initBackends() {
        BackendInfo backend = new BackendInfo("localhost",7777,11,"BotTest");
        backendInfoRepository.save(backend);
    }

    private void test() {
        List<JobList> list = jobListService.findAll();
        System.out.println(list.get(0).getJobs());

        System.out.println(backendInfoRepository.findAll());
    }
}
