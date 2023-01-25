//package com.example.demo.test;
//
//import javax.websocket.EncodeException;
//import javax.websocket.Session;
//import java.io.IOException;
//
//public class Room {
//    public synchronized void join(Session session) {
//        sessions.add(session);
//    }
//
//    public synchronized void leave(Session session) {
//        sendMessage(new Message("Korean Lab", format("%s left the chat room%", (String) session.getUser...
//        sessions.remove(session);
//    }
//
//    public synchronized void sendMessage(Message message) {
//        sessions.parallelStream()
//                .filter(Session::isOpen)
//                .forEach(session -> sendMessage(message, session));
//    }
//
//    private void sendMessage(Message message, Session session) {
//        try {
//            session.getBasicRemote().sendObject(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (EncodeException e {
//            e.printStackTrace();
//        }
//    }
//}
