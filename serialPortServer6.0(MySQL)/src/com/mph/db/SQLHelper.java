package com.mph.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mph.db.SQLHelper;

public class SQLHelper {

	//定义需要的组件
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection ct = null;
	
	String driverName = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/jspstudy?user=root&password=tiger";
	
	public SQLHelper() {
		//加载驱动
		try {
			//1.加载驱动
			Class.forName(driverName);
			//2.得到连接
			ct = DriverManager.getConnection(url);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql, String[] paras) {
		
		try {
			ps = ct.prepareStatement(sql);
			//对Sql参数赋值
			//for(int i=0; i<paras.length; i++) {
				ps.setString(1, paras[0]);
				System.out.println(sql);
			//}
			//System.out.println(sql);
			rs = ps.executeQuery();
			System.out.println( "SQL" + rs);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return rs;
	}
	
	//关闭资源的方法
	public void close() {
		
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(ct != null) ct.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
