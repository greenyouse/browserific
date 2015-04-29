/**
 * @fileoverview Externs for Cordova's vibration plugin
 *
 * @externs
 */

/**
 * @param {number|Array.<number>} time
 */
navigator.vibrate = function(time) {};

/**
 * @param {number} time
 */
navigator.notification.prototype.vibrate = function(time) {};

/**
 * @param {Array.<number>} time
 * @param {number} repeat
 */
navigator.notification.prototype.vibrateWithPattern = function(time, repeat) {};

navigator.notification.prototype.cancelVibration = function() {};
