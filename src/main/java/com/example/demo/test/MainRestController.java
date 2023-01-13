package com.example.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class MainRestController {
    @Autowired
    WebSocket webSocket;

    @GetMapping(value = "/customer_len")
    public ResponseEntity<?> customer_len() {
        String msg = "" + webSocket.getSessionList().size();
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @GetMapping(value = "/customer_list")
    public ResponseEntity<?> customer_list() {
        List<String> result = new ArrayList<>();
        for (Session singleSession :
                webSocket.getSessionList()) {
            if (singleSession.isOpen()) {
                result.add(singleSession.getId());
            }
        }

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

}