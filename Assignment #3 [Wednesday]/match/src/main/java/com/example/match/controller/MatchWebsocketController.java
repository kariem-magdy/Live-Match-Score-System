// src/main/java/com/example/match/controller/MatchWebsocketController.java
package com.example.match.controller;

import com.example.match.model.Message;
import com.example.match.service.MatchService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MatchWebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchService matchService;

    public MatchWebsocketController(SimpMessagingTemplate messagingTemplate,
                                    MatchService matchService) {
        this.messagingTemplate = messagingTemplate;
        this.matchService = matchService;
    }

    /**
     * Handle updating the score of a specific match and notify all subscribers
     */
    @MessageMapping("/match/update/{matchId}")
    public void updateScore(@DestinationVariable String matchId, Message message) {
        String team = message.matchScore(); // Used to represent the scoring team
        System.out.println("Team" + team + "scored");
        matchService.incrementScore(matchId, team);
        String score = matchService.getMatchDetails(matchId);
        Message updated = new Message(score, false);
        messagingTemplate.convertAndSend("/topic/match/" + matchId, updated);
    }

    /**
     * Handle end match messages and notify all subscribers
     */
    @MessageMapping("/match/end/{matchId}")
    public void endMatch(@DestinationVariable String matchId) {
        matchService.endMatch(matchId);
        System.out.println("Match" + matchId + "ended");
        String score = matchService.getMatchDetails(matchId);
        Message ended = new Message(score, true);
        messagingTemplate.convertAndSend("/topic/match/" + matchId, ended);
    }
}
