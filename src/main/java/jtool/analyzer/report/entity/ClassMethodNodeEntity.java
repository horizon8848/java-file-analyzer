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
package jtool.analyzer.report.entity;

/**
 * 类方法子节点
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:01:44 AM
 */
public class ClassMethodNodeEntity extends ChildNodeBaseEntity {

	/**
	 * 归属类名
	 */
	private String className;
	
	/**
	 * 唯一方法名(不包含返回参数类型)
	 */
	private String uniqueMethod;
	
	/**
	 * 方法调用次数
	 */
	private int times;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUniqueMethod() {
		return uniqueMethod;
	}

	public void setUniqueMethod(String uniqueMethod) {
		this.uniqueMethod = uniqueMethod;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	
	/**
	 * 增加次数
	 * @param times
	 */
	public void addTimes(int times) {
		synchronized(this) {
			this.times += times;
		}
	}

	@Override
	public String toString() {
		return "ClassMethodNodeEntity [className=" + className + ", uniqueMethod=" + uniqueMethod + ", times=" + times
				+ ", toString()=" + super.toString() + "]";
	}

}
