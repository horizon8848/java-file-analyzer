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
package jtool.analyzer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import jtool.analyzer.constant.Constant;
import jtool.analyzer.report.JavaProjectReport;

/**
 * 测试类
 * 
 * @author cjt
 * @date   Aug 29, 2023 11:05:00 AM
 */
public class DemoTest {
	
	private static final String PROJECT_PATH = "C:\\Users\\cjt\\eclipse-workspace\\java-file-analyzer\\target\\classes\\jtool";
	
	public static void main(String[] args) {
//		JavaProjectScannerDebugger debugger = new JavaProjectScannerDebugger();
//		debugger.testJavaMethodAnalyzer(new File("D://test//Constant.class"));
//		debugger.printProjectDirectory(PROJECT_PATH);
		
		JavaProjectReport report;
		if(Objects.nonNull(System.getProperty("dbUrl"))) {
			String url = System.getProperty("dbUrl");
			String user = System.getProperty("dbUser");
			String pwd = System.getProperty("dbPwd");
			String statisticsTable = System.getProperty("table");
			report = new JavaProjectReport(PROJECT_PATH, url, user, pwd, statisticsTable);
		} else {
			report = new JavaProjectReport(PROJECT_PATH);
			report.setQueryDb(true);
		}
		report.setRootName("java-file-analyzer");
		report.output();
		
	}
	
	public void thymeleafOutput() {
		TemplateEngine engine = new TemplateEngine();
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("report/");
        resolver.setSuffix(".html");
		engine.setTemplateResolver(resolver);
		
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(Constant.PATH + "/report.html"));){
			Context context = new Context();
			context.setVariable("keys", new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
			context.setVariable("values", new int[] {23, 24, 18, 25, 27, 28, 25});
			engine.process("project.html", context, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
