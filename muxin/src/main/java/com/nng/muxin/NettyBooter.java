package com.nng.muxin;

import com.nng.muxin.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * 监控ContextRefreshedEvent事件（容易初始化完成事件）
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 *
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。

		//root application context 没有parent，他就是老大.
		if (event.getApplicationContext().getParent() == null) {
			try {
				WSServer.getInstance().start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
