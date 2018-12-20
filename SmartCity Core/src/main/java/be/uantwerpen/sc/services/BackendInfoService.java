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

    public BackendInfo getInfoByMapId(int mapid) {
        return backendInfoRepository.findByMapId(mapid);
    }

    public List<BackendInfo> findAll() {
        return backendInfoRepository.findAll();
    }

    public BackendInfo getByName(String name)
    {
        return backendInfoRepository.findByName(name);
    }
}
