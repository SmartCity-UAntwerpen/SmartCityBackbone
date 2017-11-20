package be.uantwerpen.sc.configurations;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * TODO: fix descriptor
 *
 * Created by Thomas on 28/03/2016.
 */
@Profile("standalone")
@Configuration
@Import(EmbeddedServletContainerAutoConfiguration.class)
public class StandAloneConfiguration
{
    //Configurations for Stand-Alone mode
    //TODO: configure this?
}
