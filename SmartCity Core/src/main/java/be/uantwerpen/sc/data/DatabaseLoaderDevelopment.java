package be.uantwerpen.sc.data;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("dev")
public class DatabaseLoaderDevelopment {

    private final BackendInfoRepository backendInfoRepository;

    @Autowired
    public DatabaseLoaderDevelopment(BackendInfoRepository backendInfoRepository) {
        this.backendInfoRepository = backendInfoRepository;
    }

    @PostConstruct
    private void initData() {
            initBackendInfo();
    }

    private void initBackendInfo() {
        BackendInfo info = new BackendInfo("localhost",9999,7,"Test");
        backendInfoRepository.save(info);
    }
}
