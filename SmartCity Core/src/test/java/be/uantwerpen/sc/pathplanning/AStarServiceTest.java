package be.uantwerpen.sc.pathplanning;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import be.uantwerpen.sc.services.AStarService;
import be.uantwerpen.sc.services.GraphBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class AStarServiceTest {

    @Mock
    private GraphBuilder graphBuilder;
    @Mock
    private TransitPointRepository transitPointRepository;

    @InjectMocks
    @Resource
    private AStarService aStarService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        List<TransitPoint> transitPoints = new ArrayList<>();
        transitPoints.add(new TransitPoint(1, 1, 46));
        transitPoints.add(new TransitPoint(2, 1, 47));
        transitPoints.add(new TransitPoint(3, 1, 48));
        transitPoints.add(new TransitPoint(4, 2, 46));
        transitPoints.add(new TransitPoint(5, 2, 47));
        transitPoints.add(new TransitPoint(6, 2, 48));
        transitPoints.add(new TransitPoint(7, 3, 46));
        transitPoints.add(new TransitPoint(8, 3, 47));
        transitPoints.add(new TransitPoint(9, 3, 48));

        List<TransitLink> transitLinks = new ArrayList<>();
        transitLinks.add(new TransitLink(10, 1, 7, 10));
        transitLinks.add(new TransitLink(11, 9, 2, 40));
        transitLinks.add(new TransitLink(12, 9, 5, 2));
        transitLinks.add(new TransitLink(13, 5, 8, 3));
        transitLinks.add(new TransitLink(14, 6, 2, 9));
        transitLinks.add(new TransitLink(15, 4, 3, 1));
        transitLinks.add(new TransitLink(16, 3, 4, 5));

        when(graphBuilder.getPointList()).thenReturn(transitPoints);
        when(graphBuilder.getLinkList()).thenReturn(transitLinks);
        when(transitPointRepository.findById(anyInt())).thenAnswer((Answer<TransitPoint>) invocationOnMock -> {
            int id = invocationOnMock.getArgumentAt(0, int.class);
            return transitPoints.stream().filter(transitPoint -> transitPoint.getId() == id).findFirst().orElse(null);
        });
        when(transitPointRepository.findByPidAndMapid(anyInt(), anyInt())).thenAnswer((Answer<TransitPoint>) invocationOnMock -> {
            int pid = invocationOnMock.getArgumentAt(0, int.class);
            int mapId = invocationOnMock.getArgumentAt(1, int.class);
            return transitPoints.stream().filter(transitPoint -> transitPoint.getPid() == pid && transitPoint.getMapid() == mapId).findFirst().orElse(null);
        });
    }

    @Test
    public void determinePath() {
        assertEquals(9, graphBuilder.getPointList().size());
        assertEquals(7, graphBuilder.getLinkList().size());

        TransitPoint transitPointA = transitPointRepository.findById(7);
        assertEquals(7, transitPointA.getId());

        TransitPoint transitPointB = transitPointRepository.findByPidAndMapid(46, 3);
        assertEquals(7, transitPointB.getId());

        List<Integer[]> routes = aStarService.determinePath(46, 3, 47, 1);
        assertEquals(5, routes.size());

        assertEquals(2, routes.get(0).length);
        assertEquals(12, (int) routes.get(0)[0]);
        assertEquals(15, (int) routes.get(0)[1]);

        assertEquals(2, routes.get(1).length);
        assertEquals(12, (int) routes.get(1)[0]);
        assertEquals(16, (int) routes.get(1)[1]);

        assertEquals(2, routes.get(2).length);
        assertEquals(13, (int) routes.get(2)[0]);
        assertEquals(16, (int) routes.get(2)[1]);

        assertEquals(1, routes.get(3).length);
        assertEquals(10, (int) routes.get(3)[0]);

        assertEquals(1, routes.get(4).length);
        assertEquals(11, (int) routes.get(4)[0]);
    }

}