var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import * as fs from 'fs';
import { basename, dirname, join } from 'path';
import { promisify } from 'util';
import { gzip } from 'zlib';
import { VERSION, } from 'rollup';
const readFile = promisify(fs.readFile);
const writeFile = promisify(fs.writeFile);
const gzipPromise = promisify(gzip);
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
                : `${basename(outputFileName)}.map`;
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
    if (VERSION < '2.0.0') {
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
    const options = Object.assign({ 
        // default options
        filter: /\.(js|mjs|cjs|json|css|html|wasm|svg)$/, fileName: '.gz', customCompression: (fileContent) => gzipPromise(fileContent, options.gzipOptions), gzipOptions: {}, additionalFiles: [], additionalFilesDelay: 0, minSize: 0 }, explicitOptions);
    const mapFileName = isFunction(options.fileName)
        ? options.fileName
        : (fileName) => fileName + options.fileName;
    const plugin = Object.assign({ name: 'gzip', writeBundle(outputOptions, bundle) {
            return __awaiter(this, void 0, void 0, function* () {
                const outputDir = outputOptions.file
                    ? dirname(outputOptions.file)
                    : outputOptions.dir || '';
                const compressBundleFile = (fileName) => __awaiter(this, void 0, void 0, function* () {
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
                        yield writeFile(join(outputDir, mapFileName(fileName)), yield options.customCompression(fileContent));
                    }
                    catch (error) {
                        console.error(error);
                        return Promise.reject('[rollup-plugin-gzip] Error compressing file ' + fileName);
                    }
                });
                const compressAdditionalFile = (filePath) => __awaiter(this, void 0, void 0, function* () {
                    try {
                        const fileContent = yield readFile(filePath);
                        yield writeFile(mapFileName(filePath), yield options.customCompression(fileContent));
                        return Promise.resolve();
                    }
                    catch (error) {
                        console.error(error);
                        return Promise.reject('[rollup-plugin-gzip] Error compressing additional file ' +
                            filePath +
                            '. Please check the spelling of your configured additionalFiles. ' +
                            'You might also have to increase the value of the additionalFilesDelay option.');
                    }
                });
                const promises = Object.keys(bundle).map(compressBundleFile);
                if (!options.additionalFilesDelay) {
                    promises.push(...options.additionalFiles.map(compressAdditionalFile));
                }
                else {
                    // if a delay is set, we do not await the compression of additional files
                    setTimeout(() => options.additionalFiles.map(compressAdditionalFile), options.additionalFilesDelay);
                }
                yield Promise.all(promises);
            });
        } }, {
        apply: 'build',
        enforce: 'post',
    });
    return plugin;
}
export default gzipPlugin;
