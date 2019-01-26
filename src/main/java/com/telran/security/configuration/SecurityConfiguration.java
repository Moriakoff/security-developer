package com.telran.security.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/my-profile").hasAnyAuthority("DEVELOPER", "ADMIN_DEVELOPER")

                .antMatchers("/new").permitAll()

                .antMatchers("/add-language").hasAuthority("DEVELOPER")

                .antMatchers("/search").hasAuthority("DEVELOPER")

                .antMatchers("/create-language").hasAuthority("ADMIN_DEVELOPER")

                .antMatchers("/promote").hasAuthority("ADMIN_DEVELOPER")

                .antMatchers("/demote").hasAuthority("ADMIN_DEVELOPER")

                .antMatchers("/users").hasAuthority("ADMIN_DEVELOPER");

        http.addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class);

        //.authenticated = key != null
        //hasAuthority = key must contain the corresponding role/authority


//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//          "username",
//          null,
//          new ArrayList<>()
//        );
    }
}
