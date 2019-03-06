import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: chenchong
 * Date: 2019/3/4
 * description:
 */
public class WordCount {

	private static final Pattern SPACE = Pattern.compile(" ");


	// 一个用户程序 就是一个 Driver
	public static void main(String[] args){
		// new SparkConf
		// SparkContext.getOrCreate

		SparkSession session = SparkSession.builder()
				.master("spark://localhost:7077")
				.appName("demo")
				.getOrCreate();

		JavaRDD<String> lines = session.read().textFile("G:\\77.txt").javaRDD();
		// 转换算子 形成一个或多个 RDD
		// Action算子 触发 Job 的提交
		List<Tuple2<String,Integer>> list = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator())
				.mapToPair(s -> new Tuple2<>(s,1))
				.reduceByKey((a,b)->a+b)
				.collect();
		for(Tuple2<String,Integer> t : list)
			System.out.println(t._1 + "  : " + t._2);
	}
}
