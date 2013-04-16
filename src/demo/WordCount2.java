package demo;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 
 * ������WordCount explains by York
 * 
 * @author Hadoop Dev Group
 */
public class WordCount2 {
	/**
	 * ����Mapper��TokenizerMapper�̳��Է�����Mapper Mapper��:ʵ����Map���ܻ��� Mapper�ӿڣ�
	 * WritableComparable�ӿڣ�ʵ��WritableComparable��������໥�Ƚϡ����б�����key����Ӧ��ʵ�ִ˽ӿڡ�
	 * Reporter ������ڱ�������Ӧ�õ����н��ȣ�������δʹ�á�
	 * 
	 */
	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		/**
		 * IntWritable, Text ���� Hadoop ��ʵ�ֵ����ڷ�װ Java
		 * �������͵��࣬��Щ��ʵ����WritableComparable�ӿڣ�
		 * ���ܹ������л��Ӷ������ڷֲ�ʽ�����н������ݽ���������Խ����Ƿֱ���Ϊint,String �����Ʒ��
		 * ����one������word���ڴ�ŵ��ʵı���
		 */
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		/**
		 * Mapper�е�map������ void map(K1 key, V1 value, Context context)
		 * ӳ��һ������������k/v�Ե�һ���м��k/v�� ����Բ���Ҫ�����������ͬ�����ͣ�����Կ���ӳ�䵽0����������ԡ�
		 * Context���ռ�Mapper�����<k,v>�ԡ� Context��write(k, v)����:����һ��(k,v)�Ե�context
		 * ����Ա��Ҫ��дMap��Reduce����
		 * .���Map����ʹ��StringTokenizer�������ַ������зָ�,ͨ��write�����ѵ��ʴ���word��
		 * write��������(����,1)�����Ķ�Ԫ�鵽context��
		 */
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		/**
		 * Reducer���е�reduce������ void reduce(Text key, Iterable<IntWritable>
		 * values, Context context)
		 * ��k/v������map�����е�context,���ܾ����˽�һ������(combiner),ͬ��ͨ��context���
		 */
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		/**
		 * Configuration��map/reduce��j�����࣬��hadoop�������map-reduceִ�еĹ���
		 */
		Configuration conf = new Configuration();
		/*
		 * String[] otherArgs =new GenericOptionsParser(conf,
		 * args).getRemainingArgs(); if (otherArgs.length !=2) {
		 * System.err.println("Usage: wordcount <in> <out>"); System.exit(2); }
		 */
		Job job = new Job(conf, "word count"); // ����һ���û������job����
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class); // Ϊjob����Mapper��
		job.setCombinerClass(IntSumReducer.class); // Ϊjob����Combiner��
		job.setReducerClass(IntSumReducer.class); // Ϊjob����Reducer��
		job.setOutputKeyClass(Text.class); // Ϊjob�������������Key��
		job.setOutputValueClass(IntWritable.class); // Ϊjob�������value��
		FileInputFormat.addInputPath(job, new Path(args[0])); // Ϊjob��������·��
		FileOutputFormat.setOutputPath(job, new Path(args[1]));// Ϊjob�������·��
		System.exit(job.waitForCompletion(true) ? 0 : 1); // ����job
	}
}