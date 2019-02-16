package com.nng.muxin.utils;

import java.util.UUID;

/**
 * 返回uuid
 */
public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
