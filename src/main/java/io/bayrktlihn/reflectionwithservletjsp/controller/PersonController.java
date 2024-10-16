package io.bayrktlihn.reflectionwithservletjsp.controller;


import io.bayrktlihn.reflectionwithservletjsp.annotations.RequestMapping;
import io.bayrktlihn.reflectionwithservletjsp.annotations.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/v1/persons")
public class PersonController {


    @RequestMapping(path = "")
    public void listPerson(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        System.out.println("I'm here");
    }

}
