/* @file
 *****************************************************************************
 * @author     This file is part of zkspark, developed by SCIPR Lab
 *             and contributors (see AUTHORS).
 * @copyright  MIT license (see LICENSE file)
 *****************************************************************************/

package algebra.curves.barreto_naehrig.bn256.bn256_parameters;

import algebra.curves.barreto_naehrig.abstract_bn_parameters.AbstractBNGTParameters;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq12;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq2;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq6;
import algebra.curves.barreto_naehrig.bn256.BN256GT;

public class BN256GTParameters
        extends AbstractBNGTParameters<BN256Fq, BN256Fq2, BN256Fq6, BN256Fq12, BN256GT, BN256GTParameters> {

    public static final BN256GT ONE = new BN256GT(BN256Fq12.ONE);

    public BN256GT ONE() {
        return ONE;
    }
}
