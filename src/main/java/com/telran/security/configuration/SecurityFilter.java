package com.telran.security.configuration;

import com.telran.security.entity.Developer;
import com.telran.security.repository.DeveloperRepository;
import com.telran.security.repository.DeveloperRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperRoleRepository developerRoleRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        //Header:Authorization should contain developerId

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {

            Integer developerId = Integer.parseInt(authorizationHeader);

            Developer developer = developerRepository.findById(developerId).orElse(null);

            if (developer != null) {

                List<GrantedAuthority> roles = developerRoleRepository.findAllByDeveloper(developer)
                        .stream()
                        .map(developerRole -> developerRole.getRole().getRoleName())
                        .map(roleName -> new SimpleGrantedAuthority(roleName))
                        .collect(Collectors.toList());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    developer.getName(), //principal
                    null,       //can skip this param
                    roles                //ROLE INSERT HERE
                );

                System.out.println("Authentication: " + authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
