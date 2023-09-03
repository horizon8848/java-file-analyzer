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
package jtool.analyzer.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import jtool.analyzer.constant.Constant;
import jtool.analyzer.constant.DirectoryTypeEnum;
import jtool.analyzer.core.JavaProjectScanner;
import jtool.analyzer.core.Scanner;
import jtool.analyzer.entity.FileDirectory;
import jtool.analyzer.entity.JavaClassMethodInfo;
import jtool.analyzer.entity.JavaFileInfo;
import jtool.analyzer.report.entity.ChildNodeBaseEntity;
import jtool.analyzer.report.entity.ClassMethodNodeEntity;
import jtool.analyzer.report.entity.ProjectRootEntity;
import jtool.analyzer.report.statistics.JavaMethodStatistics;
import jtool.analyzer.report.statistics.MysqlConnectionPool;

/**
 * java项目报告
 * 
 * @author cjt
 * @date   Aug 24, 2023 8:58:29 AM
 */
public class JavaProjectReport {

	private String rootName = "root";
	
	private String scanPath;
	private String reportOutputPath = Constant.PATH;
	
	/**
	 * 是否查询数据库,默认不查询
	 */
	private boolean queryDb = false;
	private String url = "jdbc:mysql://localhost:3306/test";
	private String user = "root";
	private String password = "123456";
	private String statisticsTable = "method_statistics";
	
	/**
	 * init
	 * @param scanPath 目录路径
	 */
	public JavaProjectReport(String scanPath) {
		this.scanPath = scanPath;
	}

	/**
	 * init（查询db）
	 * @param scanPath 目录路径
	 * @param url		数据库地址
	 * @param user		数据库账号
	 * @param password	数据库密码
	 */
	public JavaProjectReport(String scanPath, String url, String user, String password, String statisticsTable) {
		super();
		this.scanPath = scanPath;
		this.url = url;
		this.user = user;
		this.password = password;
		this.statisticsTable = statisticsTable;
		this.queryDb = true;
	}

	/**
	 * 设置根节点名称
	 * @param rootName
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	
	/**
	 * 设置报告输出路径
	 * @param reportOutputPath
	 */
	public void setReportOutputPath(String reportOutputPath) {
		this.reportOutputPath = reportOutputPath;
	}
	
	/**
	 * 设置是否查询数据库
	 * @param queryDb
	 */
	public void setQueryDb(boolean queryDb) {
		this.queryDb = queryDb;
	}

	public void output() {
		Scanner scanner = new JavaProjectScanner();
		FileDirectory directorys = scanner.scan(scanPath).getFileDirectory();
		if(directorys == null) {
			System.out.println("未找到目录");
		}
		System.out.println("processing...");
		ProjectRootEntity root = new ProjectRootEntity();
		root.setRootName(rootName);
		List<ChildNodeBaseEntity> children = this.processDirectory(directorys);
		if(queryDb) {
			// 查数据库统计数据
			JavaMethodStatistics statistics = new JavaMethodStatistics(children, new MysqlConnectionPool(url, user, password), statisticsTable);
			root.setChildren(statistics.execute().getNodes());			
		} else {
			root.setChildren(children);
		}
		
		// 输出模板
		this.createReport(root);
		System.out.println("finished");
	}
	
	/**
	 * 生成报告
	 * @param root
	 */
	private void createReport(ProjectRootEntity root) {
		TemplateEngine engine = new TemplateEngine();
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("report/");
        resolver.setSuffix(".html");
		engine.setTemplateResolver(resolver);
		
		Path outputPath = Paths.get(reportOutputPath, "/report.html");
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath);){
			Context context = new Context();
			context.setVariable("projectInfo", root);
			engine.process("project_struct.html", context, writer);
			System.out.println("output:" + outputPath.toAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印目录结构
	 * @param directory
	 */
	private List<ChildNodeBaseEntity> processDirectory(FileDirectory directory) {
		List<ChildNodeBaseEntity> children = new ArrayList<>();
		if(!directory.getChildren().isEmpty()) {
			for(Object child : directory.getChildren()) {
				if(child instanceof FileDirectory f) {
					ChildNodeBaseEntity base = new ChildNodeBaseEntity();
					base.setName(f.getName());
					base.setType(f.getType());
					base.setPackagePath(Stream.of(f.getPath()).collect(Collectors.joining(Constant.DOT)));
					if(DirectoryTypeEnum.DIRECTORY.getType() == f.getType()) {
						base.setChildren(this.processDirectory(f));
					}
					children.add(base);
				} else if(child instanceof JavaFileInfo j) {
					ChildNodeBaseEntity entity = new ChildNodeBaseEntity();
					entity.setName(j.getName());
					entity.setType(j.getType());
					entity.setPackagePath(Stream.of(j.getPath()).filter(tmp -> !tmp.endsWith(".class")).collect(Collectors.joining(Constant.DOT)));
					// 遍历方法
					for(JavaClassMethodInfo methodInfo : j.getMethodInfos()) {
						String paramList = methodInfo.getParamList().stream().collect(Collectors.joining(","));
						String uniqueMethod = String.format("%s(%s)", methodInfo.getMethodName(), paramList);
						String methodAllName = String.format("%s %s", methodInfo.getReturnType(), uniqueMethod);
						ClassMethodNodeEntity methodEntity = new ClassMethodNodeEntity();
						methodEntity.setName(methodAllName);
						methodEntity.setUniqueMethod(uniqueMethod);
						methodEntity.setType(DirectoryTypeEnum.METHOD.getType());
						methodEntity.setClassName(j.getName());
						methodEntity.setPackagePath(Stream.of(j.getPath()).filter(tmp -> !tmp.endsWith(".class")).collect(Collectors.joining(Constant.DOT)));
						entity.addChild(methodEntity);
					}
						
					children.add(entity);
				} else {
					System.out.println("未识别文件类型");
				}
			}
		}
		
		return children;
	}
}
