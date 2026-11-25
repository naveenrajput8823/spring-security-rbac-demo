package com.training.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class MainController {

    @GetMapping
    public String index() {
        return "Application is Up and Running!";
    }

    @GetMapping("/admin/greet")
    public String greetByAdmin(){
        return "Hello World By Admin";
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('SEE_ADMINS')")
//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('SEE_ADMINS')")
//    @PreAuthorize("hasAnyAuthority('SEE_ADMINS', 'MANAGE_ADMINS')")
//    @PreAuthorize("""
//        hasRole('ADMIN') or
//        hasAnyAuthority('SEE_ADMINS', 'MANAGE_ADMINS')
//    """)
    @GetMapping("/see-admins")
    public String seeAdmin(){
        return "giving list of all Admins";
    }


    @GetMapping("/user/greet")
    public String greetByUser(){
        return "Hello World By User";
    }

    @GetMapping("/vendor/greet")
    public String greetByVendor(){
        return "Hello World By vendor";
    }

}
