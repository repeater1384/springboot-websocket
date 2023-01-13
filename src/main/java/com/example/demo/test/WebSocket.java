
package com.example.demo.test;

import java.util.*;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/websocket")
public class WebSocket {

    /**
     * 웹소켓 세션을 담는 ArrayList
     */
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    private static Map<String, String> sessionName = new HashMap<>();

    public List<Session> getSessionList() {
        return sessionList;
    }

    /**
     * 웹소켓 사용자 연결 성립하는 경우 호출
     */
    @OnOpen
    public void handleOpen(Session session) {
        if (session != null) {
            String sessionId = session.getId();

            System.out.println("client is connected. sessionId == [" + sessionName.get(session.getId()) + "]");
            sessionList.add(session);

            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
            sendMessageToAll("***** [USER-" + sessionName.get(session.getId()) + "] is connected. *****");
        }
    }


    /**
     * 웹소켓 메시지(From Client) 수신하는 경우 호출
     */
    @OnMessage
    public String handleMessage(String message, Session session) {
        if (session != null) {
            if (message.startsWith("initname:")) {
                sessionName.put(session.getId(), message.substring(9));
                System.out.println(sessionName.get(session.getId()) + "이름 설정 완료");
            } else {
                String sessionId = session.getId();
                System.out.println("message is arrived. sessionId == [" + sessionName.get(sessionId) + "] / message == [" + message + "]");
                sendMessageToAll("[" + sessionName.get(sessionId) + "] " + message);
            }

            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
        }

        return null;
    }


    /**
     * 웹소켓 사용자 연결 해제하는 경우 호출
     */
    @OnClose
    public void handleClose(Session session) {
        if (session != null) {
            String sessionId = session.getId();
            System.out.println("client is disconnected. sessionId == [" + sessionName.get(sessionId) + "]");
            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
            sendMessageToAll("***** [USER-" + sessionName.get(sessionId) + "] is disconnected. *****");
        }
    }


    /**
     * 웹소켓 에러 발생하는 경우 호출
     */
    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }


    /**
     * 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
     */
    private boolean sendMessageToAll(String message) {
        if (sessionList == null) {
            return false;
        }

        int sessionCount = sessionList.size();
        if (sessionCount < 1) {
            return false;
        }

        Session singleSession = null;

        for (int i = 0; i < sessionCount; i++) {
            singleSession = sessionList.get(i);
            if (singleSession == null) {
                continue;
            }

            if (!singleSession.isOpen()) {
                continue;
            }

            sessionList.get(i).getAsyncRemote().sendText(message);
        }

        return true;
    }
}


//import java.util.ArrayList;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import org.springframework.stereotype.Component;
//
//@Component
//@ServerEndpoint("/websocket”)
//public class Websocket {
//
//    /**
//     * 웹소켓 세션을 담는 ArrayList
//     */
//    private static ArrayList<Session> sessionList = new ArrayList<Session>();
//
//
//    /**
//     * 웹소켓 사용자 연결 성립하는 경우 호출
//     */
//    @OnOpen
//    public void handleOpen(Session session) {
//        if (session != null) {
//            String sessionId = session.getId();
//
//            System.out.println("client is connected. sessionId == [" + sessionId + "]”);
//            sessionList.add(session);
//
//            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
//            sendMessageToAll("***** [USER-" + sessionId + "] is connected. *****”);
//        }
//    }
//
//
//    /**
//     * 웹소켓 메시지(From Client) 수신하는 경우 호출
//     */
//    @OnMessage
//    public String handleMessage(String message, Session session) {
//        if (session != null) {
//            String sessionId = session.getId();
//            System.out.println("message is arrived. sessionId == [" + sessionId + "] / message == [" + message + "]”);
//
//            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
//            sendMessageToAll("[USER-" + sessionId + "] " + message);
//        }
//
//        return null;
//    }
//
//
//    /**
//     * 웹소켓 사용자 연결 해제하는 경우 호출
//     */
//    @OnClose
//    public void handleClose(Session session) {
//        if (session != null) {
//            String sessionId = session.getId();
//            System.out.println("client is disconnected. sessionId == [" + sessionId + "]”);
//
//            // 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
//            sendMessageToAll("***** [USER-" + sessionId + "] is disconnected. *****”);
//        }
//    }
//
//
//    /**
//     * 웹소켓 에러 발생하는 경우 호출
//     */
//    @OnError
//    public void handleError(Throwable t) {
//        t.printStackTrace();
//    }
//
//
//    /**
//     * 웹소켓 연결 성립되어 있는 모든 사용자에게 메시지 전송
//     */
//    private boolean sendMessageToAll(String message) {
//        if (sessionList == null) {
//            return false;
//        }
//
//        int sessionCount = sessionList.size();
//        if (sessionCount < 1) {
//            return false;
//        }
//
//        Session singleSession = null;
//
//        for (int i = 0; i < sessionCount; i++) {
//            singleSession = sessionList.get(i);
//            if (singleSession == null) {
//                continue;
//            }
//
//            if (!singleSession.isOpen()) {
//                continue;
//            }
//
//            sessionList.get(i).getAsyncRemote().sendText(message);
//        }
//
//        return true;
//    }
//}