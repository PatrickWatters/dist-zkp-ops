import java.util.ArrayList;

class MSMResult
{   
    public String x_value;
    public String y_value; 
    public boolean point_of_infinity;
}

public class MSMData {

    public ArrayList<String> scalars;
    public ArrayList<String> bases_x;
    public ArrayList<String> bases_y;
    public int size;
    public ArrayList<String> output_value; //not always used - on here for testing purposes

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
