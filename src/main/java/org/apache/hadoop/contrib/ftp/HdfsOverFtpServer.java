package org.apache.hadoop.contrib.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.log4j.Logger;

/**
 * Start-up class of FTP server
 */
public class HdfsOverFtpServer {

	private static Logger log = Logger.getLogger(HdfsOverFtpServer.class);

	private static int port = 0;
	private static int sslPort = 0;
	private static String passivePorts = null;
	private static String sslPassivePorts = null;
	private static String hdfsUri = null;

	public static void main(String[] args) throws Exception {
		loadConfig();

		if (port != 0) {
			startServer();
		}

		if (sslPort != 0) {
			startSSLServer();
		}
	}

	/**
	 * Load configuration
	 * 
	 * @throws IOException
	 */
	private static void loadConfig() throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(loadResource("/hdfs-over-ftp.properties")));

		try {
			port = Integer.parseInt(props.getProperty("port"));
			log.info("port is set. ftp server will be started");
		} catch (Exception e) {
			log.info("port is not set. so ftp server will not be started");
		}

		try {
			sslPort = Integer.parseInt(props.getProperty("ssl-port"));
			log.info("ssl-port is set. ssl server will be started");
		} catch (Exception e) {
			log.info("ssl-port is not set. so ssl server will not be started");
		}

		if (port != 0) {
			passivePorts = props.getProperty("data-ports");
			if (passivePorts == null) {
				log.fatal("data-ports is not set");
				System.exit(1);
			}
		}

		if (sslPort != 0) {
			sslPassivePorts = props.getProperty("ssl-data-ports");
			if (sslPassivePorts == null) {
				log.fatal("ssl-data-ports is not set");
				System.exit(1);
			}
		}

		hdfsUri = props.getProperty("hdfs-uri");
		if (hdfsUri == null) {
			log.fatal("hdfs-uri is not set");
			System.exit(1);
		}

		String superuser = props.getProperty("superuser");
		if (superuser == null) {
			log.fatal("superuser is not set");
			System.exit(1);
		}
		HdfsOverFtpSystem.setSuperuser(superuser);
	}

	/**
	 * Starts FTP server
	 * 
	 * @throws Exception
	 */
	public static void startServer() throws Exception {

		log.info("Starting Hdfs-Over-Ftp server. port: " + port + " data-ports: " + passivePorts + " hdfs-uri: " + hdfsUri);

		HdfsOverFtpSystem.setHDFS_URI(hdfsUri);

		
//		DataConnectionConfiguration dataCon = new DefaultDataConnectionConfiguration();
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(port);
//		factory.setDataConnectionConfiguration((DataConnectionConfiguration) dataCon);
		serverFactory.addListener("default", factory.createListener());
		HdfsUserManager userManager = new HdfsUserManager(hdfsUri, new Md5PasswordEncryptor());
		final File file = loadResource("/users.properties");
//		userManager.setFile(file);
//		userManagerFactory.setFile(file);
		userManager.setFile(file);
		serverFactory.setUserManager(userManager);
		serverFactory.setFileSystem(new HdfsFileSystemManager());
		FtpServer server = serverFactory.createServer();
		
//		dataCon.setPassivePorts(passivePorts);
//		server.getListener("default").setDataConnectionConfiguration(dataCon);
//		server.getListener("default").setPort(port);

//		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		
		
//		server.setUserManager(userManager);
//
//		server.setFileSystem(new HdfsFileSystemManager());

		server.start();
	}

	private static File loadResource(String resourceName) {
		final URL resource = HdfsOverFtpServer.class.getResource(resourceName);
		if (resource == null) {
			throw new RuntimeException("Resource not found: " + resourceName);
		}
		return new File(resource.getFile());
	}

	/**
	 * Starts SSL FTP server
	 * 
	 * @throws Exception
	 */
	public static void startSSLServer() throws Exception {

		log.info("Starting Hdfs-Over-Ftp SSL server. ssl-port: " + sslPort + " ssl-data-ports: " + sslPassivePorts + " hdfs-uri: " + hdfsUri);

//		HdfsOverFtpSystem.setHDFS_URI(hdfsUri);
//
//		FtpServer server = new FtpServer();
//
//		DataConnectionConfiguration dataCon = new DefaultDataConnectionConfiguration();
//		dataCon.setPassivePorts(sslPassivePorts);
//		server.getListener("default").setDataConnectionConfiguration(dataCon);
//		server.getListener("default").setPort(sslPort);
//
//		MySslConfiguration ssl = new MySslConfiguration();
//		ssl.setKeystoreFile(new File("ftp.jks"));
//		ssl.setKeystoreType("JKS");
//		ssl.setKeyPassword("333333");
//		server.getListener("default").setSslConfiguration(ssl);
//		server.getListener("default").setImplicitSsl(true);
//
//		HdfsUserManager userManager = new HdfsUserManager();
//		userManager.setFile(new File("users.conf"));
//
//		server.setUserManager(userManager);
//
//		server.setFileSystem(new HdfsFileSystemManager());
//
//		server.start();
	}
}
