package com.asiainfo.ocmanager.service.client.v2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.security.auth.Subject;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

/**
 * New HDFS client supports for multi-clusters.
 * @author Ethan
 *
 */
public class HDFSClient extends ServiceClient{
	private static final Logger LOG = Logger.getLogger(HDFSClient.class);
	private FileSystem fs;

	protected HDFSClient(String serviceName, Subject subject) {
		super(serviceName, subject);
		try {
			init();
		} catch (Exception e) {
			LOG.error("Exception while init FS: ", e);
			throw new RuntimeException("Exception while init FS: ", e);
		}
	}

	private void init() throws Exception {
		fs = doPrivileged(new SomeAction<FileSystem>() {

			@Override
			public FileSystem run() throws Exception {
				return FileSystem.get(initHDFSCommon(serviceConfig));
			}
		});
	}

	private Configuration initHDFSCommon(Properties props) {
		Configuration conf = new Configuration();
		for (Entry<Object, Object> p : props.entrySet()) {
			conf.set(String.valueOf(p.getKey()), String.valueOf(p.getValue()));
		}
		return conf;
	}
	
	public FileSystem getFS() {
		return fs;
	}
	
	public static void main(String[] args) {
		Subject sub = new Subject();
		FileSystem filesys = new HDFSClient("hdfs", sub).getFS();
		try {
			FileStatus[] files = filesys.listStatus(new Path("/"));
			for (FileStatus file : files) {
				System.out.println(">>> " + file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end of main");
	}

}
