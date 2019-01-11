package be.uantwerpen.sc.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        devConfiguration(http);
    }

    protected void devConfiguration(HttpSecurity http) throws Exception {
        for (String profile : environment.getActiveProfiles()) {
            // disable by default
            http.csrf().disable();
            if (profile.equals("dev")) {
                //Permit access to H2 console --Development only
                http.authorizeRequests().antMatchers("/h2console/**")
                        .permitAll();

//                http.csrf().disable();
                http.headers().frameOptions().disable();

                return;
            }
        }
    }
}
