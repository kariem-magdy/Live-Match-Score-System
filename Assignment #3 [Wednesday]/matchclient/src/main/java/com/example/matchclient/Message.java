package com.example.matchclient;

public record Message(String matchScore, boolean isEnded) { // matchScore = team -> A, a, B, b
}
