# rollup-plugin-gzip

![ci status](https://github.com/kryops/rollup-plugin-gzip/workflows/CI/badge.svg)

Creates a compressed `.gz` / `.br` artifact for your Rollup / Vite bundle.

**COMPATIBILITY NOTE**: This version is compatible with rollup >= 2.0.0

- For rollup >= 0.60 - 1.x, use version 2.x of this plugin
- For older versions of rollup, use version 1.x of this plugin

## Installation

```
npm install --save-dev rollup-plugin-gzip
```

## Usage

```js
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

rollup({
  input: 'src/index.js',
  plugins: [gzipPlugin()],
}).then(/* ... */)
```

> **NOTE**: This plugin is an ES module. If you import it using `require()`, you have to point to the default export via `require('rollup-plugin-gzip').default`

### Configuration

**filter** `RegExp | (fileName: string) => boolean`

Control which of the output files to compress.

Defaults to `/\.(js|mjs|cjs|json|css|html|wasm|svg)$/`

**gzipOptions** `object`

GZIP compression options, see https://nodejs.org/api/zlib.html#zlib_class_options

**minSize** `number`

Specified the minimum size in Bytes for a file to get compressed. Files that are smaller than this threshold will not be compressed. This does not apply to the files specified through `additionalFiles`!

**additionalFiles** `string[]`

This option allows you to compress additional files outside of the main rollup bundling process.

**additionalFilesDelay** `number`

This options sets a delay (ms) before the plugin compresses the files specified through `additionalFiles`.

Defaults to `0` for Rollup >= 2.0.0, `2000` for older versions of Rollup

**customCompression** `(content: string | Buffer) => string | Buffer | Promise<string | Buffer>`

Set a custom compression algorithm. The function can either return the compressed contents synchronously, or otherwise return a promise for asynchronous processing.

**fileName** `string | (fileName: string) => string`

Set a custom file name convention for the compressed files. Can be a suffix string or a function returning the file name.

Defaults to `".gz"`

## Examples

> NOTE: These examples use Rollup's JavaScript API. For Rollup configuration file examples, check out the `/examples` directory.

### Brotli Compression

Since Node 11.7.0 you can use Node's built-in Brotli compression:

```ts
import { brotliCompress } from 'zlib'
import { promisify } from 'util'
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

const brotliPromise = promisify(brotliCompress)

rollup({
  input: 'src/index.js',
  plugins: [
    gzipPlugin({
      customCompression: content => brotliPromise(Buffer.from(content)),
      fileName: '.br',
    }),
  ],
}).then(/* ... */)
```

For Node < 11.7.0 you need the external [`brotli`](https://www.npmjs.com/package/brotli) module:

```ts
import { compress } from 'brotli'
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

rollup({
  input: 'src/index.js',
  plugins: [
    gzipPlugin({
      customCompression: content => compress(Buffer.from(content)),
      fileName: '.br',
    }),
  ],
}).then(/* ... */)
```

### Zopfli Compression

Zopfli support is available through several different external packages, each of which comes with advantages and disadvantages:

- [`node-zopfli`](https://www.npmjs.com/package/node-zopfli) (or [`node-zopfli-es`](https://www.npmjs.com/package/node-zopfli-es)) - native version, longer installation time, might require build tooling

```ts
import { gzipSync } from 'node-zopfli'
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

rollup({
  input: 'src/index.js',
  plugins: [
    gzipPlugin({
      customCompression: content => gzipSync(Buffer.from(content)),
    }),
  ],
}).then(/* ... */)
```

- [`@gfx/zopfli`](https://www.npmjs.com/package/@gfx/zopfli) - WebAssembly version, faster installation, slower compression

```ts
import { gzipAsync } from '@gfx/zopfli'
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

rollup({
  input: 'src/index.js',
  plugins: [
    gzipPlugin({
      customCompression: content =>
        gzipAsync(Buffer.from(content), { numiterations: 15 }),
    }),
  ],
}).then(/* ... */)
```

### Compressing into multiple formats

To support compressing your bundle into multiple different formats, you can add this plugin multiple times with different configurations:

```ts
import { brotliCompress } from 'zlib'
import { promisify } from 'util'
import { rollup } from 'rollup'
import gzipPlugin from 'rollup-plugin-gzip'

const brotliPromise = promisify(brotliCompress)

rollup({
  input: 'src/index.js',
  plugins: [
    // GZIP compression as .gz files
    gzipPlugin(),
    // Brotil compression as .br files
    gzipPlugin({
      customCompression: content => brotliPromise(Buffer.from(content)),
      fileName: '.br',
    }),
  ],
}).then(/* ... */)
```

## License

MIT
