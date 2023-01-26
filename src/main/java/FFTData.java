import java.util.ArrayList;


class FFTResult
{ 
    public ArrayList<String> coefficients;

    public FFTResult()
    {
        coefficients = new ArrayList<>();
    }
}

public class FFTData {
    public ArrayList<String> input_coefficients;
    public int size;
    public String omega;
    public ArrayList<String> expected_output; //not always used - on here for testing purposes
    public FFTResult fft_result = new FFTResult();

}
