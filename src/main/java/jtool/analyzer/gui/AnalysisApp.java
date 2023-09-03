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
package jtool.analyzer.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * 分析程序界面
 * 
 * @author cjt
 * @date Sep 1, 2023 2:50:37 PM
 */
public class AnalysisApp extends Application {

	private static final int CHOOSE_FIELD_WIDTH = 50;
	private static final int TEXT_FIELD_WIDTH = 55;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Scene scene = new Scene(grid, 700, 350);
		primaryStage.setTitle("项目分析程序");
		primaryStage.setScene(scene);

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("选择路径");

		Text sceneTitle = new Text("项目路径设置");
		sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(sceneTitle, 0, 0, 2, 1);

		TextField projectPathTextField = new TextField();
		grid.add(new Label("*扫描路径:"), 0, 1);
		grid.add(projectPathTextField, 1, 1, CHOOSE_FIELD_WIDTH, 1);
		grid.add(this.createChooseBtn(primaryStage, projectPathTextField, directoryChooser), 50, 1, 6, 1);

		TextField outputTextField = new TextField();
		grid.add(new Label("*报告输出路径:"), 0, 2);
		grid.add(outputTextField, 1, 2, CHOOSE_FIELD_WIDTH, 1);
		grid.add(this.createChooseBtn(primaryStage, outputTextField, directoryChooser), 50, 2, 6, 1);

		Text dbTitle = new Text("统计库设置");
		dbTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(dbTitle, 0, 3, 2, 1);
		grid.add(new Label("数据库地址:"), 0, 4);
		TextField dbTextField = new TextField();
		grid.add(dbTextField, 1, 4, TEXT_FIELD_WIDTH, 1);
		grid.add(new Label("账号:"), 0, 5);
		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 5, TEXT_FIELD_WIDTH, 1);
		grid.add(new Label("密码:"), 0, 6);
		PasswordField passwordField = new PasswordField();
		grid.add(passwordField, 1, 6, TEXT_FIELD_WIDTH, 1);
		grid.add(new Label("统计表:"), 0, 7);
		TextField tableTextField = new TextField();
		grid.add(tableTextField, 1, 7, TEXT_FIELD_WIDTH, 1);

		Button createReport = new Button("生成报告");
		createReport.setOnAction(new CreateReportEvent(projectPathTextField, outputTextField, dbTextField,
				userTextField, passwordField, tableTextField));
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(createReport);
		grid.add(hbBtn, 1, 8, TEXT_FIELD_WIDTH, 1);

		primaryStage.show();
	}

	private Button createChooseBtn(Stage primaryStage, TextField TextField, DirectoryChooser directoryChooser) {
		Button inputBtn = new Button("选择");
		inputBtn.setOnAction(new ChooseDirectoryEvent(primaryStage, TextField, directoryChooser));
		HBox inputHBox = new HBox(10);
		inputHBox.setAlignment(Pos.BOTTOM_RIGHT);
		inputHBox.getChildren().add(inputBtn);
		return inputBtn;
	}

	private Border border() {
		BorderStroke borderStroke = new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null);
		return new Border(borderStroke);
	}
}
