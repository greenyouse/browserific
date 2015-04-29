/**
 * @fileoverview Externs for Cordova's media-capture (capture) plugin
 *
 * @externs
 */

/**
 * @type {Object}
 * @const
 */
navigator.device.capture;

/**
 * @typedef {{limit: number, duration: number}}
 */
CaptureAudioOptions;

/**
 * @typedef {{limit: number}}
 */
CaptureImageOptions;

/**
 * @typdef {{limit: number, duration: number}}
 */
CaptureVideoOptions;

/**
 * @param {Array.<MediaFile>} mediaFiles
 */
CaptureCB = function(mediaFiles) {};

/**
 * @enum {number}
 */
CaptureErrorCB = {
    CAPTURE_INTERNAL_ERR: 0,
    CAPTURE_APPLICATION_BUSY: 1,
    CAPTURE_INVALID_ARGUMENT: 2,
    CAPTURE_NO_MEDIA_FILES: 3,
    CAPTURE_NOT_SUPPORTED: 20
};

/**
 * @typedef {{type: string, height: number, width: number}}
 */
Configurationdata;

/**
 * @typedef {{name: string, fullPath: string, type: string,
 lastModifiedDate: Date, size: number}}
 * @constructor
 */
MediaFile;

/**
 * @param {Function} successCallback
 * @param {Function=} errorCallback
 */
MediaFile.prototype.getFormatData = function(successCallback, errorCallback) {};

/**
 * @typedef {{codecs: string, bitrate: number, height: number,
 width: number, duration: number}}
 */
MediaFileData;

/**
 * @param {CaptureCB} captureSuccess
 * @param {CaptureErrorCB} captureError
 * @param {CaptureAudioOptions=} options
 */
navigator.device.capture.captureAudio = function(captureSuccess, captureError, options) {};
/**
 * @param {CaptureCB} captureSuccess
 * @param {CaptureErrorCB} captureError
 * @param {CaptureImageptions=} options
 */
navigator.device.capture.captureImage = function(captureSuccess, captureError, options) {};

/**
 * @param {CaptureCB} captureSuccess
 * @param {CaptureErrorCB} captureError
 * @param {CaptureVideoOptions=} options
 */
navigator.device.capture.captureVideo = function(captureSuccess, captureError, options) {};
