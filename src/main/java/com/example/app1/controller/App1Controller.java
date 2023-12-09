package com.example.app1.controller;

import com.example.app1.service.App1Serice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app1")
public class App1Controller {

    @Autowired
    private App1Serice service;

    @GetMapping("/all")
    List<Map<String,Object>>  getAll(){
      return   service.getAll();
    }

    @PostMapping("/insert")
    String insert(@RequestBody String name) throws UnsupportedEncodingException {
        return   service.insert(name);
    }

    @PostMapping("/insert1")
    Map<String,String> insert1(@RequestBody Map<String,String> payload) throws UnsupportedEncodingException {
        return   service.insert1(payload);
    }
}
