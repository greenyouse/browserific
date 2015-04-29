/**
 * @fileoverview Externs for Cordova's inAppBrowser (browser) plugin
 *
 * @externs
 */

/**
 * @param {string} url
 * @param {string} target
 * @param {Object=} options
 * @return {InAppBrowser}
 */
window.prototype.open = function(url, target, options) {};

/**
 * @typedef {{type: string, url: string, code: number, message: string}}
 */
InAppBrowserEvent;

/**
 * @interface
 */
function InAppBrowser () {};

/**
 * @param {string} eventname
 * @param {function(InAppBrowserEvent)} callback
 */
InAppBrowser.prototype.addEventListener = function(eventname, callback) {};

/**
 * @param {string} eventname
 * @param {function(InAppBrowserEvent)} callback
 */
InAppBrowser.prototype.removeEventListener = function(eventname, callback) {};

InAppBrowser.prototype.close = function() {};

InAppBrowser.prototype.show = function() {};

/**
 * @param {Object} details
 * @param {Function} callback
 */
InAppBrowser.prototype.executeScript = function(details, callback) {};

/**
 * @param {Object} injectDetails
 * @param {Function} callback
 */
InAppBrowser.prototype.insertCSS = function(injectDetails, callback) {};
