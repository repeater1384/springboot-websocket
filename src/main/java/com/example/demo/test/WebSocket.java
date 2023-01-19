
package com.example.demo.test;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@ServerEndpoint("/websocket")
public class WebSocket {

    private static Set<String> sessionSet = new HashSet<String>();
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static Map<String, Object> sessionId2Obj = new HashMap<>();
    private static Map<String, Customer> customerMap = new HashMap<>();
    private static Map<String, Seller> sellerMap = new HashMap<>();
    private static Set<String> customersSet = new HashSet<String>();
    private static Set<String> sellerSet = Collections.synchronizedSet(new HashSet<String>());
    private static Map<String, LocalTime> refreshMap = new HashMap<>();

    static Random random = new Random();
    static boolean runCheck = false;

    @OnOpen
    public void handleOpen(Session session) {
        if (session != null) {
            String sessionId = session.getId();
            sessionSet.add(sessionId);
            sessionMap.put(sessionId, session);
            printInfo();

            if (!runCheck) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        LocalTime now = LocalTime.now();
                        System.out.println(now);  // 06:20:57.008731300
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
                        String formatedNow = now.format(formatter);
                        int randInt = Math.abs(random.nextInt()) % 100;
                        String msg = String.format("{%s} : [{%d}]{%s}", formatedNow, randInt, "서버에서 보내는 랜덤메세지 입니다.");
                        sendMessageToAll(msg);
                    }
                };
                runCheck = true;
                Timer timer = new Timer(true);
                timer.scheduleAtFixedRate(task, 0, 15000);
            }
            ;
        }
    }


    /**
     * 웹소켓 메시지(From Client) 수신하는 경우 호출
     */
    @OnMessage
    public String handleMessage(String jsonMessage, Session session) throws ParseException, IOException {
        if (session != null) {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonMessage);
            System.out.println(jsonMessage);
            String method = obj.get("method").toString();
            Object object = null;
            if (method.equals("init")) {
                String id = obj.get("id").toString();
                String name = obj.get("name").toString();
                String wido = obj.get("wido").toString();
                String gyungdo = obj.get("gyungdo").toString();
                if (obj.get("type").toString().equals("판매자")) {
                    if (refreshMap.containsKey(id + "판매자")) {
                        LocalTime first = refreshMap.get(id + "판매자");
                        LocalTime second = LocalTime.now();
                        if (Duration.between(first, second).getSeconds() <= 1) {
                            sendMessageToAll("[판매자/" + name + "] 님이 새로고침 하셨어요~~");
                        } else {
                            sendMessageToAll("[판매자/" + name + "] 님이 다시 들어오셨어요~~");
                        }
                    }
                    Seller seller = Seller.builder().id(id).name(name).wido(wido).gyungdo(gyungdo).build();
                    sellerSet.add(id);
                    sellerMap.put(id, seller);
                    System.out.println("판매자 입장");
                    object = seller;
                } else if (obj.get("type").toString().equals("고객")) {
                    if (refreshMap.containsKey(id + "고객")) {
                        LocalTime first = refreshMap.get(id + "고객");
                        LocalTime second = LocalTime.now();
                        if (Duration.between(first, second).getSeconds() <= 1) {
                            sendMessageToAll("[고객/" + name + "] 님이 새로고침 하셨어요~~");
                        } else {
                            sendMessageToAll("[고객/" + name + "] 님이 다시 들어오셨어요~~");
                        }
                    }
                    Customer customer = Customer.builder().id(id).name(name).wido(wido).gyungdo(gyungdo).build();
                    customersSet.add(id);
                    customerMap.put(id, customer);
                    System.out.println("소비자 입장");
                    object = customer;
                }
                sessionId2Obj.put(session.getId(), object);

            } else if (method.equals("msg")) {
                String msg = obj.get("msg").toString();
                StringBuilder sb = new StringBuilder();
                object = sessionId2Obj.get(session.getId());
                if (object instanceof Customer) {
                    String id = ((Customer) object).getId();
                    String name = customerMap.get(id).getName();
                    sb.append("[고객/").append(name).append("]");
                } else if (object instanceof Seller) {
                    String id = ((Seller) object).getId();
                    String name = sellerMap.get(id).getName();
                    sb.append("[판매자/").append(name).append("]");
                }
                sb.append(" : ").append(msg);
                sendMessageToAll(sb.toString());
            }
            printInfo();
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
            sessionSet.remove(sessionId);

            Object obj = sessionId2Obj.get(sessionId);
            sessionId2Obj.remove(sessionId);
            String type = null;
            String id = null;
            if (obj instanceof Customer) {
                type = "고객";
                id = ((Customer) obj).getId();
                customersSet.remove(id);
                customerMap.remove(id);
            } else if (obj instanceof Seller) {
                type = "판매자";
                id = ((Seller) obj).getId();
                sellerSet.remove(id);
                sellerMap.remove(id);
            }

            refreshMap.put(id + type, LocalTime.now());
            printInfo();
        }
    }


    /**
     * 웹소켓 에러 발생하는 경우 호출
     */
    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    public void printInfo() {

        System.out.println(sessionSet);
        System.out.println(customersSet);
        System.out.println(sellerSet);
        System.out.println(customerMap);
        System.out.println(sellerMap);
        System.out.println("------------------------------");
    }

    public Set<String> getSessionSet() {
        return this.sessionSet;
    }

    public double getDistance(String w1, String g1, String w2, String g2) {
        double lat1 = Double.parseDouble(w1);
        double lon1 = Double.parseDouble(g1);
        double lat2 = Double.parseDouble(w2);
        double lon2 = Double.parseDouble(g2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6371 * c * 1000;    // Distance in m
        return d;
    }

    public Set<Map<String, String>> getCustomerSet(String sellerId) {
        Set<Map<String, String>> result = new HashSet<>();
        Seller seller = sellerMap.get(sellerId);
        String wido1 = seller.getWido();
        String gyungdo1 = seller.getGyungdo();
        for (String key : customersSet) {
            Map<String, String> row = new HashMap<>();
            Customer customer = customerMap.get(key);
            row.put("id", customer.getId());
            row.put("name", customer.getName());
            row.put("wido", customer.getWido());
            row.put("gyungdo", customer.getGyungdo());
            row.put("dis", String.valueOf(getDistance(wido1, gyungdo1, customer.getWido(), customer.getGyungdo())));
            result.add(row);
        }
        return result;
    }

    public Set<String> getSellerSet() {
        Set<String> result = new HashSet<>();
        for (String key :
                sellerSet) {
            result.add(sellerMap.get(key).getName());

        }
        return result;
    }

    private boolean sendMessageToAll(String message) {
        int sessionCount = sessionSet.size();
        if (sessionCount < 1) {
            return false;
        }

        Session singleSession = null;

        for (String name : sessionSet) {

            singleSession = sessionMap.get(name);
            if (singleSession == null) {
                continue;
            }
            if (!singleSession.isOpen()) {
                continue;
            }
            singleSession.getAsyncRemote().sendText(message);
        }

        return true;
    }


}
