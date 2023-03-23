import static spark.Spark.*;
import com.google.gson.Gson;
import common.CommandLineParser;
import configuration.Configuration;
import profiler.utils.SparkUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import algebra.msm.VariableBaseMSM;
import java.util.ArrayList;
import java.math.BigInteger;
import org.apache.commons.codec.binary.Hex;
import algebra.fft.SerialFFT;
import algebra.curves.barreto_naehrig.bn254a.BN254aG1;
import algebra.curves.barreto_naehrig.bn254a.BN254aFields.BN254aFq;
import algebra.curves.barreto_naehrig.bn254a.BN254aFields.BN254aFr;

class FFTResult
{ 
    public ArrayList<String> coefficients;

    public FFTResult()
    {
        coefficients = new ArrayList<>();
    }
}

class FFTData {
    public ArrayList<String> input_coefficients;
    public int size;
    public String omega;
    public ArrayList<String> expected_output; //not always used -  here for testing purposes
    public FFTResult fft_result = new FFTResult();

}

class MSMResult
{   
    public String x_value;
    public String y_value; 
    public boolean point_of_infinity;
}

class MSMData {
    
    public ArrayList<String> scalars;
    public ArrayList<String> bases_x;
    public ArrayList<String> bases_y;
    public int size;
    public ArrayList<String> output_value; //not always used -  here for testing purposes

    public MSMResult msm_result = new MSMResult();

    public void CleanUpLists()
    {
        for (int i = 0; i <this.bases_x.size(); i++) 
        {
            this.bases_x.set(i, this.bases_x.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x", ""));
            this.bases_y.set(i, this.bases_y.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x", ""));
        }
        for (int i = 0; i <this.output_value.size(); i++) 
        {
            this.output_value.set(i, this.output_value.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x", ""));
        }
    }

}


public class ZKPServer {
    
    public static void main (String args[])
    {
        /* 
        CommandLineParser clp = new CommandLineParser(args);
        boolean isDistrubutedMode = clp.getFlag("dist");
        
        Configuration config = new Configuration();

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
            */

    }


    public static void StartSevice(Configuration config)
    {

        post("/fft", (req, res) -> {
            
            res.type("application/json");
            long startTime = System.currentTimeMillis();

            FFTData fft_data = new Gson().fromJson(req.body(),  FFTData.class);
            //ArrayList<String> input_coeffs =  new Gson().fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
            System.out.println("Started FFT of Size: " + fft_data.size);
            
            ExecuteFFT(fft_data);
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
            ExecuteMSM(msm_data);
            long endTime  = System.currentTimeMillis();
            System.out.println("MSM of Size " + msm_data.size + " took " + ((endTime - startTime)/1000) + " seconds");     
            return new Gson().toJson(msm_data.msm_result);
          });

    }

    public static void ExecuteFFT(FFTData fft_data) 
    {
        final BN254aFr fieldFactory = new BN254aFr(0L);
        final ArrayList<BN254aFr> serial = new ArrayList<>();

        ///prepre omega 
        BN254aFr omega_value = new BN254aFr(new BigInteger(fft_data.omega,16));

        // loading data into to big ints.
        for (String line : fft_data.input_coefficients) {
          BN254aFr frt = new BN254aFr(new BigInteger(line, 16));
          serial.add(fieldFactory.add(frt));
        }

        SerialFFT<BN254aFr> domain = new SerialFFT<>(serial.size(), omega_value);
        
        domain.radix2InverseFFT(serial);
        
        for (BN254aFr i : serial) {
          String hex = Hex.encodeHexString(i.toBigInteger().toByteArray());

          while(hex.length() < 64)
          {
            hex = "0" + hex;
          }
          fft_data.fft_result.coefficients.add(hex);
        }

      }
    

    public static void ExecuteMSM(MSMData msm_data)
    {
        final BN254aFr fieldFactory = new BN254aFr(0);
        final ArrayList<BN254aFr> scalars = new ArrayList<>();
        final ArrayList<BN254aG1> bases = new ArrayList<>();

        // populate scalars and bases
        for (int i = 0; i < msm_data.scalars.size(); i++) {

            scalars.add(fieldFactory.add(new BN254aFr(new BigInteger(msm_data.scalars.get(i), 16))));

            // clean up some x and y strings -- do this a better way later
            String x_value = msm_data.bases_x.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x",
                    "");
            String y_value = msm_data.bases_y.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x",
                    "");
            BN254aFq x = new BN254aFq(new BigInteger(x_value, 16));
            BN254aFq y = new BN254aFq(new BigInteger(y_value, 16));
            bases.add(new BN254aG1(x, y, BN254aFq.ONE));
        }

        // final BN256G1 result = NaiveMSM.variableBaseMSM(scalars,
        // bases).toAffineCoordinates(); // UNUSED

        final BN254aG1 result = VariableBaseMSM.serialMSM(scalars, bases).toAffineCoordinates(); // UNUSED
        String x_as_hex = Hex.encodeHexString((result.getX().toBigInteger().toByteArray()));
        String y_as_hex = Hex.encodeHexString((result.getY().toBigInteger().toByteArray()));
        String z_as_hex = Hex.encodeHexString((result.Z.toBigInteger().toByteArray()));

        // find a better way to do things instead of doing the below:
        while (x_as_hex.length() < 64) {
            x_as_hex = "0" + x_as_hex;
        }

        while (y_as_hex.length() < 64) {
            y_as_hex = "0" + y_as_hex;
        }
        while (z_as_hex.length() < 64) {
          z_as_hex = "0" + z_as_hex;
      }
        msm_data.msm_result.x_value = x_as_hex;
        msm_data.msm_result.y_value = y_as_hex;
        msm_data.msm_result.point_of_infinity = result.isZero();

        System.out.println("Result (X):" + x_as_hex);
        System.out.println("Result (y):" + y_as_hex);
        System.out.println("Result (y):" + z_as_hex);

         System.out.println("Is infinity:" + result.isZero());
    }
}