package jmysconnection;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Configuration {
	/**
	 * @author Jerry_P
	 * 
	 *         <p>
	 *         JMysConnection Version V0.1.1
	 *         </p>
	 * 
	 *         <p>
	 *         ʹ��ǰ��������������һ�����ݿ���Ϣ
	 *         </p>
	 *         <p>
	 *         �뽫������������mysql-connector-java-5.1.6-bin.jar���롣
	 *         </p>
	 *         <p>
	 *         Before using, place configurate following terms
	 *         </p>
	 *         <p>
	 *         Place input the connection driver's package:mysql-connector-java-5.1.6-bin.jar
	 *         </p>
	 */
	public static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		String addr = "localhost"; //�����������ݿ��ַ Input your database address here
		String databaseName = "name"; // �����������ݿ��� Input your database name here
		String port = "3306"; // ��������˿� Input your database port here
		String username = "root"; // �����������ݿ��û��� Your Mysql username
		String password = "password"; // �����������ݿ����� Your Mysql password
		String url = "jdbc:mysql://" + addr + ":" + port + "/" + databaseName;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
