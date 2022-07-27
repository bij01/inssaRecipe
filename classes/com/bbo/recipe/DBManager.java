package com.bbo.recipe;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.sql.*;

public class DBManager {
	
	HashMap<String, Vector<String>> categoryMap = new HashMap<String, Vector<String>>(); //카테고리
	HashMap<String, String> matrialMap = new HashMap<String, String>(); // 레시피명, 재료리스트
	HashMap<String, Vector<String>> recipeMap = new HashMap<String, Vector<String>>(); // 레시피명, 레시피리스트
	Vector<String> cuisineList; // 레시피명 리스트
	Vector<String> matrialList; // 재료 리스트
	Vector<Vector<String>> recipeList; // 레시피 리스트
	Vector<String> recipes; //레시피 세부 리스트
	
	Connection con;
	Statement stmt;
	
	void init() {
		connectDB();
	}
	
	// DB 연결
	void connectDB() {
		String ip = "localhost";
		String usr = "team2";
		String pwd = "java";
		String url;
		try {
			url = "jdbc:oracle:thin:@"+ ip +":1521:JAVA";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, usr, pwd);
			con.setAutoCommit(false);
			stmt = con.createStatement();
		}catch(ClassNotFoundException cnfe) {
			System.out.println("드라이버 로딩 실패:" + cnfe);
		}catch(SQLException se) {
			System.out.println("연결 실패: "+ se);
		}
	}
	
	
	// 보민
	// DB에 레시피명, 재료리스트 넣기
	void insertMatrial() {
		
	}
	
	// 유현
	// DB에 레시피명, 레시피리스트 넣기
	void insertRecipe() {
		
	}
	
	// 유현
	// DB에서 카테고리 리스트 불러와서 categoryMap 배열에 담기
	void setCategory() {
		
	}
	
	// 보민
	// 레시피명, 재료리스트 세팅
	void setMatrial() {
		
	}
	
	// 레시피명, 레시피리스트 배열 세팅 
	void setRecipe() {
		
	}
	
	

	public static void main(String[] args) {
		DBManager dm = new DBManager();
		dm.init();
	}

}
