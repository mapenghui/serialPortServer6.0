package com.mph.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mph.db.SQLHelper;

public class SQLHelper {

	//������Ҫ�����
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection ct = null;
	
	String driverName = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/jspstudy?user=root&password=tiger";
	
	public SQLHelper() {
		//��������
		try {
			//1.��������
			Class.forName(driverName);
			//2.�õ�����
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
			//��Sql������ֵ
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
	
	//�ر���Դ�ķ���
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
