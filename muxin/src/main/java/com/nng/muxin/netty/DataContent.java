package com.nng.muxin.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataContent implements Serializable {

	private static final long serialVersionUID = 3011709550111031018L;

	private Integer action;		// 动作类型
	private NettyChatMsg nettyChatMsg;	// 用户的聊天内容entity
	private String extend;		// 扩展字段
	

}
