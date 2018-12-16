package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.links.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackendInfoRepository extends CrudRepository<BackendInfo, Long> {
    List<BackendInfo> findAll();
    BackendInfo findBackendInfoRepositoryBymapId(int mapId);
}