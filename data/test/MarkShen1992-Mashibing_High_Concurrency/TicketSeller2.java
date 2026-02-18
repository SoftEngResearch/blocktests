package com.mark.concurrent24;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import java.util.Arrays;import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * 有n张火车票，每张票都有一编号
 * 同时有10个窗口在对外售票
 * 写一个模拟程序
 * 
 * 分析下面程序可能产生那些问题?
 * 重复销售， 超量销售
 * @author MarkShen
 *
 */
public class TicketSeller2 {
	static Vector<String> tickets = new Vector<String>();  // 本身就是同步容器
	
	static {
		for (int i=0; i<10000; i++) tickets.add("票编号:" + i);
	}
	
	public static void main(String[] args) {
		for (int i=0; i<10; i++) {
			new Thread(() -> {
				// BLOCKTEST EVAL: https://github.com/MarkShen1992/Mashibing_High_Concurrency/blob/f408144caf72de0391f6e271eec6b92021ab1ca4/src/main/java/com/mark/concurrent24/TicketSeller2.java#L25-L35
                blocktest().given(tickets, new Vector<>(Arrays.asList("1", "2", "3")))
                .checkTrue(tickets.isEmpty());
				while(tickets.size() > 0) {  // 原子性
					// 中间部分不是原子性的
					/*try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
					System.out.println("销售了--" + tickets.remove(0));  // 原子性
				}
			}).start();
		}
	}
}
