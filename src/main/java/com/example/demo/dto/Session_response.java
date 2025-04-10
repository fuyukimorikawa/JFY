
package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * レスポンス
 */

@Data
@AllArgsConstructor
public class Session_response {
	private boolean success;
	private String message;
}
