package algebra.curves.barreto_naehrig.bn256;

import algebra.fields.Fp;
import algebra.fields.Fp12_2Over3Over2;
import algebra.fields.Fp2;
import algebra.fields.Fp6_3Over2;
import algebra.curves.barreto_naehrig.BNFields.*;
import algebra.curves.barreto_naehrig.bn256.bn256_parameters.*;

import java.io.Serializable;
import java.math.BigInteger;

public class BN256Fields {
    /* Scalar field Fr */
    public static class BN256Fr extends BNFr<BN256Fr> implements Serializable {

        public static final BN256FrParameters FrParameters = new BN256FrParameters();
        public static final BN256Fr ZERO = new BN256Fr(FrParameters.ZERO());
        public static final BN256Fr ONE = new BN256Fr(FrParameters.ONE());
        public static final BN256Fr MULTIPLICATIVE_GENERATOR =
                new BN256Fr(FrParameters.multiplicativeGenerator());

        public Fp element;

        public BN256Fr(final BigInteger number) {
            this.element = new Fp(number, FrParameters);
        }

        public BN256Fr(final Fp number) {
            this(number.toBigInteger());
        }

        public BN256Fr(final String number) {
            this(new BigInteger(number));
        }

        public BN256Fr(final long number) {
            this(BigInteger.valueOf(number));
        }

        public BN256Fr self() {
            return this;
        }

        public Fp element() {
            return element;
        }

        public BN256Fr zero() {
            return ZERO;
        }

        public BN256Fr one() {
            return ONE;
        }

        public BN256Fr multiplicativeGenerator() {
            return MULTIPLICATIVE_GENERATOR;
        }

        public BN256Fr construct(final long number) {
            return new BN256Fr(number);
        }

        public BN256Fr construct(final Fp element) {
            return new BN256Fr(element);
        }

        public String toString() {
            return this.element.toString();
        }
    }

    /* Base field Fq */
    public static class BN256Fq extends BNFq<BN256Fq> implements Serializable {

        public static final BN256FqParameters FqParameters = new BN256FqParameters();
        public static final BN256Fq ZERO = new BN256Fq(FqParameters.ZERO());
        public static final BN256Fq ONE = new BN256Fq(FqParameters.ONE());
        public static final BN256Fq MULTIPLICATIVE_GENERATOR =
                new BN256Fq(FqParameters.multiplicativeGenerator());

        public Fp element;

        public BN256Fq(final Fp element) {
            this.element = element;
        }

        public BN256Fq(final BigInteger number) {
            this.element = new Fp(number, FqParameters);
        }

        public BN256Fq(final String number) {
            this(new BigInteger(number));
        }

        public BN256Fq(final long number) {
            this(BigInteger.valueOf(number));
        }

        public BN256Fq self() {
            return this;
        }

        public Fp element() {
            return element;
        }

        public BN256Fq zero() {
            return ZERO;
        }

        public BN256Fq one() {
            return ONE;
        }

        public BN256Fq multiplicativeGenerator() {
            return MULTIPLICATIVE_GENERATOR;
        }

        public BN256Fq construct(final Fp element) {
            return new BN256Fq(element);
        }

        public BN256Fq construct(final String element) {
            return new BN256Fq(element);
        }

        public BN256Fq construct(final long number) {
            return new BN256Fq(number);
        }

        public String toString() {
            return this.element.toString();
        }
    }

    /* Twist field Fq2 */
    public static class BN256Fq2 extends BNFq2<BN256Fq, BN256Fq2> implements Serializable {

        public static final BN256Fq2Parameters Fq2Parameters = new BN256Fq2Parameters();
        public static BN256Fq2 ZERO = new BN256Fq2(Fq2Parameters.ZERO());
        public static BN256Fq2 ONE = new BN256Fq2(Fq2Parameters.ONE());

        public Fp2 element;

        public BN256Fq2(final Fp2 element) {
            this.element = element;
        }

        public BN256Fq2(final BigInteger c0, final BigInteger c1) {
            this.element = new Fp2(c0, c1, Fq2Parameters);
        }

        public BN256Fq2(final BN256Fq c0, final BN256Fq c1) {
            this(c0.toBigInteger(), c1.toBigInteger());
        }

        public BN256Fq2(final long c0, final long c1) {
            this(BigInteger.valueOf(c0), BigInteger.valueOf(c1));
        }

        public BN256Fq2 self() {
            return this;
        }

        public Fp2 element() {
            return this.element;
        }

        public BN256Fq2 zero() {
            return ZERO;
        }

        public BN256Fq2 one() {
            return ONE;
        }

        public BN256Fq2 construct(final Fp2 element) {
            return new BN256Fq2(element);
        }

        public BN256Fq2 construct(final BN256Fq c0, final BN256Fq c1) {
            return new BN256Fq2(c0, c1);
        }

        public BN256Fq2 construct(final long c0, final long c1) {
            return new BN256Fq2(c0, c1);
        }

        public String toString() {
            return this.element.toString();
        }
    }

    /* Field Fq6 */
    public static class BN256Fq6 extends BNFq6<BN256Fq, BN256Fq2, BN256Fq6> implements Serializable {

        public static final BN256Fq6Parameters Fq6Parameters = new BN256Fq6Parameters();
        public static BN256Fq6 ZERO = new BN256Fq6(Fq6Parameters.ZERO());
        public static BN256Fq6 ONE = new BN256Fq6(Fq6Parameters.ONE());

        public Fp6_3Over2 element;

        public BN256Fq6(final Fp6_3Over2 element) {
            this.element = element;
        }

        public BN256Fq6(final BN256Fq2 c0, final BN256Fq2 c1, final BN256Fq2 c2) {
            this.element = new Fp6_3Over2(c0.element, c1.element, c2.element, Fq6Parameters);
        }

        public BN256Fq6 self() {
            return this;
        }

        public Fp6_3Over2 element() {
            return this.element;
        }

        public BN256Fq6 zero() {
            return ZERO;
        }

        public BN256Fq6 one() {
            return ONE;
        }

        public Fp2 mulByNonResidue(final Fp2 that) {
            return Fq6Parameters.nonresidue().mul(that);
        }

        public BN256Fq6 construct(final Fp6_3Over2 element) {
            return new BN256Fq6(element);
        }

        public String toString() {
            return this.element.toString();
        }
    }

    /* Field Fq12 */
    public static class BN256Fq12 extends BNFq12<BN256Fq, BN256Fq2, BN256Fq6, BN256Fq12> implements Serializable {

        public static final BN256Fq12Parameters Fq12Parameters = new BN256Fq12Parameters();
        public static BN256Fq12 ZERO = new BN256Fq12(Fq12Parameters.ZERO());
        public static BN256Fq12 ONE = new BN256Fq12(Fq12Parameters.ONE());

        public Fp12_2Over3Over2 element;

        public BN256Fq12(final Fp12_2Over3Over2 element) {
            this.element = element;
        }

        public BN256Fq12(final BN256Fq6 c0, final BN256Fq6 c1) {
            this.element = new Fp12_2Over3Over2(c0.element, c1.element, Fq12Parameters);
        }

        public BN256Fq12 self() {
            return this;
        }

        public Fp12_2Over3Over2 element() {
            return this.element;
        }

        public BN256Fq12 zero() {
            return ZERO;
        }

        public BN256Fq12 one() {
            return ONE;
        }

        public BN256Fq12 construct(final Fp12_2Over3Over2 element) {
            return new BN256Fq12(element);
        }

        public String toString() {
            return this.element.toString();
        }
    }
}
