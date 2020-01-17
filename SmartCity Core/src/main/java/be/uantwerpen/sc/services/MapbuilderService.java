package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.mapbuilder.*;
import be.uantwerpen.sc.repositories.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapbuilderService {
    private static final Logger logger = LogManager.getLogger(MapbuilderService.class);

    @Autowired
    private CarPointRepository carPointRepository;
    @Autowired
    private CarLinkRepository carLinkRepository;
    @Autowired
    private DroneLinkRepository droneLinkRepository;
    @Autowired
    private DronePointRepository dronePointRepository;
    @Autowired
    private RobotTileRepository robotTileRepository;
    @Autowired
    private RobotLinkRepository robotLinkRepository;
    @Autowired
    private LinkLockRepository linkLockRepository;
    @Autowired
    private MBTransitLinkRepository mbTransitLinkRepository;

    public CarPoint save(final CarPoint carPoint){
        return this.carPointRepository.save(carPoint);
    }

    public CarLink save(final CarLink carLink){
        return this.carLinkRepository.save(carLink);
    }

    public DroneLink save(final DroneLink droneLink){
        return this.droneLinkRepository.save(droneLink);
    }

    public DronePoint save(final DronePoint dronePoint){
        return this.dronePointRepository.save(dronePoint);
    }

    public RobotTile save(final RobotTile robotTile){
        return this.robotTileRepository.save(robotTile);
    }

    public RobotLink save(final RobotLink robotLink){
        return this.robotLinkRepository.save(robotLink);
    }

    public LinkLock save(final LinkLock linkLock){
        return this.linkLockRepository.save(linkLock);
    }

    public MBTransitLink save(final MBTransitLink mbTransitLink){
        return this.mbTransitLinkRepository.save(mbTransitLink);
    }

    public List<CarPoint> findAllCarpoints(){
        return this.carPointRepository.findAll();
    }

    public List<CarLink> findAllCarLinks(){
        return this.carLinkRepository.findAll();
    }

    public List<DroneLink> findAllDroneLinks(){
        return this.droneLinkRepository.findAll();
    }

    public List<DronePoint> findAllDronePoints(){
        return this.dronePointRepository.findAll();
    }

    public List<RobotTile> findAllRobotTiles(){
        return this.robotTileRepository.findAll();
    }

    public List<RobotLink> findAllRobotLinks(){
        return this.robotLinkRepository.findAll();
    }

    public List<LinkLock> findAllLinkLocks(){
        return this.linkLockRepository.findAll();
    }

    public List<MBTransitLink> findAllTransitLinks(){
        return this.mbTransitLinkRepository.findAll();
    }

    public void eraseMapdata(){
        this.carPointRepository.deleteAll();
        this.carLinkRepository.deleteAll();
        this.dronePointRepository.deleteAll();
        this.droneLinkRepository.deleteAll();
        this.robotLinkRepository.deleteAll();
        this.robotTileRepository.deleteAll();
        this.linkLockRepository.deleteAll();
        this.mbTransitLinkRepository.deleteAll();
    }

}
