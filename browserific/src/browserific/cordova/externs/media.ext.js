/**
 * @fileoverview Externs for Cordova's media plugin
 *
 * @externs
 */


/**
 * @typedef {{code: number, message: string}}
 */
MediaError;

/**
 * @param {string} src
 * @param {Function=} mediaSuccess
 * @param {Function=} mediaError
 * @param {Function=} mediaStatus
 * @constructor
 */
function Media (mediaSuccess, MediaError, mediaStatus) {};

/**
 * @param {function(number)} mediaSuccess
 * @param {function(MediaError)=} mediaError
 */
Media.getCurrentPosition = function(mediaSuccess, MediaError) {};

Media.getDuration = function () {};

/**
 * @param {Object=} opts
 */
Media.prototype.play = function(opts) {};
Media.prototype.pause = function() {};
Media.prototype.release = function() {};

/**
 * @param {number} milliseconds
 */
Media.prototype.seekTo = function(milliseconds) {};

/**
 * @param {number} volume;
 */
Media.prototype.setVolume = function(volume) {};

Media.prototype.startRecord = function() {};
Media.prototype.stopRecord = function() {};
Media.prototype.stop = function() {};

/**
 * @type {number}
 */
Media.position;

/**
 * @type {number}
 */
Media.duration;
