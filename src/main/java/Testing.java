
import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class Testing {
  private static final DecimalFormat dfZero = new DecimalFormat("0.00");

  public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
    
    //TestMSMFromJsonFile("msm_data_example/4_scalars.json");


    //TestMSMFromJsonFile("msm_data_example/4_scalars.json");
    // TestFromJsonFileFolder("C:\\Users\\watters\\projects\\bellman\\msm_json_examples");
  }

  private static boolean TestMSMFromJsonFile(String file_path)
      throws JsonSyntaxException, JsonIOException, FileNotFoundException {
    Executor executor = new Executor();
    MSMData msm_data = new Gson().fromJson(new FileReader(file_path), MSMData.class);
    msm_data.CleanUpLists();


    executor.ExecuteMSM(msm_data);
    
    //Triplet<String, String, Boolean> response = new Triplet.with(msm_data.ouput_x_value, msm_data.ouput_y_value, msm_data.result_is_at_point_of_infinity);
    //Quartet<String, String, Integer, String> quartet = new Quartet<String, String, Integer, String >("Sophia", "Female", 22, "Marketing Manager");  

    System.out.println(new Gson().toJson(msm_data.msm_result));     
    //System.out.println(Gson().toJson(msm_data.msm_result));
    if (msm_data.msm_result.x_value.equals(msm_data.output_value.get(0)) && msm_data.msm_result.y_value.equals(msm_data.output_value.get(1))) {
      System.out.println("Success:" + Paths.get(file_path).getFileName().toString());
      return true;
    } else {
      System.out.println("Fail:" + Paths.get(file_path).getFileName().toString());
      return false;
    }
  }

  private static void TestFromJsonFileFolder(String folder_path)
      throws JsonSyntaxException, JsonIOException, FileNotFoundException {
    int succsses = 0;
    int failures = 0;
    File folder = new File(folder_path);
    File[] json_files = folder.listFiles();

    for (File msm_example : json_files) {
      if (TestMSMFromJsonFile(msm_example.getPath())) {
        succsses = succsses + 1;
      } else {
        failures = failures + 1;
      }
    }
    double success_rate = ((double) succsses) / ((double) succsses + failures) * 100;
    System.out.println(
        "Total Passed: " + succsses + ", Total Failed: " + failures + ", Success %: " + dfZero.format(success_rate));
  }

}
