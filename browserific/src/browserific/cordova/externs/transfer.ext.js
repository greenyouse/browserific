/**
 * @fileoverview Externs for Cordova's file-transfer (transfer) plugin
 *
 * @externs
 */

/**
 * @constructor
 */
FileTransfer;

/**
 * @constructor
 */
FileUploadOptions;

/**
 * TODO: check that all fields return OK (see docs)
 * @param {number} bytesSent
 * @param {number} responseCode
 * @param {string} response
 * @param {Object} headers
 */
FileUploadResult;

/**
 * @param {number} code
 * @param {string} source
 * @param {string} target
 * @param {number} http_status
 * @param {string} body
 * @param {string} exception
 */
FileTransferError;

/**
 * @param {string} fileURL
 * @param {string} server
 * @param {function(FileUploadResult)} successCallback
 * @param {function(FileTransferError)} errorCallback
 * @param {Object=} options
 * @param {boolean=} trustAllHosts
 */
FileTransfer.prototype.upload = function(fileURL, server, successCallback, errorCallback, options, trustAllHosts) {};

/**
 * @param {string} source
 * @param {Function} successCallback TODO: add FileEntry object here
 * @param {function(FileTransferError)} errorCallback
 * @param {boolean=} trustAllHosts
 * @param {Object=} options
 */
FileTransfer.prototype.download = function(source, successCallback, errorCallback, trustAllHosts, options) {};

FileTransfer.prototype.abort = function() {};
