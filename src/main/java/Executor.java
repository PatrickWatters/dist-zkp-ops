import algebra.msm.VariableBaseMSM;
import algebra.curves.barreto_naehrig.bn256.BN256G1;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fr;
import java.util.ArrayList;
import java.math.BigInteger;
import org.apache.commons.codec.binary.Hex;
import algebra.fft.SerialFFT;

public class Executor {

    public void ExecuteFFT(FFTData fft_data) 
    {
        final BN256Fr fieldFactory = new BN256Fr(0L);
        final ArrayList<BN256Fr> serial = new ArrayList<>();

        ///prepre omega 
        BN256Fr omega_value = new BN256Fr(new BigInteger(fft_data.omega,16));

        // loading data into to big ints.
        for (String line : fft_data.input_coefficients) {
          BN256Fr frt = new BN256Fr(new BigInteger(line, 16));
          serial.add(fieldFactory.add(frt));
        }

        SerialFFT<BN256Fr> domain = new SerialFFT<>(serial.size(), omega_value);
        
        domain.radix2InverseFFT(serial);
        
        for (BN256Fr i : serial) {
          String hex = Hex.encodeHexString(i.toBigInteger().toByteArray());

          while(hex.length() < 64)
          {
            hex = "0" + hex;
          }
          fft_data.fft_result.coefficients.add(hex);
        }

      }
    

    public void ExecuteMSM(MSMData msm_data)
    {
        final BN256Fr fieldFactory = new BN256Fr(0);
        final ArrayList<BN256Fr> scalars = new ArrayList<>();
        final ArrayList<BN256G1> bases = new ArrayList<>();

        // populate scalars and bases
        for (int i = 0; i < msm_data.scalars.size(); i++) {

            scalars.add(fieldFactory.add(new BN256Fr(new BigInteger(msm_data.scalars.get(i), 16))));

            // clean up some x and y strings -- do this a better way later
            String x_value = msm_data.bases_x.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x",
                    "");
            String y_value = msm_data.bases_y.get(i).replace("(", "").replace(")", "").replace("Fq", "").replace("0x",
                    "");
            BN256Fq x = new BN256Fq(new BigInteger(x_value, 16));
            BN256Fq y = new BN256Fq(new BigInteger(y_value, 16));
            bases.add(new BN256G1(x, y, BN256Fq.ONE));
        }

        // final BN256G1 result = NaiveMSM.variableBaseMSM(scalars,
        // bases).toAffineCoordinates(); // UNUSED

        final BN256G1 result = VariableBaseMSM.serialMSM(scalars, bases).toAffineCoordinates(); // UNUSED
        String x_as_hex = Hex.encodeHexString((result.X.toBigInteger().toByteArray()));
        String y_as_hex = Hex.encodeHexString((result.Y.toBigInteger().toByteArray()));
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
