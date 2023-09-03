/**
 * Copyright cjt(tris73768u@163.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jtool.analyzer.calculate;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 有界计算管道
 * @param <I> 输入对象
 * @param <O> 输出对象
 * @author cjt
 * @date   Aug 24, 2023 2:06:38 PM
 */
public class CalculatePipeline<I, O> implements Pipeline<I, O>, Runnable {

	/**
	 * 管道队列
	 */
	private Queue<HandleWrapper> queue;
	
	/**
	 * 数据处理传输载体
	 */
	private Carrier carrier = new Carrier();
	
	/**
	 * 状态，0：待处理、1：处理中、2：处理完成待取数、3：可释放
	 */
	private int status = 0;
	
	public CalculatePipeline(int capacity) {
		this.queue = new ArrayBlockingQueue<>(capacity);
	}

	@Override
	public Pipeline<I, O> input(I i) {
		carrier.setTransportBody(i);
		return this;
	}

	@Override
	public Pipeline<I, O> work() {
		this.status = 1;
		try {
			this.executeHandler();			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			this.status = 2;
		}
		return this;
	}
	
	/**
	 * 递归处理handler
	 */
	private void executeHandler() {
		HandleWrapper handler = queue.poll();
		if(handler != null) {
			handler.handle(carrier);
			executeHandler();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public O output() {
		synchronized(this) {
			try {
				return (O) carrier.getTransportBody();	
			} catch(ClassCastException e) {
				System.out.println("转换异常，请检查最后一个handler是否正确");
				return null;
			}
		}
	}

	/**
	 * 添加handler
	 * @param handler
	 * @return
	 */
	public boolean addHandler(HandleWrapper handler) {
		return this.queue.offer(handler);
	}
	
	public void setUnused() {
		synchronized(this) {
			this.status = 3;	
		}
	}
	
	public boolean isUnused() {
		synchronized(this) {
			return this.status == 3;	
		}
	}
	
	public boolean waitProcess() {
		synchronized(this) {
			return this.status == 0;	
		}
	}
	
	public boolean canGetResult() {
		synchronized(this) {
			return this.status == 2;	
		}
	}

	@Override
	public void run() {
		this.work();
	}
}
