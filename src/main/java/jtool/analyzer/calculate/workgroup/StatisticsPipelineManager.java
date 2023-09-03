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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import jtool.analyzer.calculate.AbstratHandler;
import jtool.analyzer.calculate.CalculatePipeline;
import jtool.analyzer.calculate.HandleWrapper;

/**
 * 管道任务管理
 * @param <I> 输入参数
 * @param <O> 输出参数
 * @author cjt
 * @date   Aug 28, 2023 10:34:49 AM
 */
public class StatisticsPipelineManager<I, O> {

	/**
	 * 管道
	 */
	private CalculatePipeline<I, O>[] pipelines;
	
	private int appendIndex = 0;
	
	private List<AbstratHandler> handlers = Collections.emptyList();
	
	@SuppressWarnings("unchecked")
	public StatisticsPipelineManager(int capacity, AbstratHandler... hadnler) {
		this.pipelines = new CalculatePipeline[capacity];
		this.handlers = Stream.of(hadnler).toList();
	}
	
	public synchronized boolean createPipeline(I input) {
		if(appendIndex >= pipelines.length) {
			// 遍历管道，是否闲置管道
			for(int i = 0; i < pipelines.length; i++) {
				if(pipelines[i].isUnused()) {
					// 清空管道数据
					pipelines[i] = this.initPipeline(input);
					return true;
				}
			}
		} else {
			pipelines[appendIndex] = this.initPipeline(input);
			appendIndex++;
			return true;
		}
		
		return false;
	}
	
	private CalculatePipeline<I, O> initPipeline(I input) {
		CalculatePipeline<I, O> pipeline = new CalculatePipeline<>(handlers.size());
		for(AbstratHandler handler : handlers) {
			pipeline.addHandler(new HandleWrapper(handler));
		}
		pipeline.input(input);
		return pipeline;
	}

	public CalculatePipeline<I, O>[] getPipelines() {
		return pipelines;
	}
}
