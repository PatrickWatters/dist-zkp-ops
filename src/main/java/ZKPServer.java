import static spark.Spark.*;
import com.google.gson.Gson;

import common.CommandLineParser;
import common.SparkUtils;
import configuration.Configuration;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class ZKPServer {
    
    public static void main (String args[])
    {
        CommandLineParser clp = new CommandLineParser(args);
        boolean isDistrubutedMode = clp.getFlag("dist");
        Configuration config = new Configuration(isDistrubutedMode);

        if(isDistrubutedMode){          

            System.out.println("Starting FFT/MSM Service in Distubuted Mode with Apache Spark. Configuration will be defaulted if not specified in command line args.");
          
            if(clp.getArgumentValue("executors") != null){
                 config.setNumExecutors((Integer.parseInt(clp.getArgumentValue("executors"))));
            }
            if(clp.getArgumentValue("cores") != null){
                config.setNumCores(Integer.parseInt(clp.getArgumentValue("cores")));
            }
            if(clp.getArgumentValue("memory") != null){     
                config.setNumMemory(Integer.parseInt(clp.getArgumentValue("memory")));

            }if(clp.getArgumentValue("partitions") != null){
                config.setNumPartitions(Integer.parseInt(clp.getArgumentValue("partitions")));
            }

            System.out.println("\tNumExecutors:" + config.numExecutors());
            System.out.println("\tNumCores:" + config.numCores());
            System.out.println("\tNumMemory:" + config.numMemory());
            System.out.println("\tNumPartitions:" + config.numPartitions());

            SparkSession spark =  SparkSession.builder().appName(SparkUtils.appName("FFT/MSM Service")).getOrCreate();
            spark.sparkContext().conf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
            spark.sparkContext().conf().registerKryoClasses(SparkUtils.zksparkClasses());
            JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
            config.setSparkContext(sc);
        }

        StartSevice(config);
    }


    public static void StartSevice(Configuration config)
    {
        Executor executor = new Executor();

        post("/fft", (req, res) -> {
            
            res.type("application/json");
            long startTime = System.currentTimeMillis();

            FFTData fft_data = new Gson().fromJson(req.body(),  FFTData.class);
            //ArrayList<String> input_coeffs =  new Gson().fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
            System.out.println("Started FFT of Size: " + fft_data.size);
            
            executor.ExecuteFFT(fft_data);
            //System.out.println(new Gson().toJson(fft_data.fft_result));
            long endTime  = System.currentTimeMillis();     
            System.out.println("FFT of Size " + fft_data.size + " took " + ((endTime - startTime)) + "ms");

            return new Gson().toJson(fft_data.fft_result);
          });
          
           post("/msm", (req, res) -> {    
            res.type("application/json");
            long startTime = System.currentTimeMillis();
            MSMData msm_data = new Gson().fromJson(req.body(),  MSMData.class);
            System.out.println("Started MSM of Size: " + msm_data.size);
            executor.ExecuteMSM(msm_data);
            long endTime  = System.currentTimeMillis();
            System.out.println("MSM of Size " + msm_data.size + " took " + ((endTime - startTime)/1000) + " seconds");     
            return new Gson().toJson(msm_data.msm_result);
          });

    }    
}