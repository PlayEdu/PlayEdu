# @rc-component/mini-decimal

A mini decimal calculator which only support `add`, `multi` or compare operation for mini bundle size.

[![NPM version][npm-image]][npm-url] [![dumi](https://img.shields.io/badge/docs%20by-dumi-blue?style=flat-square)](https://github.com/umijs/dumi) [![build status][github-actions-image]][github-actions-url] [![Codecov][codecov-image]][codecov-url] [![npm download][download-image]][download-url]

[npm-image]: http://img.shields.io/npm/v/@rc-component/mini-decimal.svg?style=flat-square
[npm-url]: http://npmjs.org/package/@rc-component/mini-decimal
[github-actions-image]: https://github.com/react-component/mini-decimal/workflows/CI/badge.svg
[github-actions-url]: https://github.com/react-component/mini-decimal/actions
[codecov-image]: https://img.shields.io/codecov/c/github/react-component/mini-decimal/master.svg?style=flat-square
[codecov-url]: https://codecov.io/gh/react-component/mini-decimal/branch/master
[download-image]: https://img.shields.io/npm/dm/@rc-component/mini-decimal.svg?style=flat-square
[download-url]: https://npmjs.org/package/@rc-component/mini-decimal

## Development

```bash
npm install
npm test
```

## Usage

```tsx
import getMiniDecimal from '@rc-component/mini-decimal';

// Add
getMiniDecimal('0.1').add('0.2').toString(); // 0.3

// Multi
getMiniDecimal('0.1').multi('0.2').toString(); // 0.02

// Negate
getMiniDecimal('0.1').negate().toString(); // -0.1

// Equal
getMiniDecimal('0.1').equal('0.1'); // true
getMiniDecimal('0.1').equal('0.2'); // false

// Less Equals
getMiniDecimal('0.1').lessEquals('0.2'); // true
getMiniDecimal('0.1').lessEquals('0.1'); // false
```
