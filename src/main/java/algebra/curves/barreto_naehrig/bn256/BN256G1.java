/* @file
 *****************************************************************************
 * @author     This file is part of zkspark, developed by SCIPR Lab
 *             and contributors (see AUTHORS).
 * @copyright  MIT license (see LICENSE file)
 *****************************************************************************/

package algebra.curves.barreto_naehrig.bn256;

import algebra.curves.barreto_naehrig.BNG1;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fr;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256G1Parameters;

public class BN256G1 extends BNG1<BN256Fr, BN256Fq, BN256G1, BN256G1Parameters> {

    public static final BN256G1Parameters G1Parameters = new BN256G1Parameters();

    public BN256G1(
            final BN256Fq X,
            final BN256Fq Y,
            final BN256Fq Z) {
        super(X, Y, Z, G1Parameters);
    }

    public BN256G1 self() {
        return this;
    }

    public BN256G1 construct(final BN256Fq X, final BN256Fq Y, final BN256Fq Z) {
        return new BN256G1(X, Y, Z);
    }
}
