package com.example.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class MainRestController {
    @Autowired
    WebSocket webSocket;

    @GetMapping(value = "/customer_len")
    public ResponseEntity<?> customer_len() {
//        String msg = "" + webSocket.getSessionList().size();
        String msg = "test";
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @GetMapping(value = "/customer_list")
    public ResponseEntity<?> customer_list() {
//        for (Session singleSession :
//                webSocket.getSessionList()) {
//            if (singleSession.isOpen()) {
//                result.add(singleSession.getId());
//            }
//        }
//        result.add("개발중, localhost 바꿔야함ㅋㅋ");
        List<String> result = webSocket.getUserNameList();
        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

}