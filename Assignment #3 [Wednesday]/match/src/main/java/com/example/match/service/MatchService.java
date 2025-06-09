package com.example.match.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.match.model.Match;
import com.example.match.model.MatchStat;

@Service
public class MatchService {
    Map<String, Match> matchMap=new ConcurrentHashMap<>(); // sting = match id
    AtomicInteger newId=new AtomicInteger(1);
    public String createMatch(String teamA, String teamB) {
        String id = String.valueOf(newId.getAndIncrement());
        Match match = new Match(teamA, teamB);
        matchMap.put(id, match);
        return id;
    }

    public int incrementScore(String id,String team) {
        Match match = matchMap.get(id);
        if (match == null){
            return -1;
        }
        if(team.equalsIgnoreCase("A")){
            match.incrementScoreA();
        }
        else if(team.equalsIgnoreCase("B")){
            match.incrementScoreB();
        }
        else{
            return -1;
        }
        return -1;

    }
    public String getMatchDetails(String id) {
        Match match=matchMap.get(id);
        if(match==null){
            return "";
        }
        return match.getMatchScore();
    }
    public void endMatch(String id){
        matchMap.get(id).endMatch();
    }
    public int countActiveMatches() {
        // Count matches that are not ended using functional programming
        return (int) matchMap.values().stream()
                .filter(match -> match.getMatchStatus() != MatchStat.ENDED)
                .count();
    }
    public int getGoalCount(String team) {
        // Count goals for the given team using functional programming
        return matchMap.values().stream()
                .mapToInt(match -> match.getTeamScore(team))
                .sum();
    }
}
