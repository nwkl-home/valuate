package com.lwkl.valuate.eval;

import com.lwkl.valuate.enums.OperatorSymbol;

import java.util.HashMap;
import java.util.Map;

public class Symbol {

    public static final Map<String, OperatorSymbol> COMPARATORSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> LOGICALSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> BITWISESYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> BITWISESHIFTSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> ADDITIVESYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> MULTIPLICATIVESYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> EXPONENTIALSYMBOLSS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> PREFIXSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> TERNARYSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> MODIFIERSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> SEPARATORSYMBOLS = new HashMap<String, OperatorSymbol>();


    public static final Map<String, OperatorSymbol> ANDSYMBOLS = new HashMap<String, OperatorSymbol>();
    public static final Map<String, OperatorSymbol> ORSYMBOLS = new HashMap<String, OperatorSymbol>();


    static {
        COMPARATORSYMBOLS.put("==", OperatorSymbol.EQ);
        COMPARATORSYMBOLS.put("!=", OperatorSymbol.NEQ);
        COMPARATORSYMBOLS.put(">",  OperatorSymbol.GT);
        COMPARATORSYMBOLS.put(">=", OperatorSymbol.GTE);
        COMPARATORSYMBOLS.put("<", OperatorSymbol.LT);
        COMPARATORSYMBOLS.put("<=", OperatorSymbol.LTE);
        COMPARATORSYMBOLS.put("=~", OperatorSymbol.REQ);
        COMPARATORSYMBOLS.put("!~", OperatorSymbol.NREQ);
        COMPARATORSYMBOLS.put("in", OperatorSymbol.IN);

        LOGICALSYMBOLS.put("&&", OperatorSymbol.AND);
        LOGICALSYMBOLS.put("||", OperatorSymbol.OR);

        BITWISESYMBOLS.put("^", OperatorSymbol.BITWISE_XOR);
        BITWISESYMBOLS.put(	"&", OperatorSymbol.BITWISE_AND);
        BITWISESYMBOLS.put("|", OperatorSymbol.BITWISE_OR);

        BITWISESHIFTSYMBOLS.put(">>", OperatorSymbol.BITWISE_RSHIFT);
        BITWISESHIFTSYMBOLS.put("<<", OperatorSymbol.BITWISE_LSHIFT);

        ADDITIVESYMBOLS.put("+", OperatorSymbol.PLUS);
        ADDITIVESYMBOLS.put("-", OperatorSymbol.MINUS);

        MULTIPLICATIVESYMBOLS.put("*", OperatorSymbol.MULTIPLY);
        MULTIPLICATIVESYMBOLS.put("/", OperatorSymbol.DIVIDE);
        MULTIPLICATIVESYMBOLS.put("%", OperatorSymbol.MODULUS);

        EXPONENTIALSYMBOLSS.put("**", OperatorSymbol.EXPONENT);

        PREFIXSYMBOLS.put("-", OperatorSymbol.NEGATE);
        PREFIXSYMBOLS.put("!", OperatorSymbol.INVERT);
        PREFIXSYMBOLS.put("~", OperatorSymbol.BITWISE_NOT);

        TERNARYSYMBOLS.put("?", OperatorSymbol.TERNARY_TRUE);
        TERNARYSYMBOLS.put(":", OperatorSymbol.TERNARY_FALSE);
        TERNARYSYMBOLS.put("??", OperatorSymbol.COALESCE);

        MODIFIERSYMBOLS.put("+",  OperatorSymbol.PLUS);
        MODIFIERSYMBOLS.put("-",  OperatorSymbol.MINUS);
        MODIFIERSYMBOLS.put("*",  OperatorSymbol.MULTIPLY);
        MODIFIERSYMBOLS.put("/",  OperatorSymbol.DIVIDE);
        MODIFIERSYMBOLS.put("%",  OperatorSymbol.MODULUS);
        MODIFIERSYMBOLS.put("**", OperatorSymbol.EXPONENT);
        MODIFIERSYMBOLS.put("&",  OperatorSymbol.BITWISE_AND);
        MODIFIERSYMBOLS.put("|",  OperatorSymbol.BITWISE_OR);
        MODIFIERSYMBOLS.put("^",  OperatorSymbol.BITWISE_XOR);
        MODIFIERSYMBOLS.put(">>", OperatorSymbol.BITWISE_RSHIFT);
        MODIFIERSYMBOLS.put("<<", OperatorSymbol.BITWISE_LSHIFT);

        SEPARATORSYMBOLS.put(",", OperatorSymbol.SEPARATE);

        ANDSYMBOLS.put("&&", OperatorSymbol.AND);

        ORSYMBOLS.put("||", OperatorSymbol.OR);
    }
}
