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
package jtool.analyzer.collection;

/**
 * 简单字符串栈
 * Non-Thread-Safe
 * 
 * @author cjt
 * @date   Aug 24, 2023 8:58:58 AM
 */
public class SimpleStringStack {

	private String[] stack;
	
	private int index = 0;
	
	public SimpleStringStack(int capacity) {
		this.stack = new String[capacity];
	}

	/**
	 * 获取整个栈
	 * @return
	 */
	public String[] getStack() {
		return stack;
	}

	/**
	 * 获取当前入栈索引
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 入栈
	 * @param val
	 */
	public void push(String val) {
		if (index >= this.stack.length) {
			throw new ArrayIndexOutOfBoundsException(
					String.format("array max length %s, but current index is %s", this.stack.length, index));
		}
		this.stack[index] = val;
		index++;
	}
	
	/**
	 * 出栈
	 * @return
	 */
	public String pop() {
		if(index <= 0) {
			return null;
		}
		try {
			return this.stack[index - 1];
		} finally {
			// 清空当前值，index-1
			this.stack[index - 1] = null;
			index--;
		}
	}
	
	/**
	 * 清空
	 */
	public void clear() {
		this.stack = new String[this.stack.length];
		this.index = 0;
	}

}
