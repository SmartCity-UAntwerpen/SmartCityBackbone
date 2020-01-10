package be.uantwerpen.sc.data;


import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.jobs.JobList;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.services.JobListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Profile("devdata")
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
        list.setIdDelivery(1);

        Job job = new Job(1L,5L,5,1);
        list.addJob(job);

        job = new Job(7L,10L,10,1);
        list.addJob(job);
        jobListService.saveJobList(list);
    }

    private void initBackends() {
        //BackendInfo backend = new BackendInfo("localhost",7777,11,"BotTest");
        BackendInfo backend = new BackendInfo("smartcity.ddns.net",7777,5,"BotTest");
        backendInfoRepository.save(backend);

        //backend = new BackendInfo("localhost",8888,10,"BotTest");
        backend = new BackendInfo("smartcity.ddns.net",8888,10,"BotTest");
        backendInfoRepository.save(backend);
    }

    private void test() {
        List<JobList> list = jobListService.findAll();
        System.out.println(list.get(0).getJobs());

        System.out.println(backendInfoRepository.findAll());
    }
}
