package com.example.demo.entity;
/*
 * ドメイン
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session_entity {
	private String uuid;
	private String hash_password;
	private boolean connected;
}
