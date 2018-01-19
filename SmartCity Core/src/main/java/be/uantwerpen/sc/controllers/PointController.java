package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.services.PointControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/point/")
public class PointController {
    @Autowired
    private PointControlService pointService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Point> allPoints() {
        List<Point> points = pointService.getAllPoints();

        return points;
    }

}
