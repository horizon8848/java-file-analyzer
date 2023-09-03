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
package jtool.analyzer.calculate.workgroup;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

import jtool.analyzer.calculate.AbstratHandler;
import jtool.analyzer.calculate.CalculatePipeline;

/**
 * 计算工作组
 * 
 * @author cjt
 * @date   Aug 28, 2023 8:59:09 AM
 */
public abstract class AbstractCalculateWorkerGroup<I, O> {
	
	private boolean prepareFinish = false;
	private boolean workGroupFinish = false;
	private boolean resultProcessFinish = false;
	
	private int workerNum = 5;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(workerNum);
	
	private StatisticsPipelineManager<I, O> manager;
	
	private Queue<O> resultQueue = new LinkedTransferQueue<>();
	
	public AbstractCalculateWorkerGroup(AbstratHandler... hadnler) {
		this.manager = new StatisticsPipelineManager<>(workerNum, hadnler);
	}
	
	public AbstractCalculateWorkerGroup(int workerNum, AbstratHandler... hadnler) {
		this.workerNum = workerNum;
		this.manager = new StatisticsPipelineManager<>(workerNum, hadnler);
	}
	
	/**
	 * 工作分配
	 * @param manager
	 */
	public abstract void assignment(StatisticsPipelineManager<I, O> manager);
	
	/**
	 * 结果数据处理
	 * @param data
	 */
	public abstract void resultProcess(O data);
	
	/**
	 * 启动执行
	 */
	public void start() {
		try {
			Thread t1 = this.runWorkerGroup();
			Thread t2 = this.runResultHandle();
			this.assignment(manager);
			prepareFinish = true;
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否停止
	 * @return
	 */
	public boolean isStop() {
		return this.resultProcessFinish;
	}

	/**
	 * 启动管道任务
	 * @throws InterruptedException 
	 */
	private Thread runWorkerGroup() throws InterruptedException {
		Thread thread = new Thread(() -> {
			while(true) {
				if(manager.getPipelines().length < 1) {
					continue;
				}
				boolean hasWorking = false;
				for(CalculatePipeline<I, O> pipeline : manager.getPipelines()) {
					if(pipeline == null) {
						continue;
					}
					if(pipeline.waitProcess()) {
						executorService.submit(pipeline);
						hasWorking = true;
					} else if(!pipeline.isUnused()) {
						// 结果取值
						if(pipeline.canGetResult()) {
							if(pipeline.output() != null) {
								resultQueue.offer(pipeline.output());
							}
							pipeline.setUnused();
						}
						hasWorking = true;
					}
				}
				
				if(prepareFinish && !hasWorking) {
					workGroupFinish = true;
					break;
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return thread;
	}
	
	/**
	 * 结果汇总处理
	 * @throws InterruptedException 
	 */
	private Thread runResultHandle() throws InterruptedException {
		Thread thread = new Thread(() -> {
			int emptyCount = 0;
			while(true) {
				if(resultQueue.size() > 0) {
					emptyCount = 0;
					while(true) {
						O data = resultQueue.poll();
						if(data == null) {
							break;
						}
						this.resultProcess(data);
					}
				} else {
					emptyCount++;
				}
				
				if(workGroupFinish && emptyCount > 2) {
					this.resultProcessFinish = true;
					// 释放线程池
					executorService.shutdown();
					break;					
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return thread;
	}
}
