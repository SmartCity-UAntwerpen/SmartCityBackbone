package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.services.LinkControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link/")
public class LinkController {
    @Autowired
    private LinkControlService linkControlService;

    /**
     * @return A List of Link objects, acquired from LinkControlService
     */

    @RequestMapping(method = RequestMethod.GET)
    public List<Link> allLinks() {
        List<Link> linkEntityList = linkControlService.getAllLinks();
        return linkEntityList;
    }

    @RequestMapping(value = "updateBotTest/{id}", method = RequestMethod.GET)
    public void updateBotTest(@PathVariable("id") Long id) {

    }
}

