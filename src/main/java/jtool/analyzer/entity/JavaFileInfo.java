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

import java.util.Arrays;
import java.util.List;

/**
 * java文件信息
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:01:22 AM
 */
public final class JavaFileInfo extends AbstractFileDirectory {

	/**
	 * analysis result
	 */
	private boolean anaylsisSuccess;
	
	/**
	 * analysis result message
	 */
	private String analysisMessage;
	
	/**
	 * full name
	 */
	private String classFullName;
	
	/**
	 * method list
	 */
	private List<JavaClassMethodInfo> methodInfos;

	public boolean isAnaylsisSuccess() {
		return anaylsisSuccess;
	}

	public void setAnaylsisSuccess(boolean anaylsisSuccess) {
		this.anaylsisSuccess = anaylsisSuccess;
	}

	public String getAnalysisMessage() {
		return analysisMessage;
	}

	public void setAnalysisMessage(String analysisMessage) {
		this.analysisMessage = analysisMessage;
	}

	public String getClassFullName() {
		return classFullName;
	}

	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

	public List<JavaClassMethodInfo> getMethodInfos() {
		return methodInfos;
	}

	public void setMethodInfos(List<JavaClassMethodInfo> methodInfos) {
		this.methodInfos = methodInfos;
	}
	
	@Override
	public String toString() {
		return "JavaFileInfo [type=" + super.getType() + ", name=" + super.getName() + ", path="
				+ Arrays.toString(super.getPath()) + "]";
	}
}
