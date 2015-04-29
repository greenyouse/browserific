/**
* @fileoverview Externs for Cordova's device-motion (accelerometer) plugin
*
* @externs
*/

/**
 * @type {Object}
 * @const
 */
navigator.accelerometer = {};

/**
 * @typedef {{x: number, y: number, z: number, timestamp: Date}}
 */
navigator.accelerometer.Acceleration;

/**
 * @param {function(navigator.accelerometer.Acceleration)} accelerometerSuccess
 * @param {Function} accelerometerError
 */
navigator.accelerometer.getCurrentAcceleration = function(accelerometerSuccess, accelerometerError) {};

/**
 * @param {function(navigator.accelerometer.Acceleration)} accelerometerSuccess
 * @param {Function} accelerometerError
 * @param {Object=} accelerometerOptions
 */
navigator.accelerometer.watchAcceleration = function(accelerometerSuccess, accelerometerError, accelerometerOptions) {};

/**
 * @param {string} watch
 */
navigator.accelerometer.clearWatch  = function(watch) {};
