package me.zenox.superitems.util;

import java.util.Set;
import java.util.EnumSet;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Roman numeral class I definitely didn't steal from someone
 */
public interface Romans {
    enum Numeral {
        M(1000), CM(900), D(500), CD(400), C(100), XC(90), L(50), XL(40), X(10), IX(9), V(5), IV(4), I(1);

        public final long weight;

        private static final Set<Numeral> SET = Collections.unmodifiableSet(EnumSet.allOf(Numeral.class));

        Numeral(long weight) {
            this.weight = weight;
        }

        /**
         * Finds the largest numeral below the given weight
         * @param weight The weight given
         * @return The largest numeral
         */
        public static Numeral getLargest(long weight) {
            return SET.stream()
                    .filter(numeral -> weight >= numeral.weight)
                    .findFirst()
                    .orElse(I)
                    ;
        }
    }

    static String encode(long n) {
        return LongStream.iterate(n, l -> l - Numeral.getLargest(l).weight)
                .limit(Numeral.values().length)
                .filter(l -> l > 0)
                .mapToObj(Numeral::getLargest)
                .map(String::valueOf)
                .collect(Collectors.joining())
                ;
    }

    static long decode(String roman) {
        long result =  new StringBuilder(roman.toUpperCase()).reverse().chars()
                .mapToObj(c -> Character.toString((char) c))
                .map(numeral -> Enum.valueOf(Numeral.class, numeral))
                .mapToLong(numeral -> numeral.weight)
                .reduce(0, (a, b) -> a + (a <= b ? b : -b))
                ;
        if (roman.charAt(0) == roman.charAt(1)) {
            result += 2 * Enum.valueOf(Numeral.class, roman.substring(0, 1)).weight;
        }
        return result;
    }
}
