package com.mph.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mph.db.SQLHelper;

public class UserModel {

	public String checkUser(String uId, String pw) {
		
		String passwd = null;
		SQLHelper sp = null;
		
		try {

			String sql = "select passwd from usemp where ename=?";
			String paras[] = {uId, pw};
			
			for(int i=0; i<paras.length; i++) {
				System.out.println(paras[i]);
			}
			
			sp = new SQLHelper();
			ResultSet rs = sp.query(sql, paras);//java 是引用传递，所以close可以关闭rs
			
			if(rs.next()) {
				
				passwd = rs.getString(1);
				System.out.println("passwd:" + passwd );
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			sp.close();
		}
		
		return passwd;
		
	}
}
