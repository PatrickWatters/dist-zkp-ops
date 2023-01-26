/* @file
 *****************************************************************************
 * @author     This file is part of zkspark, developed by SCIPR Lab
 *             and contributors (see AUTHORS).
 * @copyright  MIT license (see LICENSE file)
 *****************************************************************************/

package algebra.curves.barreto_naehrig.bn256;

import algebra.curves.barreto_naehrig.BNG2;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fq2;
import algebra.curves.barreto_naehrig.bn256.BN256Fields.BN256Fr;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.BN256G2Parameters;

public class BN256G2 extends BNG2<BN256Fr, BN256Fq, BN256Fq2, BN256G2, BN256G2Parameters> {

    private static final BN256G2Parameters G2Parameters = new BN256G2Parameters();

    public BN256G2(
            final BN256Fq2 X,
            final BN256Fq2 Y,
            final BN256Fq2 Z) {
        super(X, Y, Z, G2Parameters);
    }

    public BN256G2 self() {
        return this;
    }

    public BN256G2 construct(final BN256Fq2 X, final BN256Fq2 Y, final BN256Fq2 Z) {
        return new BN256G2(X, Y, Z);
    }
}
