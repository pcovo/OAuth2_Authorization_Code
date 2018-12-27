package com.oauth2.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ResourceController {

    @RequestMapping(value = "/api/getRecursoProtegido", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String getRecursoProtegido() {
        return "Recurso Protegido";

    }

}