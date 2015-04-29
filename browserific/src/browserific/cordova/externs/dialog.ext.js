/**
* @fileoverview Externs for Cordova's dialog plugin
*
* @externs
*/

/**
 * @type {Object}
 * @const
 */
navigator.compass;

/**
 * @param {string} message
 * @param {Function} alertCallback
 * @param {string=} title
 * @param {string=} buttonName
 */
navigator.notification.alert = function(message, alertCallback, title, buttonName) {};

/**
 * @param {string} message
 * @param {Function} confirmCallback
 * @param {string=} title
 * @param {Array.<string>=} buttonLabels
 */
navigator.notification.confirm = function(message, confirmCallback, title, buttonLabels) {};

/**
 * @param {string} message
 * @param {Function} promptCallback
 * @param {string=} title
 * @param {Array.<string>=} buttonLabels
 * @param {string=} defaultText
 */
navigator.notification.prompt = function(message, promptCallback, title, buttonLabels, defaultText) {};

/**
 * @param {number} times
 */
navigator.notification.beep = function(times) {};
