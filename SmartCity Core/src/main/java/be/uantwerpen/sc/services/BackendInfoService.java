package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackendInfoService {

    @Autowired
    private BackendInfoRepository backendInfoRepository;

    public BackendInfo getInfoById(int mapid) {
        return backendInfoRepository.findBymapId(mapid);
    }

    public List<BackendInfo> findAll() {
        return backendInfoRepository.findAll();
    }
}
