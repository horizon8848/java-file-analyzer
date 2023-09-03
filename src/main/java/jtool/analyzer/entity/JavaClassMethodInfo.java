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
package jtool.analyzer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类方法信息
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:01:14 AM
 */
public class JavaClassMethodInfo  {
	
	/**
	 * 方法名称
	 */
	private String methodName;
	
	/**
	 * 参数列表
	 */
	private List<String> paramList;
	
	/**
	 * 返回类型
	 */
	private String returnType;
	
	public JavaClassMethodInfo() {
		this.paramList = new ArrayList<>(4);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getParamList() {
		return paramList;
	}

	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
}
