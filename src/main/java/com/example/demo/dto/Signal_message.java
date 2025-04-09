package com.example.demo.dto;

import lombok.Data;

@Data
public class Signal_message {
	    private String type; // "offer", "answer", "candidate"
	    private String sender; // メッセージ送信者ID
	    private String target; // メッセージ受信者ID
	    private String sdp; // SDP情報
	    private String candidate; // ICE候補情報

	   
}
