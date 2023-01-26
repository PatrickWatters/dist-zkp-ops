/* @file
 *****************************************************************************
 * @author     This file is part of zkspark, developed by SCIPR Lab
 *             and contributors (see AUTHORS).
 * @copyright  MIT license (see LICENSE file)
 *****************************************************************************/

package algebra.curves.barreto_naehrig.bn256;

import algebra.curves.barreto_naehrig.BNPairing;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.*;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256G1Parameters;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256G2Parameters;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256GTParameters;

public class BN256Pairing extends BNPairing<
        BN256Fr, BN256Fq, BN256Fq2, BN256Fq6, BN256Fq12, BN256G1, BN256G2, BN256GT, BN256G1Parameters, BN256G2Parameters, BN256GTParameters, BN256PublicParameters> {

    private static final BN256PublicParameters publicParameters = new BN256PublicParameters();

    public BN256PublicParameters publicParameters() {
        return publicParameters;
    }

    public BN256GT reducedPairing(final BN256G1 P, final BN256G2 Q) {
        final BN256Fq12 f = atePairing(P, Q);
        final BN256Fq12 result = finalExponentiation(f);
        return new BN256GT(result);
    }
}
