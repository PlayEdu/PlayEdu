"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const fs = require("fs");
const path_1 = require("path");
const util_1 = require("util");
const zlib_1 = require("zlib");
const rollup_1 = require("rollup");
const readFile = (0, util_1.promisify)(fs.readFile);
const writeFile = (0, util_1.promisify)(fs.writeFile);
const gzipPromise = (0, util_1.promisify)(zlib_1.gzip);
const isFunction = (arg) => typeof arg === 'function';
const isRegExp = (arg) => Object.prototype.toString.call(arg) === '[object RegExp]';
// functionality partially copied from rollup
/**
 * copied from https://github.com/rollup/rollup/blob/master/src/rollup/index.ts#L450
 */
function isOutputChunk(file) {
    return typeof file.code === 'string';
}
/**
 * Gets the string/buffer content from a file object.
 * Important for adding source map comments
 *
 * Copied partially from rollup.writeOutputFile
 * https://github.com/rollup/rollup/blob/master/src/rollup/index.ts#L454
 */
function getOutputFileContent(outputFileName, outputFile, outputOptions) {
    if (isOutputChunk(outputFile)) {
        let source;
        source = outputFile.code;
        if ((outputOptions.sourcemap === true ||
            outputOptions.sourcemap === 'inline') &&
            outputFile.map) {
            const url = outputOptions.sourcemap === 'inline'
                ? outputFile.map.toUrl()
                : `${(0, path_1.basename)(outputFileName)}.map`;
            // https://github.com/rollup/rollup/blob/master/src/utils/sourceMappingURL.ts#L1
            const sourceMapComment = `//# source` + `MappingURL=${url}\n`;
            // Rollup >= 3.0.0 already includes the comment, older versions do not
            if (!source.includes(sourceMapComment)) {
                source += sourceMapComment;
            }
        }
        return source;
    }
    else {
        return typeof outputFile.source === 'string'
            ? outputFile.source
            : // just to be sure, as it is typed string | Uint8Array in rollup 2.0.0
                Buffer.from(outputFile.source);
    }
}
// actual plugin code
function performInitChecks(options) {
    if (rollup_1.VERSION < '2.0.0') {
        console.error('[rollup-plugin-gzip] This plugin supports rollup version >= 2.0.0!');
        console.error('For older rollup versions, please use an older version of this plugin.');
    }
    // check for old options
    if ('algorithm' in options) {
        console.warn('[rollup-plugin-gzip] The "algorithm" option is not supported any more! ' +
            'Use "customCompression" instead to specify a different compression algorithm.');
    }
    if ('options' in options) {
        console.warn('[rollup-plugin-gzip] The "options" option was renamed to "gzipOptions"!');
    }
    if ('additional' in options) {
        console.warn('[rollup-plugin-gzip] The "additional" option was renamed to "additionalFiles"!');
    }
    if ('delay' in options) {
        console.warn('[rollup-plugin-gzip] The "delay" option was renamed to "additionalFilesDelay"!');
    }
}
function gzipPlugin(explicitOptions = {}) {
    performInitChecks(explicitOptions);
    const options = {
        // default options
        filter: /\.(js|mjs|cjs|json|css|html|wasm|svg)$/,
        fileName: '.gz',
        customCompression: (fileContent) => gzipPromise(fileContent, options.gzipOptions),
        gzipOptions: {},
        additionalFiles: [],
        additionalFilesDelay: 0,
        minSize: 0,
        ...explicitOptions,
    };
    const mapFileName = isFunction(options.fileName)
        ? options.fileName
        : (fileName) => fileName + options.fileName;
    const plugin = {
        name: 'gzip',
        async writeBundle(outputOptions, bundle) {
            const outputDir = outputOptions.file
                ? (0, path_1.dirname)(outputOptions.file)
                : outputOptions.dir || '';
            const compressBundleFile = async (fileName) => {
                const fileEntry = bundle[fileName];
                // filter check
                if (isRegExp(options.filter) && !fileName.match(options.filter)) {
                    return Promise.resolve();
                }
                if (isFunction(options.filter) &&
                    !options.filter(fileName)) {
                    return Promise.resolve();
                }
                const fileContent = getOutputFileContent(fileName, fileEntry, outputOptions);
                // minSize option check
                if (options.minSize && options.minSize > fileContent.length) {
                    return Promise.resolve();
                }
                try {
                    await writeFile((0, path_1.join)(outputDir, mapFileName(fileName)), await options.customCompression(fileContent));
                }
                catch (error) {
                    console.error(error);
                    return Promise.reject('[rollup-plugin-gzip] Error compressing file ' + fileName);
                }
            };
            const compressAdditionalFile = async (filePath) => {
                try {
                    const fileContent = await readFile(filePath);
                    await writeFile(mapFileName(filePath), await options.customCompression(fileContent));
                    return Promise.resolve();
                }
                catch (error) {
                    console.error(error);
                    return Promise.reject('[rollup-plugin-gzip] Error compressing additional file ' +
                        filePath +
                        '. Please check the spelling of your configured additionalFiles. ' +
                        'You might also have to increase the value of the additionalFilesDelay option.');
                }
            };
            const promises = Object.keys(bundle).map(compressBundleFile);
            if (!options.additionalFilesDelay) {
                promises.push(...options.additionalFiles.map(compressAdditionalFile));
            }
            else {
                // if a delay is set, we do not await the compression of additional files
                setTimeout(() => options.additionalFiles.map(compressAdditionalFile), options.additionalFilesDelay);
            }
            await Promise.all(promises);
        },
        // vite options
        ...{
            apply: 'build',
            enforce: 'post',
        },
    };
    return plugin;
}
exports.default = gzipPlugin;
