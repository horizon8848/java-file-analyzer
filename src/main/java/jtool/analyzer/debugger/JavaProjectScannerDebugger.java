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
package jtool.analyzer.debugger;

import java.io.File;
import java.util.stream.Collectors;

import jtool.analyzer.core.Analyzer;
import jtool.analyzer.core.JavaMethodAnalyzer;
import jtool.analyzer.core.JavaProjectScanner;
import jtool.analyzer.core.Scanner;
import jtool.analyzer.entity.FileDirectory;
import jtool.analyzer.entity.JavaClassMethodInfo;
import jtool.analyzer.entity.JavaFileInfo;

/**
 * 调试java项目扫描
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:00:48 AM
 */
public class JavaProjectScannerDebugger {

	/**
	 * 扫描项目
	 * @param projectPath
	 */
	public void printProjectDirectory(String projectPath) {
		Scanner scanner = new JavaProjectScanner();
		FileDirectory directorys = scanner.scan(projectPath).getFileDirectory();
		if(directorys != null) {
			this.printDeepDirectory(directorys);
		}
	}
	
	/**
	 * 打印目录结构
	 * @param directory
	 */
	private void printDeepDirectory(FileDirectory directory) {
		if(!directory.getChildren().isEmpty()) {
			for(Object child : directory.getChildren()) {
				System.out.println(child);
				if(child instanceof FileDirectory f) {
					this.printDeepDirectory(f);					
				} else if(child instanceof JavaFileInfo j) {
					System.out.println("文件信息======================start");
					System.out.println("文件名：" + j.getName());
					System.out.println("类名：" + j.getClassFullName());
					for(JavaClassMethodInfo info : j.getMethodInfos()) {
						System.out.println(String.format("方法：%s %s(%s)", info.getReturnType(), info.getMethodName(), info.getParamList()));			
					}
					System.out.println("文件信息======================end");
				} else {
					System.out.println("未识别文件类型");
				}
			}
		}
	}
	
	/**
	 * 测试字节码文件获取方法
	 * @param classFile 字节码文件
	 */
	public void testJavaMethodAnalyzer(File classFile) {
		Analyzer<JavaFileInfo> analyzer = new JavaMethodAnalyzer(classFile);
		analyzer.execute();
		JavaFileInfo result = analyzer.getAnalysisResult();
		System.out.println("文件名：" + result.getName());
		System.out.println("类名：" + result.getClassFullName());
		for(JavaClassMethodInfo info : result.getMethodInfos()) {
			String paramList = info.getParamList().stream().collect(Collectors.joining(","));
			System.out.println(String.format("方法：%s %s(%s)", info.getReturnType(), info.getMethodName(), paramList));			
		}
	}
}
