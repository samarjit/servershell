package com.ycs.fe.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

<<<<<<< HEAD
=======
import com.ycs.fe.util.Constants;
>>>>>>> 737c1c1e52b7f74ba221e2e0e600f2df07eae153
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.exception.BackendException;

/**
 * This class is used to connect to the database and execute queries.
 * 
 */
public class DBConnector {
	private Logger logger = Logger.getLogger(DBConnector.class);
	private static String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";
	private static String DBURL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static String DBUSER = "test";
	private static String DBPASSWORD = "test";
	private boolean isRuninServerContext;

	private final String SIMPLE_DATE_FORMAT = PrepstmtDTO.DATE_NS_FORMAT;//"yyyy-MM-dd HH:mm:ss.SSS";
	private final String DATE_TIME = PrepstmtDTO.DATE_TIME_MIN_FORMAT;

	public DBConnector() {
		Properties prop = new Properties();
		try {
			ClassLoader loader = this.getClass().getClassLoader();
<<<<<<< HEAD
			prop.load(loader.getResourceAsStream("path_config.properties"));
=======
			prop.load(loader.getResourceAsStream(Constants.PATH_CONFIG+".properties"));
>>>>>>> 737c1c1e52b7f74ba221e2e0e600f2df07eae153

			DBURL = prop.getProperty("DBURL");
			DBUSER = prop.getProperty("DBUSER");
			DBPASSWORD = prop.getProperty("DBPASSWORD");
			DRIVERNAME = prop.getProperty("DRIVERNAME");
		} catch (FileNotFoundException e) {
<<<<<<< HEAD
			logger.error("path_config.properties file not found", e);
		} catch (IOException e) {
			logger.error("Error in reading path_config.properties", e);
=======
			logger.error(Constants.PATH_CONFIG+".properties file not found", e);
		} catch (IOException e) {
			logger.error("Error in reading "+Constants.PATH_CONFIG+".properties", e);
>>>>>>> 737c1c1e52b7f74ba221e2e0e600f2df07eae153
		}
	}

	private void debug(int priority, String s) {
		if (priority > 0) {
			System.out.println("DBConnecctor:" + s);
		}
	}

	private void debug(int priority, String s, Throwable e) {
		if (priority > 0) {
			System.out.println("DBConnecctor:" + s);
		}
	}

	/**
	 * This function is used to return a connection with the database.
	 * 
	 * @throws BackendException
	 * 
	 */
	public Connection getConnection() throws BackendException {

		Connection conn = null;
		try {
			String driverName = DRIVERNAME;// "oracle.jdbc.driver.OracleDriver";
			// String url = "jdbc:mysql://localhost:3306/ams";
			String url = DBURL;// "jdbc:oracle:thin:@127.0.0.1:1521:XE";
			String userName = DBUSER;
			String password = DBPASSWORD;// "test";

			Context initContext = null;
			Context envContext = null;
			try {
				initContext = new InitialContext();
				envContext = (Context) initContext.lookup("java:/comp/env");
			} catch (NamingException e1) {
				logger.error("Exception occured in contructing InitialContext");
			}

			boolean fallaback = false;
			if (envContext != null) {
				try {
					DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");
					conn = ds.getConnection();

					if (conn == null || conn.isClosed()) {
						logger.error("Some thing wrong with connecting with database!");
						throw new SQLException("Some thing wrong with connecting with database using connection pool!");
					}
				debug(3, "Database connection Running in tomcat datasource pooled mode");
					isRuninServerContext = true;
				} catch (SQLException e) {
					fallaback = true;
					debug(3, "Datasource is not configured, Using fallback method of direct connection", e);
				} catch (NamingException e) {
					fallaback = true;
					debug(3, "Datasource is not configured, Using fallback method of direct connection", e);
				}
			}

			if (envContext == null || fallaback) {
				// /Running in standalone mode
		 debug(3, "Database connection Running in standalone mode# "+DBURL);
				// conn = DriverManager.getConnection (url, userName, password);
				Class.forName(driverName);
				conn = DriverManager.getConnection(url, userName, password);
				if (conn == null) {
					logger.error("Some thing wrong with connecting with database!");
					throw new SQLException("DBConnection is null");
				}
			}
			debug(0, "Database connection established");
			// CachedRowSet crs;

		} catch (ClassNotFoundException e) {
			logger.error("error.driverclassnameNotfound", e);
			throw new BackendException("error.DBconnectionFail", e);
		} catch (SQLException e) {
			logger.error("error.getdbConnection", e);
			throw new BackendException("error.DBconnectionFail", e);
		}

		return conn;
	}

	/**
	 * This functions executes a query
	 * 
	 * @param qry
	 * @return result
	 * @throws BackendException
	 * @throws SQLException
	 */
	public CachedRowSet executeQuery(String qry) throws BackendException {
		CachedRowSet crs;
		Connection conn = null;
		try {

			conn = getConnection();
			Statement stmt = conn.createStatement();
			debug(0, qry);
			ResultSet rs = stmt.executeQuery(qry);

			crs = new OracleCachedRowSet();
			crs.populate(rs);
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			logger.error("error.sqlstatement", e);
			throw new BackendException("error.sqlstatement", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
					debug(0, "Database connection terminated");
				} catch (SQLException e) {
					logger.error("error.closeDBconnection", e);
					// throw new BackendException("error.closeDBconnection", e);
				}
			}
		}

		return crs;
	}

	/**
	 * This function is used to execute update query.
	 * 
	 * @param qry
	 * @return
	 * @throws SQLException
	 * @throws BackendException
	 */
	public int executeUpdate(String qry) throws BackendException {
		Connection conn = null;
		int retval = 0;
		try {

			conn = getConnection();
			Statement stmt = conn.createStatement();
			retval = stmt.executeUpdate(qry);
		} catch (SQLException e) {
			logger.error("error.sqlstatement", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Rollback Exception", e);
			}
			throw new BackendException("error.sqlstatement", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					// log ("Database connection terminated");
				} catch (SQLException e) {
					logger.error("error.closeDBconnection", e);
					// throw new BackendException("error.closeDBconnection", e);
				}
			}
		}
		return retval;
	}

	/**
	 * Executes prepared statements
	 * 
	 * @param qry
	 * @param arPrepstmt
	 * @return result
	 * @throws BackendException
	 * @throws SQLException
	 */
	public CachedRowSet executePreparedQuery(String qry, PrepstmtDTOArray arPrepstmt) throws BackendException {
		CachedRowSet crs = null;
		Connection conn = null;
		try {

			conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(qry);
			Iterator itr = arPrepstmt.getArdto().iterator();
			int count = 1;
			while (itr.hasNext()) {
				PrepstmtDTO pd = (PrepstmtDTO) itr.next();
				if (pd.getType() == PrepstmtDTO.DataType.TIMESTAMP) {
					Timestamp newDate = new Timestamp(new SimpleDateFormat(SIMPLE_DATE_FORMAT).parse(pd.getData()).getTime());
					stmt.setTimestamp(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATE_NS) {
					Date newDate = new Date((new SimpleDateFormat(SIMPLE_DATE_FORMAT)).parse(pd.getData()).getTime());
					stmt.setDate(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATE_TIME_MIN_FORMAT) {
					Date newDate = new Date((new SimpleDateFormat(PrepstmtDTO.DATE_TIME_MIN_FORMAT)).parse(pd.getData()).getTime());
					stmt.setDate(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATEDDMMYYYY) {
					Date newDate = new Date((new SimpleDateFormat(PrepstmtDTO.DATEDDMMYYYY_FORMAT)).parse(pd.getData()).getTime());
					stmt.setDate(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DOUBLE) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0.0D";
					stmt.setDouble(count, Double.parseDouble(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.FLOAT) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0.0f";
					stmt.setFloat(count, Float.parseFloat(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.INT) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0";
					stmt.setInt(count, Integer.parseInt(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.STRING) {
					stmt.setString(count, pd.getData());
				} else if (pd.getType() == PrepstmtDTO.DataType.LONG) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0";
					stmt.setLong(count, Long.parseLong(in));
				}
				count++;
			}
			debug(0, qry);
			ResultSet rs = stmt.executeQuery();

			crs = new OracleCachedRowSet();
			crs.populate(rs);
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			logger.error("error.sqlstatement", e);
			throw new BackendException("error.sqlstatement", e);
		} catch (ParseException e) {
			logger.error("error.datatypeParsing", e);
			throw new BackendException("error.datatypeParsing", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
					debug(0, "Database connection terminated");
				} catch (SQLException e) {
					logger.error("error.closeDBconnection", e);
					// throw new BackendException("error.closeDBconnection", e);
				}
			}
		}

		return crs;
	}

	/**
	 * Executes prepared update statements.
	 * 
	 * @param qry
	 * @param arPrepstmt
	 * @return result
	 * @throws BackendException
	 * @throws SQLException
	 */
	public int executePreparedUpdate(String qry, PrepstmtDTOArray arPrepstmt) throws BackendException {
		Connection conn = null;
		int retval = 0;
		try {

			conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(qry);
			Iterator itr = arPrepstmt.getArdto().iterator();
			int count = 1;
			while (itr.hasNext()) {
				PrepstmtDTO pd = (PrepstmtDTO) itr.next();

				if (pd.getType() == PrepstmtDTO.DataType.TIMESTAMP) {
					Timestamp newDate = new Timestamp(new SimpleDateFormat(SIMPLE_DATE_FORMAT).parse(pd.getData()).getTime());
					stmt.setTimestamp(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATE_NS) {
					Date newDate = new Date((new SimpleDateFormat(SIMPLE_DATE_FORMAT)).parse(pd.getData()).getTime());
					stmt.setDate(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATEDDMMYYYY) {
					Date newDate = new Date((new SimpleDateFormat(PrepstmtDTO.DATEDDMMYYYY_FORMAT)).parse(pd.getData()).getTime());
					stmt.setDate(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DATE_TIME_MIN_FORMAT) {
					Timestamp newDate = new Timestamp((new SimpleDateFormat(PrepstmtDTO.DATE_TIME_MIN_FORMAT)).parse(pd.getData()).getTime());
					stmt.setTimestamp(count, newDate);
				} else if (pd.getType() == PrepstmtDTO.DataType.DOUBLE) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0.0D";
					stmt.setDouble(count, Double.parseDouble(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.FLOAT) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0.0f";
					stmt.setFloat(count, Float.parseFloat(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.INT) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0";
					stmt.setInt(count, Integer.parseInt(in));
				} else if (pd.getType() == PrepstmtDTO.DataType.STRING) {
					stmt.setString(count, pd.getData());
				} else if (pd.getType() == PrepstmtDTO.DataType.LONG) {
					String in = pd.getData();
					if (in == null || "".equals(in))
						in = "0";
					stmt.setLong(count, Long.parseLong(in));
				}
				count++;
			}

			retval = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("error.sqlstatement", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("Rollback Exception", e);
			}
			throw new BackendException("error.sqlstatement", e);
		} catch (ParseException e) {
			logger.error("error.datatypeParsing", e);
			throw new BackendException("error.datatypeParsing", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					// log ("Database connection terminated");
				} catch (SQLException e) {
					logger.error("error.closeDBconnection", e);
					// throw new BackendException("error.closeDBconnection", e);
				}
			}
		}
		return retval;
	}

	/*
	 * testing
	 */

	public static void main(String[] args) throws BackendException {
		try {

			CachedRowSet crs = new DBConnector().executeQuery("select * from  test2");
			while (crs.next()) {
				System.out.println("ARGUMENT_NAME:" + crs.getString(1));
				// System.out.println(",DATA_TYPE:"+crs.getString("DATA_TYPE"));
			}
			crs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
