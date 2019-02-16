package com.nng.muxin.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMsg {

    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    /**
     * 消息是否签收状态
     * 1：签收
     * 0：未签收
     */
    private Integer signFlag;

    private Date createTime;


}