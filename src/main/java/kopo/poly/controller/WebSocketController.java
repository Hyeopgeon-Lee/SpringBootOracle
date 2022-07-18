package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@Slf4j
@Controller
@RequestMapping(value = "/chat")
@ServerEndpoint(value = "/ws")
public class WebSocketController {

    public WebSocketController() {
        log.info("웹소켓 서버 객체 생성");

    }

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());


    /**
     * 채팅창 입장 전
     */
    @RequestMapping(value = "intro")
    public String intro() throws Exception {
        return "/chat/intro";
    }

    /**
     * 채팅창 접속
     */
    @RequestMapping(value = "room")
    public String room() throws Exception {
        return "/chat/chatroom";
    }



    @OnOpen
    public void onOpen(Session s) {
        System.out.println("open session : " + s.toString());
        log.info("Open session id:" + s.getId());
        if (!clients.contains(s)) {
            clients.add(s);
            System.out.println("session open : " + s);
        } else {
            System.out.println("이미 연결된 session 임!!!");
        }
    }


    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        System.out.println("receive message : " + msg);

        for (Session s : clients) {

            System.out.println("send data : " + msg);

            s.getBasicRemote().sendText(msg);

        }

    }

    @OnError
    public void onError(Throwable e, Session session) {

    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("session close : " + s);
        clients.remove(s);
    }
}