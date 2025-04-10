package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * セッション作成
 */

@Data
@AllArgsConstructor
public class Session_request {
	private String uuid;//セッション参加用
	private String password;//平文の合言葉（セッション作成、参加両方に必要）
}