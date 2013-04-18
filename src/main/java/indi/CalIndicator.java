package indi;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CalIndicator {

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			BizOrder bizOrder = null;
			try {
				bizOrder = new BizOrder(value.toString());

			} catch (Exception e) {

				System.out.println("Invalid:"
						+ value.toString().split("\t").length + ":"
						+ value.toString());
				System.err.print(e);
				return;
			}

			System.out.println(bizOrder.isValid() + ":"
					+ bizOrder.getBizOrderId());
		}

	}

	public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			result.set(key.toString());
			context.write(key, result);
		}
	}

	static String bizPath = "/group/taobao/";
	static String targetPath = "/out/" + System.currentTimeMillis();

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = new Job(conf, "word count");
		conf.set("ugi", "mufeng.qcg");
		conf.set("fs.default.name", "hdfs://v129201.sqa.cm4.tbsite.net:9000//");
		job.setJarByClass(CalIndicator.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		Path inputPath = new Path(bizPath);
		FileInputFormat.addInputPath(job, inputPath);
		// SequenceFileInputFormat.addInputPath(job, new Path(bizPath));
		// TextInputFormat.addInputPath(job, new Path(bizPath));
		TextOutputFormat.setOutputPath(job, new Path(targetPath));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
