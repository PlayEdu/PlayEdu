import _slicedToArray from "@babel/runtime/helpers/esm/slicedToArray";
import { supportBigInt } from "./supportUtil";
export function isEmpty(value) {
  return !value && value !== 0 && !Number.isNaN(value) || !String(value).trim();
}

/**
 * Format string number to readable number
 */
export function trimNumber(numStr) {
  var str = numStr.trim();
  var negative = str.startsWith('-');
  if (negative) {
    str = str.slice(1);
  }
  str = str
  // Remove decimal 0. `1.000` => `1.`, `1.100` => `1.1`
  .replace(/(\.\d*[^0])0*$/, '$1')
  // Remove useless decimal. `1.` => `1`
  .replace(/\.0*$/, '')
  // Remove integer 0. `0001` => `1`, 000.1' => `.1`
  .replace(/^0+/, '');
  if (str.startsWith('.')) {
    str = "0".concat(str);
  }
  var trimStr = str || '0';
  var splitNumber = trimStr.split('.');
  var integerStr = splitNumber[0] || '0';
  var decimalStr = splitNumber[1] || '0';
  if (integerStr === '0' && decimalStr === '0') {
    negative = false;
  }
  var negativeStr = negative ? '-' : '';
  return {
    negative: negative,
    negativeStr: negativeStr,
    trimStr: trimStr,
    integerStr: integerStr,
    decimalStr: decimalStr,
    fullStr: "".concat(negativeStr).concat(trimStr)
  };
}
export function isE(number) {
  var str = String(number);
  return !Number.isNaN(Number(str)) && str.includes('e');
}
/**
 * Parse a scientific-notation string into reusable parts.
 *
 * The idea is to split the value into mantissa and exponent first, then
 * normalize the mantissa into sign, integer/decimal segments, and a compact
 * digit sequence so later logic can move the decimal point without re-parsing.
 */
function parseScientificNotation(numStr) {
  var _numStr$toLowerCase$s = numStr.toLowerCase().split('e'),
    _numStr$toLowerCase$s2 = _slicedToArray(_numStr$toLowerCase$s, 2),
    mantissa = _numStr$toLowerCase$s2[0],
    _numStr$toLowerCase$s3 = _numStr$toLowerCase$s2[1],
    exponent = _numStr$toLowerCase$s3 === void 0 ? '0' : _numStr$toLowerCase$s3;
  var negative = mantissa.startsWith('-');
  var unsignedMantissa = negative ? mantissa.slice(1) : mantissa;
  var _unsignedMantissa$spl = unsignedMantissa.split('.'),
    _unsignedMantissa$spl2 = _slicedToArray(_unsignedMantissa$spl, 2),
    _unsignedMantissa$spl3 = _unsignedMantissa$spl2[0],
    integer = _unsignedMantissa$spl3 === void 0 ? '0' : _unsignedMantissa$spl3,
    _unsignedMantissa$spl4 = _unsignedMantissa$spl2[1],
    decimal = _unsignedMantissa$spl4 === void 0 ? '' : _unsignedMantissa$spl4;
  var digits = "".concat(integer).concat(decimal).replace(/^0+/, '') || '0';
  return {
    decimal: decimal,
    digits: digits,
    exponent: Number(exponent),
    integer: integer,
    negative: negative
  };
}

/**
 * Expand parsed scientific notation into a plain decimal string.
 *
 * The core idea is to calculate where the decimal point lands after applying
 * the exponent, then rebuild the string by either padding zeros or inserting
 * the decimal point inside the normalized digit sequence.
 */
function expandScientificNotation(parsed) {
  var decimal = parsed.decimal,
    digits = parsed.digits,
    exponent = parsed.exponent,
    integer = parsed.integer,
    negative = parsed.negative;
  if (digits === '0') {
    return '0';
  }
  var integerDigits = integer.replace(/^0+/, '').length;
  var leadingDecimalZeros = (decimal.match(/^0*/) || [''])[0].length;
  var initialDecimalIndex = integerDigits || -leadingDecimalZeros;
  var decimalIndex = initialDecimalIndex + exponent;
  var expanded = '';
  if (decimalIndex <= 0) {
    expanded = "0.".concat('0'.repeat(-decimalIndex)).concat(digits);
  } else if (decimalIndex >= digits.length) {
    expanded = "".concat(digits).concat('0'.repeat(decimalIndex - digits.length));
  } else {
    expanded = "".concat(digits.slice(0, decimalIndex), ".").concat(digits.slice(decimalIndex));
  }
  return "".concat(negative ? '-' : '').concat(expanded);
}
function getScientificPrecision(parsed) {
  if (parsed.exponent >= 0) {
    return Math.max(0, parsed.decimal.length - parsed.exponent);
  }
  return Math.abs(parsed.exponent) + parsed.decimal.length;
}

/**
 * [Legacy] Convert 1e-9 to 0.000000001.
 * This may lose some precision if user really want 1e-9.
 */
export function getNumberPrecision(number) {
  var numStr = String(number);
  if (isE(number)) {
    return getScientificPrecision(parseScientificNotation(numStr));
  }
  return numStr.includes('.') && validateNumber(numStr) ? numStr.length - numStr.indexOf('.') - 1 : 0;
}

/**
 * Convert number (includes scientific notation) to -xxx.yyy format
 */
export function num2str(number) {
  var numStr = String(number);
  if (isE(number)) {
    if (number > Number.MAX_SAFE_INTEGER) {
      return String(supportBigInt() ? BigInt(number).toString() : Number.MAX_SAFE_INTEGER);
    }
    if (number < Number.MIN_SAFE_INTEGER) {
      return String(supportBigInt() ? BigInt(number).toString() : Number.MIN_SAFE_INTEGER);
    }
    var parsed = parseScientificNotation(numStr);
    var precision = getScientificPrecision(parsed);
    numStr = precision > 100 ? expandScientificNotation(parsed) : number.toFixed(precision);
  }
  return trimNumber(numStr).fullStr;
}
export function validateNumber(num) {
  if (typeof num === 'number') {
    return !Number.isNaN(num);
  }

  // Empty
  if (!num) {
    return false;
  }
  return (
    // Normal type: 11.28
    /^\s*-?\d+(\.\d+)?\s*$/.test(num) ||
    // Pre-number: 1.
    /^\s*-?\d+\.\s*$/.test(num) ||
    // Post-number: .1
    /^\s*-?\.\d+\s*$/.test(num)
  );
}