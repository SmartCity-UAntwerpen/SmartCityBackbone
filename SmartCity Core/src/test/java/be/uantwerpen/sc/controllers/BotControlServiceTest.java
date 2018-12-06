package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.SmartCityCoreApplication;
import be.uantwerpen.sc.models.Bot;
import be.uantwerpen.sc.services.BotControlService;
import be.uantwerpen.sc.services.LinkControlService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by quent on 6/10/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SmartCityCoreApplication.class)
public class BotControlServiceTest {

    private Long idBot1;
    private Long idBot2;

    @Mock
    private BotControlService mockBotService;

    @Mock
    private LinkControlService mockLinkService;

    @Before
    public void inititTest(){

        MockitoAnnotations.initMocks(this);

        mockBotService.resetBots();
        Bot bot1 = new Bot();
        idBot1 = bot1.getId();
        bot1.setLinkId(mockLinkService.getLink(1L));
        bot1.setPercentageCompleted(100);
        mockBotService.saveBot(bot1);

        Bot bot2 = new Bot();
        idBot2 = bot2.getId();
        bot2.setLinkId(mockLinkService.getLink(1L));
        bot2.setPercentageCompleted(50);
        mockBotService.saveBot(bot2);
    }

    @After
    public void resetTest(){
        mockBotService.resetBots();
    }

//    @Test
//    public void posAll() throws Exception {
//        System.out.println(mockBotService.getPosAll());
//        assertEquals(" ",mockBotService.getPosAll());
//    }
//
//    @Test
//    public void posOne() throws Exception {
//        System.out.println(mockBotService.getPosOne(idBot1));
//        assertEquals(" ",mockBotService.getPosOne(idBot1));
//    }

}