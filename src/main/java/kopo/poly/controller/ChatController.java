package kopo.poly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.ChatDTO;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/chat")
@ServerEndpoint(value = "/ws/{roomName}/{userName}")
public class ChatController {

    // 웹소켓에 접속되는 사용자들을 저장하며, 중복을 제거하기 위해 Set 데이터 구조 사용함
    private static Set<Session> clients = Collections.synchronizedSet(new LinkedHashSet<>());

    // 채팅룸 조회하기 위해 사용
    private static Map<String, String> roomInfo = Collections.synchronizedMap(new LinkedHashMap<>());

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

    /**
     * 채팅방 목록
     */
    @RequestMapping(value = "roomList")
    @ResponseBody
    public Set<String> roomList() throws Exception {

        log.info(this.getClass().getName() + ".roomList Start!");

        log.info(this.getClass().getName() + ".roomList Ends!");

        return roomInfo.keySet();

    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomName") String roomName,
                       @PathParam("userName") String userName) throws Exception {

        log.info(this.getClass().getName() + ".onOpen Start!");

        log.info("session : " + session);
        log.info("session id:" + session.getId());
        log.info("roomName : " + roomName);
        log.info("userName : " + userName);

        // 채팅룸 이름이 한글 및 특수문자로 입력될 수 있기에 채팅룸 이름은 데이터 처리에 문제없는 영문이나 숫자로 변환해야 함
        // 채팅룸 이름은 해시 함수를 이용하여 영문명으로 변경함
        String roomNameHash = EncryptUtil.encHashSHA256(roomName);

        session.getUserProperties().putIfAbsent("roomName", roomNameHash); // 채팅룸 이름의 해시 값 저장
        session.getUserProperties().putIfAbsent("userName", userName); // 채팅유저 이름 저장

        log.info("session roomName : " + session.getUserProperties().get("roomName"));

        // 웹소켓에 접속된 모든 사용자 검색
        clients.stream().forEach(s -> {

            log.info("s.room : " + s.getUserProperties().get("roomName"));
            log.info("s.userName : " + s.getUserProperties().get("userName"));

            // 내가 접속한 채팅방에 있는 세션만 메시지 보내기
            if (roomNameHash.equals(s.getUserProperties().get("roomName"))) {

                try {

                    //{"name":"이협건","msg":"ㅇㅎ","date":"2022. 7. 25. 오전 9:30:57"}
                    ChatDTO cDTO = new ChatDTO();
                    cDTO.setName("관리자");
                    cDTO.setMsg(userName + "님이 " + roomName + " 채팅방에 입장하셨습니다.");
                    cDTO.setDate(DateUtil.getDateTime("yyyyMMdd hh:mm:ss"));

                    String json = new ObjectMapper().writeValueAsString(cDTO);
                    log.info("json : " + json);

                    s.getBasicRemote().sendText(json);

                    cDTO = null;

                } catch (IOException e) {
                    log.info("Error : " + e);
                }
            }
        });

        // 기존 세션에 존재하지 않으면, 신규 세션이기에 저장함
        if (!clients.contains(session)) {

            clients.add(session); // 접속된 세션 저장

            roomInfo.put(roomName, roomNameHash); // 생성된 채팅룸 이름 저장

            log.info("session open : " + session);

        }

        log.info(this.getClass().getName() + ".onOpen End!");

    }


    @OnMessage
    public void onMessage(Session session, String msg, @PathParam("roomName") String roomName,
                          @PathParam("userName") String userName) throws Exception {

        log.info(this.getClass().getName() + ".onMessage Start!");

        log.info("receive message : " + msg);
        log.info("receive session : " + session);
        log.info("receive roomName : " + roomName);
        log.info("receive userName : " + userName);

        // 실제 저장된 채팅룸 해시값 가져오기
        String roomNameHash = roomInfo.get(roomName);

        log.info("roomNameHash : " + roomNameHash);

        // 웹소켓에 접속된 모든 사용자 검색
        clients.stream().forEach(s -> {

            // 내가 접속한 채팅방에 있는 세션만 메시지 보내기
            if (roomNameHash.equals(s.getUserProperties().get("roomName"))) {
                try {
                    // 발송시간 추가를 위해 JSON 문자열을 DTO로 변환
                    ChatDTO cDTO = new ObjectMapper().readValue(msg, ChatDTO.class);

                    // 메시지 발송시간 서버 시간으로 설정하여 추가하기
                    cDTO.setDate(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"));

                    // ChatDTO을 JSON으로 다시 변환하기
                    s.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(cDTO));

                } catch (IOException e) {
                    log.info("Error : " + e);
                }

            }
        });

        log.info(this.getClass().getName() + ".onMessage End!");
    }

    @OnError
    public void onError(Throwable e) throws Exception {
        log.info(this.getClass().getName() + ".onError Start!");

        log.info(this.getClass().getName() + ".onError : " + e);

        log.info(this.getClass().getName() + ".onError End!");

    }

    @OnClose
    public void onClose(Session session, @PathParam("roomName") String roomName,
                        @PathParam("userName") String userName) {

        log.info(this.getClass().getName() + ".onClose Start!");
        log.info("receive roomName : " + roomName);
        log.info("receive userName : " + userName);

        // 실제 저장된 채팅룸 해시값 가져오기
        String roomNameHash = roomInfo.get(roomName);
        log.info("roomNameHash : " + roomNameHash);

        clients.remove(session); // 접속되어있는 세션 삭제

        // 웹소켓에 접속된 모든 사용자 검색
        clients.stream().forEach(s -> {

            log.info("s.room : " + s.getUserProperties().get("roomName"));
            log.info("s.userName : " + s.getUserProperties().get("userName"));

            // 내가 접속한 채팅방에 있는 세션만 메시지 보내기
            if (roomNameHash.equals(s.getUserProperties().get("roomName"))) {

                try {

                    //{"name":"이협건","msg":"ㅇㅎ","date":"2022. 7. 25. 오전 9:30:57"}
                    ChatDTO cDTO = new ChatDTO();
                    cDTO.setName("관리자");
                    cDTO.setMsg(userName + "님이 " + roomName + " 채팅방에 퇴장하셨습니다.");
                    cDTO.setDate(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"));

                    String json = new ObjectMapper().writeValueAsString(cDTO);
                    log.info("json : " + json);

                    s.getBasicRemote().sendText(json);

                    cDTO = null;

                } catch (IOException e) {
                    log.info("Error : " + e);
                }
            }
        });
        log.info(this.getClass().getName() + ".onClose End!");
    }
}



