package profiler;
import configuration.Configuration;
import profiler.utils.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

public class Profiler {

  public static void main(String[] args) 
  {

    if (args.length > 0) {
      String app = args[0].toLowerCase();
      long size = (long) Math.pow(2, Long.parseLong(args[1]));
      int numCores = Integer.parseInt(args[2]);
      int numMemory = Integer.parseInt(args[3].substring(0, args[3].length() - 1));
      int numExecutors = Integer.parseInt(args[4]);
      int numPartitions;
   
      if (args.length == 5) {
        numPartitions = SparkUtils.numPartitions(numExecutors, size);
      } else {
        numPartitions = Integer.parseInt(args[5]);
      }

      if(app.equals("serial-fft"))
      {
        final SparkSession spark = SparkSession.builder().appName(app).getOrCreate();
        spark.sparkContext().conf().set("spark.files.overwrite", "true");
        //spark.sparkContext().conf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        //spark.sparkContext().conf().registerKryoClasses(SparkUtils.zksparkClasses());
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        Configuration config =new Configuration(true,numExecutors,numCores,numMemory,numPartitions,sc,StorageLevel.MEMORY_AND_DISK_SER());
        System.out.format("\n[Profiler] - Start Serial %s - %d size\n", app, size);

        FFTProfiling.serialFFTProfiling(config, size);
        System.out.format("\n[Profiler] - End Serial %s - %d size\n", app, size);

      }
      else
      { 
        final SparkSession spark = SparkSession.builder().appName(app).getOrCreate();
        spark.sparkContext().conf().set("spark.files.overwrite", "true");
        //spark.sparkContext().conf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        //spark.sparkContext().conf().registerKryoClasses(SparkUtils.zksparkClasses());
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        Configuration config =new Configuration(true,numExecutors,numCores,numMemory,numPartitions,sc,StorageLevel.MEMORY_AND_DISK_SER());
        System.out.format(
          "\n[Profiler] - Start Distributed %s - %d executors - %d partitions - %d size\n\n",
          app, config.numExecutors(), config.numPartitions(), size);
  
        FFTProfiling.distributedFFTProfiling(config, size);
        System.out.format(
          "\n[Profiler] - End Distributed %s - %d executors - %d partitions - %d size\n\n",
          app, config.numExecutors(), config.numPartitions(), size);
  

      }
    }else {

      //default when no arguments are given
      final String app = "default-dist-fft";
      final int numExecutors = 1;
      final int numCores = 8;
      final int numMemory = 12;
      final long size = (long) Math.pow(2, Long.parseLong("10"));
      final int numPartitions = SparkUtils.numPartitions(numExecutors, size);

      /*
      final SparkSession spark = SparkSession.builder().appName(app).getOrCreate();
      spark.sparkContext().conf().set("spark.files.overwrite", "true");
      //spark.sparkContext().conf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
      //spark.sparkContext().conf().registerKryoClasses(SparkUtils.zksparkClasses()); 
      JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
       */
      final SparkConf conf = new SparkConf().setMaster("local").setAppName(app);
      //conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
      //conf.set("spark.kryo.registrationRequired", "true");
      //conf.registerKryoClasses(SparkUtils.zksparkClasses());
      JavaSparkContext sc = new JavaSparkContext(conf);
      
      Configuration config =new Configuration(false,numExecutors,numCores,numMemory,numPartitions,sc,StorageLevel.MEMORY_AND_DISK_SER());
      System.out.format(
        "\n[Profiler] - Start Distributed %s - %d executors - %d partitions - %d size\n\n",
        app, config.numExecutors(), config.numPartitions(), size);

      FFTProfiling.distributedFFTProfiling(config, size);
      System.out.format(
        "\n[Profiler] - End Distributed %s - %d executors - %d partitions - %d size\n\n",
        app, config.numExecutors(), config.numPartitions(), size);

    }
  }
}
