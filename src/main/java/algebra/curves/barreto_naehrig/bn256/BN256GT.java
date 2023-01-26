/* @file
 *****************************************************************************
 * @author     This file is part of zkspark, developed by SCIPR Lab
 *             and contributors (see AUTHORS).
 * @copyright  MIT license (see LICENSE file)
 *****************************************************************************/

package algebra.curves.barreto_naehrig.bn256;

import algebra.curves.barreto_naehrig.BNGT;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq12;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq2;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq6;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256GTParameters;

public class BN256GT extends
        BNGT<BN256Fq, BN256Fq2, BN256Fq6, BN256Fq12, BN256GT, BN256GTParameters> {

    private static final BN256GTParameters GTParameters = new BN256GTParameters();

    public BN256GT(final BN256Fq12 value) {
        super(value, GTParameters);
    }

    public BN256GT construct(final BN256Fq12 element) {
        return new BN256GT(element);
    }
}
