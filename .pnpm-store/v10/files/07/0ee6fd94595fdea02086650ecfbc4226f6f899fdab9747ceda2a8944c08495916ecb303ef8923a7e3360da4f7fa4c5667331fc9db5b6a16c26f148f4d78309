(function (global, factory) {
	typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
	typeof define === 'function' && define.amd ? define(['exports'], factory) :
	(global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.matchSorter = {}));
})(this, (function (exports) { 'use strict';

	function getDefaultExportFromCjs (x) {
		return x && x.__esModule && Object.prototype.hasOwnProperty.call(x, 'default') ? x['default'] : x;
	}

	var removeAccents$2 = {exports: {}};

	var characterMap = {
	  "À": "A",
	  "Á": "A",
	  "Â": "A",
	  "Ã": "A",
	  "Ä": "A",
	  "Å": "A",
	  "Ấ": "A",
	  "Ắ": "A",
	  "Ẳ": "A",
	  "Ẵ": "A",
	  "Ặ": "A",
	  "Æ": "AE",
	  "Ầ": "A",
	  "Ằ": "A",
	  "Ȃ": "A",
	  "Ả": "A",
	  "Ạ": "A",
	  "Ẩ": "A",
	  "Ẫ": "A",
	  "Ậ": "A",
	  "Ç": "C",
	  "Ḉ": "C",
	  "È": "E",
	  "É": "E",
	  "Ê": "E",
	  "Ë": "E",
	  "Ế": "E",
	  "Ḗ": "E",
	  "Ề": "E",
	  "Ḕ": "E",
	  "Ḝ": "E",
	  "Ȇ": "E",
	  "Ẻ": "E",
	  "Ẽ": "E",
	  "Ẹ": "E",
	  "Ể": "E",
	  "Ễ": "E",
	  "Ệ": "E",
	  "Ì": "I",
	  "Í": "I",
	  "Î": "I",
	  "Ï": "I",
	  "Ḯ": "I",
	  "Ȋ": "I",
	  "Ỉ": "I",
	  "Ị": "I",
	  "Ð": "D",
	  "Ñ": "N",
	  "Ò": "O",
	  "Ó": "O",
	  "Ô": "O",
	  "Õ": "O",
	  "Ö": "O",
	  "Ø": "O",
	  "Ố": "O",
	  "Ṍ": "O",
	  "Ṓ": "O",
	  "Ȏ": "O",
	  "Ỏ": "O",
	  "Ọ": "O",
	  "Ổ": "O",
	  "Ỗ": "O",
	  "Ộ": "O",
	  "Ờ": "O",
	  "Ở": "O",
	  "Ỡ": "O",
	  "Ớ": "O",
	  "Ợ": "O",
	  "Ù": "U",
	  "Ú": "U",
	  "Û": "U",
	  "Ü": "U",
	  "Ủ": "U",
	  "Ụ": "U",
	  "Ử": "U",
	  "Ữ": "U",
	  "Ự": "U",
	  "Ý": "Y",
	  "à": "a",
	  "á": "a",
	  "â": "a",
	  "ã": "a",
	  "ä": "a",
	  "å": "a",
	  "ấ": "a",
	  "ắ": "a",
	  "ẳ": "a",
	  "ẵ": "a",
	  "ặ": "a",
	  "æ": "ae",
	  "ầ": "a",
	  "ằ": "a",
	  "ȃ": "a",
	  "ả": "a",
	  "ạ": "a",
	  "ẩ": "a",
	  "ẫ": "a",
	  "ậ": "a",
	  "ç": "c",
	  "ḉ": "c",
	  "è": "e",
	  "é": "e",
	  "ê": "e",
	  "ë": "e",
	  "ế": "e",
	  "ḗ": "e",
	  "ề": "e",
	  "ḕ": "e",
	  "ḝ": "e",
	  "ȇ": "e",
	  "ẻ": "e",
	  "ẽ": "e",
	  "ẹ": "e",
	  "ể": "e",
	  "ễ": "e",
	  "ệ": "e",
	  "ì": "i",
	  "í": "i",
	  "î": "i",
	  "ï": "i",
	  "ḯ": "i",
	  "ȋ": "i",
	  "ỉ": "i",
	  "ị": "i",
	  "ð": "d",
	  "ñ": "n",
	  "ò": "o",
	  "ó": "o",
	  "ô": "o",
	  "õ": "o",
	  "ö": "o",
	  "ø": "o",
	  "ố": "o",
	  "ṍ": "o",
	  "ṓ": "o",
	  "ȏ": "o",
	  "ỏ": "o",
	  "ọ": "o",
	  "ổ": "o",
	  "ỗ": "o",
	  "ộ": "o",
	  "ờ": "o",
	  "ở": "o",
	  "ỡ": "o",
	  "ớ": "o",
	  "ợ": "o",
	  "ù": "u",
	  "ú": "u",
	  "û": "u",
	  "ü": "u",
	  "ủ": "u",
	  "ụ": "u",
	  "ử": "u",
	  "ữ": "u",
	  "ự": "u",
	  "ý": "y",
	  "ÿ": "y",
	  "Ā": "A",
	  "ā": "a",
	  "Ă": "A",
	  "ă": "a",
	  "Ą": "A",
	  "ą": "a",
	  "Ć": "C",
	  "ć": "c",
	  "Ĉ": "C",
	  "ĉ": "c",
	  "Ċ": "C",
	  "ċ": "c",
	  "Č": "C",
	  "č": "c",
	  "C̆": "C",
	  "c̆": "c",
	  "Ď": "D",
	  "ď": "d",
	  "Đ": "D",
	  "đ": "d",
	  "Ē": "E",
	  "ē": "e",
	  "Ĕ": "E",
	  "ĕ": "e",
	  "Ė": "E",
	  "ė": "e",
	  "Ę": "E",
	  "ę": "e",
	  "Ě": "E",
	  "ě": "e",
	  "Ĝ": "G",
	  "Ǵ": "G",
	  "ĝ": "g",
	  "ǵ": "g",
	  "Ğ": "G",
	  "ğ": "g",
	  "Ġ": "G",
	  "ġ": "g",
	  "Ģ": "G",
	  "ģ": "g",
	  "Ĥ": "H",
	  "ĥ": "h",
	  "Ħ": "H",
	  "ħ": "h",
	  "Ḫ": "H",
	  "ḫ": "h",
	  "Ĩ": "I",
	  "ĩ": "i",
	  "Ī": "I",
	  "ī": "i",
	  "Ĭ": "I",
	  "ĭ": "i",
	  "Į": "I",
	  "į": "i",
	  "İ": "I",
	  "ı": "i",
	  "Ĳ": "IJ",
	  "ĳ": "ij",
	  "Ĵ": "J",
	  "ĵ": "j",
	  "Ķ": "K",
	  "ķ": "k",
	  "Ḱ": "K",
	  "ḱ": "k",
	  "K̆": "K",
	  "k̆": "k",
	  "Ĺ": "L",
	  "ĺ": "l",
	  "Ļ": "L",
	  "ļ": "l",
	  "Ľ": "L",
	  "ľ": "l",
	  "Ŀ": "L",
	  "ŀ": "l",
	  "Ł": "l",
	  "ł": "l",
	  "Ḿ": "M",
	  "ḿ": "m",
	  "M̆": "M",
	  "m̆": "m",
	  "Ń": "N",
	  "ń": "n",
	  "Ņ": "N",
	  "ņ": "n",
	  "Ň": "N",
	  "ň": "n",
	  "ŉ": "n",
	  "N̆": "N",
	  "n̆": "n",
	  "Ō": "O",
	  "ō": "o",
	  "Ŏ": "O",
	  "ŏ": "o",
	  "Ő": "O",
	  "ő": "o",
	  "Œ": "OE",
	  "œ": "oe",
	  "P̆": "P",
	  "p̆": "p",
	  "Ŕ": "R",
	  "ŕ": "r",
	  "Ŗ": "R",
	  "ŗ": "r",
	  "Ř": "R",
	  "ř": "r",
	  "R̆": "R",
	  "r̆": "r",
	  "Ȓ": "R",
	  "ȓ": "r",
	  "Ś": "S",
	  "ś": "s",
	  "Ŝ": "S",
	  "ŝ": "s",
	  "Ş": "S",
	  "Ș": "S",
	  "ș": "s",
	  "ş": "s",
	  "Š": "S",
	  "š": "s",
	  "Ţ": "T",
	  "ţ": "t",
	  "ț": "t",
	  "Ț": "T",
	  "Ť": "T",
	  "ť": "t",
	  "Ŧ": "T",
	  "ŧ": "t",
	  "T̆": "T",
	  "t̆": "t",
	  "Ũ": "U",
	  "ũ": "u",
	  "Ū": "U",
	  "ū": "u",
	  "Ŭ": "U",
	  "ŭ": "u",
	  "Ů": "U",
	  "ů": "u",
	  "Ű": "U",
	  "ű": "u",
	  "Ų": "U",
	  "ų": "u",
	  "Ȗ": "U",
	  "ȗ": "u",
	  "V̆": "V",
	  "v̆": "v",
	  "Ŵ": "W",
	  "ŵ": "w",
	  "Ẃ": "W",
	  "ẃ": "w",
	  "X̆": "X",
	  "x̆": "x",
	  "Ŷ": "Y",
	  "ŷ": "y",
	  "Ÿ": "Y",
	  "Y̆": "Y",
	  "y̆": "y",
	  "Ź": "Z",
	  "ź": "z",
	  "Ż": "Z",
	  "ż": "z",
	  "Ž": "Z",
	  "ž": "z",
	  "ſ": "s",
	  "ƒ": "f",
	  "Ơ": "O",
	  "ơ": "o",
	  "Ư": "U",
	  "ư": "u",
	  "Ǎ": "A",
	  "ǎ": "a",
	  "Ǐ": "I",
	  "ǐ": "i",
	  "Ǒ": "O",
	  "ǒ": "o",
	  "Ǔ": "U",
	  "ǔ": "u",
	  "Ǖ": "U",
	  "ǖ": "u",
	  "Ǘ": "U",
	  "ǘ": "u",
	  "Ǚ": "U",
	  "ǚ": "u",
	  "Ǜ": "U",
	  "ǜ": "u",
	  "Ứ": "U",
	  "ứ": "u",
	  "Ṹ": "U",
	  "ṹ": "u",
	  "Ǻ": "A",
	  "ǻ": "a",
	  "Ǽ": "AE",
	  "ǽ": "ae",
	  "Ǿ": "O",
	  "ǿ": "o",
	  "Þ": "TH",
	  "þ": "th",
	  "Ṕ": "P",
	  "ṕ": "p",
	  "Ṥ": "S",
	  "ṥ": "s",
	  "X́": "X",
	  "x́": "x",
	  "Ѓ": "Г",
	  "ѓ": "г",
	  "Ќ": "К",
	  "ќ": "к",
	  "A̋": "A",
	  "a̋": "a",
	  "E̋": "E",
	  "e̋": "e",
	  "I̋": "I",
	  "i̋": "i",
	  "Ǹ": "N",
	  "ǹ": "n",
	  "Ồ": "O",
	  "ồ": "o",
	  "Ṑ": "O",
	  "ṑ": "o",
	  "Ừ": "U",
	  "ừ": "u",
	  "Ẁ": "W",
	  "ẁ": "w",
	  "Ỳ": "Y",
	  "ỳ": "y",
	  "Ȁ": "A",
	  "ȁ": "a",
	  "Ȅ": "E",
	  "ȅ": "e",
	  "Ȉ": "I",
	  "ȉ": "i",
	  "Ȍ": "O",
	  "ȍ": "o",
	  "Ȑ": "R",
	  "ȑ": "r",
	  "Ȕ": "U",
	  "ȕ": "u",
	  "B̌": "B",
	  "b̌": "b",
	  "Č̣": "C",
	  "č̣": "c",
	  "Ê̌": "E",
	  "ê̌": "e",
	  "F̌": "F",
	  "f̌": "f",
	  "Ǧ": "G",
	  "ǧ": "g",
	  "Ȟ": "H",
	  "ȟ": "h",
	  "J̌": "J",
	  "ǰ": "j",
	  "Ǩ": "K",
	  "ǩ": "k",
	  "M̌": "M",
	  "m̌": "m",
	  "P̌": "P",
	  "p̌": "p",
	  "Q̌": "Q",
	  "q̌": "q",
	  "Ř̩": "R",
	  "ř̩": "r",
	  "Ṧ": "S",
	  "ṧ": "s",
	  "V̌": "V",
	  "v̌": "v",
	  "W̌": "W",
	  "w̌": "w",
	  "X̌": "X",
	  "x̌": "x",
	  "Y̌": "Y",
	  "y̌": "y",
	  "A̧": "A",
	  "a̧": "a",
	  "B̧": "B",
	  "b̧": "b",
	  "Ḑ": "D",
	  "ḑ": "d",
	  "Ȩ": "E",
	  "ȩ": "e",
	  "Ɛ̧": "E",
	  "ɛ̧": "e",
	  "Ḩ": "H",
	  "ḩ": "h",
	  "I̧": "I",
	  "i̧": "i",
	  "Ɨ̧": "I",
	  "ɨ̧": "i",
	  "M̧": "M",
	  "m̧": "m",
	  "O̧": "O",
	  "o̧": "o",
	  "Q̧": "Q",
	  "q̧": "q",
	  "U̧": "U",
	  "u̧": "u",
	  "X̧": "X",
	  "x̧": "x",
	  "Z̧": "Z",
	  "z̧": "z",
	  "й": "и",
	  "Й": "И",
	  "ё": "е",
	  "Ё": "Е"
	};
	var chars = Object.keys(characterMap).join('|');
	var allAccents = new RegExp(chars, 'g');
	var firstAccent = new RegExp(chars, '');
	function matcher(match) {
	  return characterMap[match];
	}
	var removeAccents = function (string) {
	  return string.replace(allAccents, matcher);
	};
	var hasAccents = function (string) {
	  return !!string.match(firstAccent);
	};
	removeAccents$2.exports = removeAccents;
	removeAccents$2.exports.has = hasAccents;
	removeAccents$2.exports.remove = removeAccents;
	var removeAccentsExports = removeAccents$2.exports;
	var removeAccents$1 = /*@__PURE__*/getDefaultExportFromCjs(removeAccentsExports);

	/**
	 * @name match-sorter
	 * @license MIT license.
	 * @copyright (c) 2020 Kent C. Dodds
	 * @author Kent C. Dodds <me@kentcdodds.com> (https://kentcdodds.com)
	 */
	const rankings = {
	  CASE_SENSITIVE_EQUAL: 7,
	  EQUAL: 6,
	  STARTS_WITH: 5,
	  WORD_STARTS_WITH: 4,
	  CONTAINS: 3,
	  ACRONYM: 2,
	  MATCHES: 1,
	  NO_MATCH: 0
	};
	const defaultBaseSortFn = (a, b) => String(a.rankedValue).localeCompare(String(b.rankedValue));

	/**
	 * Takes an array of items and a value and returns a new array with the items that match the given value
	 * @param {Array} items - the items to sort
	 * @param {String} value - the value to use for ranking
	 * @param {Object} options - Some options to configure the sorter
	 * @return {Array} - the new sorted array
	 */
	function matchSorter(items, value, options) {
	  if (options === void 0) {
	    options = {};
	  }
	  const {
	    keys,
	    threshold = rankings.MATCHES,
	    baseSort = defaultBaseSortFn,
	    sorter = matchedItems => matchedItems.sort((a, b) => sortRankedValues(a, b, baseSort))
	  } = options;
	  const matchedItems = items.reduce(reduceItemsToRanked, []);
	  return sorter(matchedItems).map(_ref => {
	    let {
	      item
	    } = _ref;
	    return item;
	  });
	  function reduceItemsToRanked(matches, item, index) {
	    const rankingInfo = getHighestRanking(item, keys, value, options);
	    const {
	      rank,
	      keyThreshold = threshold
	    } = rankingInfo;
	    if (rank >= keyThreshold) {
	      matches.push({
	        ...rankingInfo,
	        item,
	        index
	      });
	    }
	    return matches;
	  }
	}
	matchSorter.rankings = rankings;

	/**
	 * Gets the highest ranking for value for the given item based on its values for the given keys
	 * @param {*} item - the item to rank
	 * @param {Array} keys - the keys to get values from the item for the ranking
	 * @param {String} value - the value to rank against
	 * @param {Object} options - options to control the ranking
	 * @return {{rank: Number, keyIndex: Number, keyThreshold: Number}} - the highest ranking
	 */
	function getHighestRanking(item, keys, value, options) {
	  if (!keys) {
	    // if keys is not specified, then we assume the item given is ready to be matched
	    const stringItem = item;
	    return {
	      // ends up being duplicate of 'item' in matches but consistent
	      rankedValue: stringItem,
	      rank: getMatchRanking(stringItem, value, options),
	      keyIndex: -1,
	      keyThreshold: options.threshold
	    };
	  }
	  const valuesToRank = getAllValuesToRank(item, keys);
	  return valuesToRank.reduce((_ref2, _ref3, i) => {
	    let {
	      rank,
	      rankedValue,
	      keyIndex,
	      keyThreshold
	    } = _ref2;
	    let {
	      itemValue,
	      attributes
	    } = _ref3;
	    let newRank = getMatchRanking(itemValue, value, options);
	    let newRankedValue = rankedValue;
	    const {
	      minRanking,
	      maxRanking,
	      threshold
	    } = attributes;
	    if (newRank < minRanking && newRank >= rankings.MATCHES) {
	      newRank = minRanking;
	    } else if (newRank > maxRanking) {
	      newRank = maxRanking;
	    }
	    if (newRank > rank) {
	      rank = newRank;
	      keyIndex = i;
	      keyThreshold = threshold;
	      newRankedValue = itemValue;
	    }
	    return {
	      rankedValue: newRankedValue,
	      rank,
	      keyIndex,
	      keyThreshold
	    };
	  }, {
	    rankedValue: item,
	    rank: rankings.NO_MATCH,
	    keyIndex: -1,
	    keyThreshold: options.threshold
	  });
	}

	/**
	 * Gives a rankings score based on how well the two strings match.
	 * @param {String} testString - the string to test against
	 * @param {String} stringToRank - the string to rank
	 * @param {Object} options - options for the match (like keepDiacritics for comparison)
	 * @returns {Number} the ranking for how well stringToRank matches testString
	 */
	function getMatchRanking(testString, stringToRank, options) {
	  testString = prepareValueForComparison(testString, options);
	  stringToRank = prepareValueForComparison(stringToRank, options);

	  // too long
	  if (stringToRank.length > testString.length) {
	    return rankings.NO_MATCH;
	  }

	  // case sensitive equals
	  if (testString === stringToRank) {
	    return rankings.CASE_SENSITIVE_EQUAL;
	  }

	  // Lower casing before further comparison
	  testString = testString.toLowerCase();
	  stringToRank = stringToRank.toLowerCase();

	  // case insensitive equals
	  if (testString === stringToRank) {
	    return rankings.EQUAL;
	  }

	  // starts with
	  if (testString.startsWith(stringToRank)) {
	    return rankings.STARTS_WITH;
	  }

	  // word starts with
	  if (testString.includes(` ${stringToRank}`)) {
	    return rankings.WORD_STARTS_WITH;
	  }

	  // contains
	  if (testString.includes(stringToRank)) {
	    return rankings.CONTAINS;
	  } else if (stringToRank.length === 1) {
	    // If the only character in the given stringToRank
	    //   isn't even contained in the testString, then
	    //   it's definitely not a match.
	    return rankings.NO_MATCH;
	  }

	  // acronym
	  if (getAcronym(testString).includes(stringToRank)) {
	    return rankings.ACRONYM;
	  }

	  // will return a number between rankings.MATCHES and
	  // rankings.MATCHES + 1 depending  on how close of a match it is.
	  return getClosenessRanking(testString, stringToRank);
	}

	/**
	 * Generates an acronym for a string.
	 *
	 * @param {String} string the string for which to produce the acronym
	 * @returns {String} the acronym
	 */
	function getAcronym(string) {
	  let acronym = '';
	  const wordsInString = string.split(' ');
	  wordsInString.forEach(wordInString => {
	    const splitByHyphenWords = wordInString.split('-');
	    splitByHyphenWords.forEach(splitByHyphenWord => {
	      acronym += splitByHyphenWord.substr(0, 1);
	    });
	  });
	  return acronym;
	}

	/**
	 * Returns a score based on how spread apart the
	 * characters from the stringToRank are within the testString.
	 * A number close to rankings.MATCHES represents a loose match. A number close
	 * to rankings.MATCHES + 1 represents a tighter match.
	 * @param {String} testString - the string to test against
	 * @param {String} stringToRank - the string to rank
	 * @returns {Number} the number between rankings.MATCHES and
	 * rankings.MATCHES + 1 for how well stringToRank matches testString
	 */
	function getClosenessRanking(testString, stringToRank) {
	  let matchingInOrderCharCount = 0;
	  function findMatchingCharacter(matchChar, string, index) {
	    for (let j = index, J = string.length; j < J; j++) {
	      const stringChar = string[j];
	      if (stringChar === matchChar) {
	        matchingInOrderCharCount += 1;
	        return j + 1;
	      }
	    }
	    return -1;
	  }
	  let skipped = 0;
	  function getRanking(spread) {
	    const spreadPercentage = 1 / spread;
	    const inOrderPercentage = matchingInOrderCharCount / stringToRank.length;
	    const matchPercentage = (stringToRank.length - skipped) / stringToRank.length;
	    const ranking = rankings.MATCHES + inOrderPercentage * spreadPercentage * matchPercentage;
	    return ranking;
	  }
	  let firstIndex = 0;
	  let charNumber = 0;
	  let nextCharNumber = 0;
	  for (let i = 0, I = stringToRank.length; i < I; i++) {
	    const matchChar = stringToRank[i];
	    nextCharNumber = findMatchingCharacter(matchChar, testString, charNumber);
	    const found = nextCharNumber > -1;
	    if (found) {
	      charNumber = nextCharNumber;
	      if (i === 0) {
	        firstIndex = charNumber;
	      }
	    } else if (skipped > 0 || stringToRank.length <= 3) {
	      // if search term is short, require finding all characters
	      return rankings.NO_MATCH;
	    } else {
	      skipped += 1;
	    }
	  }
	  const spread = charNumber - firstIndex;
	  return getRanking(spread);
	}

	/**
	 * Sorts items that have a rank, index, and keyIndex
	 * @param {Object} a - the first item to sort
	 * @param {Object} b - the second item to sort
	 * @return {Number} -1 if a should come first, 1 if b should come first, 0 if equal
	 */
	function sortRankedValues(a, b, baseSort) {
	  const aFirst = -1;
	  const bFirst = 1;
	  const {
	    rank: aRank,
	    keyIndex: aKeyIndex
	  } = a;
	  const {
	    rank: bRank,
	    keyIndex: bKeyIndex
	  } = b;
	  const same = aRank === bRank;
	  if (same) {
	    if (aKeyIndex === bKeyIndex) {
	      // use the base sort function as a tie-breaker
	      return baseSort(a, b);
	    } else {
	      return aKeyIndex < bKeyIndex ? aFirst : bFirst;
	    }
	  } else {
	    return aRank > bRank ? aFirst : bFirst;
	  }
	}

	/**
	 * Prepares value for comparison by stringifying it, removing diacritics (if specified)
	 * @param {String} value - the value to clean
	 * @param {Object} options - {keepDiacritics: whether to remove diacritics}
	 * @return {String} the prepared value
	 */
	function prepareValueForComparison(value, _ref4) {
	  let {
	    keepDiacritics
	  } = _ref4;
	  // value might not actually be a string at this point (we don't get to choose)
	  // so part of preparing the value for comparison is ensure that it is a string
	  value = `${value}`; // toString
	  if (!keepDiacritics) {
	    value = removeAccents$1(value);
	  }
	  return value;
	}

	/**
	 * Gets value for key in item at arbitrarily nested keypath
	 * @param {Object} item - the item
	 * @param {Object|Function} key - the potentially nested keypath or property callback
	 * @return {Array} - an array containing the value(s) at the nested keypath
	 */
	function getItemValues(item, key) {
	  if (typeof key === 'object') {
	    key = key.key;
	  }
	  let value;
	  if (typeof key === 'function') {
	    value = key(item);
	  } else if (item == null) {
	    value = null;
	  } else if (Object.hasOwnProperty.call(item, key)) {
	    value = item[key];
	  } else if (key.includes('.')) {
	    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
	    return getNestedValues(key, item);
	  } else {
	    value = null;
	  }

	  // because `value` can also be undefined
	  if (value == null) {
	    return [];
	  }
	  if (Array.isArray(value)) {
	    return value;
	  }
	  return [String(value)];
	}

	/**
	 * Given path: "foo.bar.baz"
	 * And item: {foo: {bar: {baz: 'buzz'}}}
	 *   -> 'buzz'
	 * @param path a dot-separated set of keys
	 * @param item the item to get the value from
	 */
	function getNestedValues(path, item) {
	  const keys = path.split('.');
	  let values = [item];
	  for (let i = 0, I = keys.length; i < I; i++) {
	    const nestedKey = keys[i];
	    let nestedValues = [];
	    for (let j = 0, J = values.length; j < J; j++) {
	      const nestedItem = values[j];
	      if (nestedItem == null) continue;
	      if (Object.hasOwnProperty.call(nestedItem, nestedKey)) {
	        const nestedValue = nestedItem[nestedKey];
	        if (nestedValue != null) {
	          nestedValues.push(nestedValue);
	        }
	      } else if (nestedKey === '*') {
	        // ensure that values is an array
	        nestedValues = nestedValues.concat(nestedItem);
	      }
	    }
	    values = nestedValues;
	  }
	  if (Array.isArray(values[0])) {
	    // keep allowing the implicit wildcard for an array of strings at the end of
	    // the path; don't use `.flat()` because that's not available in node.js v10
	    const result = [];
	    return result.concat(...values);
	  }
	  // Based on our logic it should be an array of strings by now...
	  // assuming the user's path terminated in strings
	  return values;
	}

	/**
	 * Gets all the values for the given keys in the given item and returns an array of those values
	 * @param item - the item from which the values will be retrieved
	 * @param keys - the keys to use to retrieve the values
	 * @return objects with {itemValue, attributes}
	 */
	function getAllValuesToRank(item, keys) {
	  const allValues = [];
	  for (let j = 0, J = keys.length; j < J; j++) {
	    const key = keys[j];
	    const attributes = getKeyAttributes(key);
	    const itemValues = getItemValues(item, key);
	    for (let i = 0, I = itemValues.length; i < I; i++) {
	      allValues.push({
	        itemValue: itemValues[i],
	        attributes
	      });
	    }
	  }
	  return allValues;
	}
	const defaultKeyAttributes = {
	  maxRanking: Infinity,
	  minRanking: -Infinity
	};
	/**
	 * Gets all the attributes for the given key
	 * @param key - the key from which the attributes will be retrieved
	 * @return object containing the key's attributes
	 */
	function getKeyAttributes(key) {
	  if (typeof key === 'string') {
	    return defaultKeyAttributes;
	  }
	  return {
	    ...defaultKeyAttributes,
	    ...key
	  };
	}

	/*
	eslint
	  no-continue: "off",
	*/

	exports.defaultBaseSortFn = defaultBaseSortFn;
	exports.matchSorter = matchSorter;
	exports.rankings = rankings;

}));
//# sourceMappingURL=match-sorter.umd.js.map
