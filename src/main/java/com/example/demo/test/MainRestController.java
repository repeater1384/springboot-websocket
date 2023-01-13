package com.example.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class MainRestController {
    @Autowired
    WebSocket webSocket;

    @GetMapping(value = "/customer_len")
    public ResponseEntity<?> main() {
        String msg = "" + webSocket.getSessionList();
        System.out.println("api");
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}