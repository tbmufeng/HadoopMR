package util.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static m.util.PrintUtil.*;
import m.util.FileUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptID;

public class JobSandbox extends Job {

	public JobSandbox(Configuration conf, String root) throws IOException {
		super(conf);
		this.root = root;
	}

	String root;

	List<String> inputFiles = new ArrayList<String>();

	String outPutDir = "";

	public void addInput(String dir) {
		inputFiles.add(dir);
	};

	private void addStr(List<String> targetList, String[] array) {
		for (String str : array) {
			targetList.add(str);
		}
	}

	private List<String> getFiles() {

		List<String> files = new ArrayList<String>();
		for (String filestr : inputFiles) {

			String dir = root + filestr;
			File f = new File(filestr);
			if (f.exists()) {
				if (f.isDirectory()) {
					addStr(files, f.list());
				} else {
					files.add(dir);
				}
			}
		}
		return files;
	}

	public boolean waitForCompletion(boolean wait) {
		try {
			initObj();

			List<String> files = getFiles();
			int total_file_count = files.size();
			pf("find %d input data!", files.size());
			for (String str : files) {
				p("add dir ->" + str);
			}

			int current_file_count = 0;
			for (String str : files) {

				List<String> datas = FileUtils.eachLines(str);
				mapper(datas);
				pf("mapper complete ->%s%", 0.0 * current_file_count
						/ total_file_count);
				current_file_count++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	Mapper<?, ?, ?, ?> mapperObj = null;
	Reducer<?, ?, ?, ?> reduceObj = null;

	private void initObj() throws Exception {
		if (mapperObj == null) {
			mapperObj = getMapperClass().newInstance();
			reduceObj = getReducerClass().newInstance();
		}
	}

	Map<Object, List<Object>> dataCtxMap = new HashMap<Object, List<Object>>();

	private void reducer() {
		
	}

	private void mapper(List<String> contents) throws ClassNotFoundException {

		Class<? extends Mapper<?, ?, ?, ?>> mapperClass = getMapperClass();
		for (String text : contents) {
			TaskAttemptID tId = new TaskAttemptID();

		}
	}

}
