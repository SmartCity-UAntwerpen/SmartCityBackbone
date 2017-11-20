package be.uantwerpen.sc.configurations;

import be.uantwerpen.sc.services.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Runs after other configurations, starts TerminalService
 *
 * Created by Thomas on 25/02/2016.
 */
@Configuration
public class SystemLoader implements ApplicationRunner
{
    /**
     * TerminalService allows the user to input arguments in the console
     *
     * @see TerminalService
     */
    @Autowired
    private TerminalService terminalService;

    //Run after Spring context initialization
    public void run(ApplicationArguments args)
    {
        new Thread(new StartupProcess()).start();
    }

    private class StartupProcess implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(200);
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
                //Thread interrupted
            }

            terminalService.systemReady();
        }
    }
}
