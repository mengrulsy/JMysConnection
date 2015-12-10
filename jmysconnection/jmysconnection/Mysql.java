package jmysconnection;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import jmysconnection.Configuration;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * 
 * @author Jerry_P
 * 
 *         <p>
 *         JMysConnection Version V0.1.1
 *         </p>
 * 
 *         <p>
 *         ʹ��ǰ���뽫������Ϣ��д��Configuration.java��ʹ��ʱ���뵼�����
 *         </p>
 *         <p>
 *         Before using is, place fill in some terms in Configuration.java, and place import this class if you use it
 *         </p>
 * 
 *         <p>
 *         Tips: Before calling each method, place call countSelected() and make sure the return is not 0 in order to make sure that the data existed, otherwise report errors.
 *         </p>
 *         <p>
 *         We did not defense the sql injection, place filter the character that may harm to the program or escaping the character.
 *         </p>
 *         <p>
 *         ע�⣺ ���ô��෽��ǰ�����ȵ���countSelected()ȷ�Ϸ���ֵ��0������֤���ݴ��ڣ�����ᱨ��
 *         </p>
 *         <p>
 *         ���ǲ�û�з���Sqlע�룬���ڵ���ǰȷ��Σ���ַ��Թ��˻�ת�塣
 *         </p>
 * 
 */

public class Mysql {
	@SuppressWarnings("rawtypes")
	/**
	 * <p>insert����ֻ��һ���Բ���һ������</p>
	 * <p>��table�������������ݱ���</p>
	 * <p>��title�������������ݱ����������ʽΪ����1,��2,��3�����ٸ����ӣ����������3�У��ֱ�С�Name������Value������Date������title����������Ҫд�ɡ�Name,Value,Date��</p>
	 * <p>��row����ʳ����Ҫ��������ݣ�������ArrayList��ÿ�β������ݵ�˳����������Ӧ�����������3�У��ֱ�С�Name������Value������Date������row[0]��ӦName��row[1]��ӦValue���Դ����ơ�</p>
	 * <p>Method insert just can insert one row of data when called.</p>
	 * @param table Parameter "table" is the name of table.
	 * @param title Parameter "title" is the name of each rank, and the format of this parameter is "rank1,rank2,rank3". For example, suppose we have three ranks, and each called "Name", "Value", "Date". So the parameter has to be write as "Name,Value,Date".
	 * @param row Parameter "row" is an ArrayList which contain the value you wanted to insert in. Each value in "row" is related to the parameter of "title". For example, suppose we have three ranks "Name", "Value", "Date", then row[0] relate to "Name", and row[1] relate to "Value", and the rest can be done in the same manner.
	 */
	public static void insert(String table, String title, ArrayList row) {
		Connection conn = Configuration.getConn();
		String title1 = "?";
		char d = ',';
		char[] chars = title.toCharArray();
		for (int a = 1; a < chars.length; a++) {
			if (d == chars[a]) {
				title1 = title1 + ",?";
			}
		}
		String sql = "insert into " + table + "(" + title + ") values(" + title1 + ")";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			for (int a = 1; a <= row.size(); a++) {
				if (row.get(a - 1) instanceof Integer) {
					pstmt.setInt(a, (Integer) row.get(a - 1));
				} else if (row.get(a - 1) instanceof String) {
					pstmt.setString(a, (String) row.get(a - 1));
				} else if (row.get(a - 1) instanceof Timestamp) {
					pstmt.setTimestamp(a, (Timestamp) row.get(a - 1));
				}
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ������update����ͨ��һ�����ݽ��ж�λ���޸ĵ������ݡ�
	 * </p>
	 * <p>
	 * ������table������Ҫ�޸����ݵı�����
	 * </p>
	 * <p>
	 * ������condition���������ݶ�λ����һ�����ӣ�����Ҫ�ҡ�Name����һ���е���ֵΪ��Jack������һ�����ݣ���ô��condition��������Ҫд�ɡ�Name='Jack'�����мǱ��ʽ�б���ʹ�õ����š������Ҫ����Ӷ����������and����or���߼��ж�ʱ����ʹ�á�&&����and�����ߡ�||����or�����зָ�������˵��Name='Jack'&&Password='123'����һ��������д����ʱ���ᱨ��ʹ��ǰ������ʹ��searchOneRowByIndex()ȷ�ϡ�
	 * </p>
	 * <p>
	 * Method "update" use for modify one data locating by specified data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table that you are going to modifying.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to modify. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example ��Name='Jack'&&Password='123'��. It would report errors when you transfer a wrong data, so place use searchOneRowByIndex() method for ensuring before update() method is called.
	 * @param modificationIndex
	 *            Parameter "modificationIndex" is the rank's name that you are going to modify
	 * @param modificationData
	 *            Parameter "modificationData" is the value of data that you are going to put that data instead of present data. For example, if I want to modify a data which rank's name is "name" and modify it as "abc", the parameter "modificationIndex" should be "name" and parameter "modificationData" should be "abc".
	 */

	public static void update(String table, String condition, String modificationIndex, String modificationData) {
		Connection conn = Configuration.getConn();
		String sql = "update " + table + " set " + modificationIndex + "='" + modificationData + "' where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ������delete����������������һ�����ݶ�λ�����ɾ��ĳ�����ݣ�
	 * </p>
	 * <p>
	 * ������table����ָɾ��Ŀ��ı���
	 * </p>
	 * <p>
	 * ������condition���������ݶ�λ����һ�����ӣ�����Ҫ�ҡ�Name����һ���е���ֵΪ��Jack������һ�����ݣ���ô��condition��������Ҫд�ɡ�Name='Jack'�����мǱ��ʽ�б���ʹ�õ����š������Ҫ����Ӷ����������and����or���߼��ж�ʱ����ʹ�á�&&����and�����ߡ�||����or�����зָ�������˵��Name='Jack'&&Password='123'����һ��������д����ʱ���ᱨ��ʹ��ǰ������ʹ��searchOneRowByIndex()ȷ�ϡ�
	 * </p>
	 * <p>
	 * Method "delete" use for delete one row of data locating by a data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to modify. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example ��Name='Jack'&&Password='123'��. It would report errors when you transfer a wrong data, so place use searchOneRowByIndex() method for ensuring before update() method is called.
	 */

	public static void delete(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "delete from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ����deleteAll()������ɾ��һ�����е��������ݣ�ɾ����ᱣ�����
	 * </p>
	 * <p>
	 * ������table������Ҫ��ɾ�����ݵı�����
	 * </p>
	 * <p>
	 * Method deleteAll() use for delete everything from specific table, and retain table when called.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table which you are going to empty.
	 */

	public static void deleteAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "delete from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ����getAll()�����ڻ�ȡһ���������ȫ�����ݣ�����������String�Ķ�ά���顣
	 * </p>
	 * <p>
	 * ������table��������Ҫ��ȡ���ݵı�����
	 * </p>
	 * <p>
	 * ������һ����ȫ��Ӧ���ݿ���������ʽ��
	 * </p>
	 * <p>
	 * Method getAll() use for obtain all data from specific table, the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above
	 */

	public static String[][] getAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countAll(table)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * ����searchOneRowByIndex()������һ��������ֵ������һ�����ݡ�
	 * </p>
	 * <p>
	 * ������table����ʾ��Ҫ��ѯ�ı�����
	 * </p>
	 * <p>
	 * ������condition���������ݶ�λ����һ�����ӣ�����Ҫ�ҡ�Name����һ���е���ֵΪ��Jack������һ�����ݣ���ô��condition��������Ҫд�ɡ�Name='Jack'�����мǱ��ʽ�б���ʹ�õ����š������Ҫ����Ӷ����������and����or���߼��ж�ʱ����ʹ�á�&&����and�����ߡ�||����or�����зָ�������˵��Name='Jack'&&Password='123'����
	 * </p>
	 * <p>
	 * Method searchOneRowByIndex() use for selecting a row of data located by one data.
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table that you going to search.
	 * @param condition
	 *            Parameter "condition" use for locating the position of data you are going to search. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example ��Name='Jack'&&Password='123'��.
	 * @return The return type is String array. The order of array is corresponding to the rank in table.
	 */

	public static String[] searchOneRowByIndex(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[] = new String[col];
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1] = rs.getString(j);
				}
				break;
			}

			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * ����searchFewRowByIndex()����������ȫ���������������ݣ�����������String��ά���顣
	 * </p>
	 * <p>
	 * ������table����ָ��Ҫ���������ı����
	 * </p>
	 * <p>
	 * ������index���͡�indexValue����ָ��������λ������һ�е�����������˵��һ���е������ǡ�Name��������Ҫ���С�Name����һ�е���ֵ���ڡ�abc�����������ݣ���ô������index����ֵӦ���ǡ�Name����������indexValue����ֵӦ���ǡ�abc����
	 * </p>
	 * <p>
	 * Method searchFewRowByIndex() use to search all data which match the condition, and the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of table you going to search.
	 * @param index
	 *            Parameters "index" and "indexValue" use for locating the data you going to search. Suppose there is a rank that names "Name", and I want all row of data that the rank "Name" value is "abc". The parameter "index" value should be "Name" and parameter "indexValue" should be "abc".
	 * @param indexValue
	 *            See parameter index.
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above.
	 */

	public static String[][] searchFewRowByIndex(String table, String index, String indexValue) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + index + "='" + indexValue + "'";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countSelected(table, index + "," + indexValue)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * ������countAll�������ڼ����������ݵ�����
	 * </p>
	 * <p>
	 * ������table����ָ����Ҫ���м����ı���
	 * </p>
	 * <p>
	 * ������countAll���ķ���ֵ�ǣ�Integer�����Σ�����ֵ���Ǳ��е�������
	 * </p>
	 * <p>
	 * Method "countAll" use for counting the number of row which existed in specific table
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of specific table that you are going to counting.
	 * @return return type is integer, and the value in return is the number of row in specific table.
	 */

	public static int countAll(String table) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int raw = 0;
			while (rs.next()) {
				raw = raw + 1;
			}
			return raw;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	/**
	 * <p>
	 * ������countSelected�������ڼ�����ƥ���������ݵ�����
	 * </p>
	 * <p>
	 * ������table����ָ����Ҫ���м����ı���
	 * </p>
	 * <p>
	 * ������condition��������Ҫͳ�����ݵ����������ʽ�������������
	 * </p>
	 * <p>
	 * ������countSelected���ķ���ֵ�ǣ�Integer�����Σ�����ֵ���Ƿ���������������
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the name of specific table that you are going to counting.
	 * @param condition
	 *            Parameter "condition" is the qualification of counting, and place using the format as shown in previous method.
	 * @return return type is integer, and the value in return is the number of row in specific table with specific condition.
	 */

	public static int countSelected(String table, String condition) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int raw = 0;
			while (rs.next()) {
				raw = raw + 1;
			}
			return raw;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Integer) null;
	}

	/**
	 * <p>
	 * ������custom���������Զ���Sql����������
	 * </p>
	 * <p>
	 * ������sql����������Ҫ�����sql����
	 * </p>
	 * <p>
	 * Method "custom" use for custom Sql commend entry.
	 * </p>
	 * 
	 * @param sql
	 *            Parameter "sql" is the Sql commend you are going to entry.
	 */

	public static void custom(String sql) {
		Connection conn = Configuration.getConn();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ������selectTime�������ڲ�ѯ���ݿ���Timestampʱ��
	 * </p>
	 * <p>
	 * ������table��������Ŀ��ı��
	 * </p>
	 * <p>
	 * ������condition�������ڶ�λ���ݵ���������һ�����ӣ�����Ҫ�ҡ�Name����һ���е���ֵΪ��Jack������һ�����ݣ���ô��condition��������Ҫд�ɡ�Name='Jack'�����мǱ��ʽ�б���ʹ�õ����š������Ҫ����Ӷ����������and����or���߼��ж�ʱ����ʹ�á�&&����and�����ߡ�||����or�����зָ�������˵��Name='Jack'&&Password='123'����
	 * </p>
	 * <p>
	 * ������positionOfTime����ȷ��Timestamp���ڵ��У�����ֵ��Ϊ����
	 * </p>
	 * 
	 * @param table
	 *            "table" is the table in database you going to select.
	 * @param condition
	 *            "condition" use for locating the position of data you are going to search. For example, if we have to find a row by rank "Name" equals "Jack", the parameter "condition" should be write as "Name='Jack'". Be sure to keep in mind, you must use single quotes rather than double quotas. If you wanted to adding logical judgment such as "and" and "or", place use "&&" represent "and" and "||"represent"or". For example ��Name='Jack'&&Password='123'��.
	 * @param positionOfTime
	 *            "positionOfTime" is the location of time in row.
	 * @return method return the Timestamp object from database.
	 */

	public static Timestamp selectTime(String table, String condition, int positionOfTime) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " where " + condition;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getTimestamp(positionOfTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * ����getAll()�����ڻ�ȡһ���������ȫ�����ݣ�����������String�Ķ�ά���顣
	 * </p>
	 * <p>
	 * ������table��������Ҫ��ȡ���ݵı�����
	 * </p>
	 * <p>
	 * ������sortIndex�����������ݣ�������һ�н�������
	 * </p>
	 * <p>
	 * ������method�������ھ������з�ʽ��ʹ��ʱ���봫��ֵ����asc��Ϊ�������У���desc��Ϊ��������
	 * </p>
	 * <p>
	 * ������һ����ȫ��Ӧ���ݿ���������ʽ��
	 * </p>
	 * <p>
	 * Method getAll() use for obtain all data from specific table, the return type is String array with 2 dimension.
	 * </p>
	 * <p>
	 * if (data[0] != null) {
	 * </p>
	 * <p>
	 * for (int i = 0; i < data[0].length; i++) {
	 * </p>
	 * <p>
	 * for (int j = 0; j < data.length; j++) {
	 * </p>
	 * <p>
	 * System.out.print(data[j][i]+"  ");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * System.out.println("");
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param table
	 *            Parameter "table" is the target's table name
	 * @param sortIndex
	 *            Parameter "sortIndex" is the rank that you are according to sort the whole data.
	 * @param method
	 *            Parameter "method" is the method you going to sort, and please pass only two value "asc" means ascending sequence and "desc" means descending sequence
	 * @return The return type is 2 dimensional String array. An output that correspond to the table in database as shown above
	 */

	public static String[][] getAll(String table, String sortIndex, String method) {
		Connection conn = Configuration.getConn();
		String sql = "select * from " + table + " order by " + sortIndex + " " + method;
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			java.sql.ResultSet rs = pstmt.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			String data[][] = new String[col][Mysql.countAll(table)];
			int i = 0;
			while (rs.next()) {
				for (int j = 1; j <= col; j++) {
					data[j - 1][i] = rs.getString(j);
				}
				i = i + 1;
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
