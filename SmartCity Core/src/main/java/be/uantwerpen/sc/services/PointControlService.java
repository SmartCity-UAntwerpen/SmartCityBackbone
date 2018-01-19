package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.repositories.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointControlService {
    @Autowired
    private PointRepository pointRepository;

    /**
     * @return List of all points in the db
     */
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public Point getPoint(Long id) {
        return pointRepository.findOne(id);
    }

    public Point save(Point point) {
        return pointRepository.save(point);
    }

  /*  public boolean clearAllLocks()
    {
        for(Point point : pointRepository.findAll())
        {
            point.setPointLock(0);
            this.save(point);
        }

        return true;
    }*/
}
