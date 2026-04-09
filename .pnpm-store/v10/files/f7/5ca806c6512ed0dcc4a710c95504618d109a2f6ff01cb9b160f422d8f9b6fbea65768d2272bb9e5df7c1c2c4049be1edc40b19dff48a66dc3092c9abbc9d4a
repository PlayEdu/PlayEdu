# remove-accents

Removes the accents from a string, converting them to their corresponding non-accented ASCII characters.

```
npm install remove-accents
```

[![Unit tests](https://github.com/tyxla/remove-accents/actions/workflows/unit-tests.yml/badge.svg)](https://github.com/tyxla/remove-accents/actions/workflows/unit-tests.yml)

## About

An easy to use solution for converting all accented characters to their corresponding non-accented ASCII characters.

## Syntax

``` js
import removeAccents from 'remove-accents';

removeAccents(inputString)
```

Alternatively, you could use the CommonJS syntax to import it:

``` js
const removeAccents = require('remove-accents');
```

#### inputString

The string that you wish to remove accents from.

## Usage

Call `removeAccents()` by passing the string you wish to remove accents from, and you will get the non-accented string as result.

``` js
const input = 'ÀÁÂÃÄÅ';
const output = removeAccents(input);

console.log(output); // AAAAAA
```

## Methods

The exported function also has helper methods.

#### has

Determine if a string has any accented characters.

``` js
import removeAccents from 'remove-accents';

console.log(removeAccents.has('ÀÁÂÃÄÅ')); // true
console.log(removeAccents.has('ABC'));    // false
```

#### remove

Alias of `removeAccents`.

``` js
import removeAccents from 'remove-accents';

console.log(removeAccents.remove('ÀÁÂÃÄÅ')); // AAAAAA
```

## License

MIT
