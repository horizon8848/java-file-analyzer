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
package jtool.analyzer.report.statistics.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jtool.analyzer.calculate.AbstratHandler;
import jtool.analyzer.report.statistics.dto.StatisticsSourceDataDTO;

/**
 * 方法统计数据处理
 * 
 * @author cjt
 * @date   Aug 25, 2023 1:48:04 PM
 */
public final class MethodStatisticsHandler extends AbstratHandler {

	@Override
	public Object execute(Object input) {
		// 示例
		List<StatisticsSourceDataDTO> dataList = (List<StatisticsSourceDataDTO>) input;
		Map<String, Integer> statisticsMap = new HashMap<>();
		for(StatisticsSourceDataDTO data : dataList) {
			/**
			 * 数据处理
			 * StatisticsSourceDataDTO [className=cn.com.cjt.TestController, methodName=test, paramList=com.alibaba.fastjson.JSONObject;java.lang.String]
			 * ->
			 * cn.com.cjt#TestController.class#test(com.alibaba.fastjson.JSONObject,java.lang.String)
			 */
			StringBuilder pathBuilder = new StringBuilder();
			String[] paths = data.getClassName().split("\\.");
			String methodKey = null;
			for(int i = 0; i < paths.length; i++) {
				if(i == paths.length - 1) {
					// 参数列表分号转逗号
					String paramList = "";
					if(Objects.nonNull(data.getParamList())) {
						paramList = data.getParamList().replaceAll("\\;", ",");						
					}
					methodKey = String.format("%s#%s.class#%s(%s)", pathBuilder.toString(), paths[i], data.getMethodName(), paramList);
				} else {
					if(i > 0) {
						pathBuilder.append(".");
					}
					pathBuilder.append(paths[i]);					
				}
			}
			if(Objects.nonNull(methodKey)) {
				int count = statisticsMap.containsKey(methodKey) ? (statisticsMap.get(methodKey) + 1) : 1;
				statisticsMap.put(methodKey, count);
			}
		}
		return statisticsMap;
	}

}
