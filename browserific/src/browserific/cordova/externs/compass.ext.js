/**
* @fileoverview Externs for Cordova's compass plugin
*
* @externs
*/

/**
 * @type {Object}
 * @const
 */
navigator.compass;

/**
 * @constructor
 */
CompassHeading;

/**
 * @type {number}
 */
CompassHeading.prototype.magneticHeading;

/**
 * @type {number}
 */
CompassHeading.prototype.trueHeading;

/**
 * @type {number}
 */
CompassHeading.prototype.headingAccuracy;

/**
 * @type {Date}
 */
CompassHeading.prototype.timestamp;

/**
 * @enum {number}
 */
CompassError = {
    COMPASS_INTERNAL_ERR: 0,
    COMPASS_NOT_SUPPORTED: 20
};

/**
 * @param {function(CompassHeading)} compassSuccess
 * @param {function(CompassError)} compassError
 */
navigator.compass.getCurrentHeading = function(compassSuccess, compassError) {};

/**
 * @param {function(CompassHeading)} compassSuccess
 * @param {function(CompassError)} compassError
 * @return {string}
 */
navigator.compass.watchHeading = function(compassSuccess, compassError) {};

/**
 * @param {string} watchID
 */
navigator.compass.clearWatch = function(watchID) {};
