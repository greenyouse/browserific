/**
 * @fileoverview Externs for Cordova's statusbar plugin
 *
 * @externs
 */

/**
 * @type {Object}
 * @const
 */
StatusBar;

/**
 * @param {boolean} visible
 */
StatusBar.prototype.overlaysWebView = function(visible) {};

StatusBar.prototype.styleDefault = function() {};
StatusBar.prototype.styleLightContent = function() {};
StatusBar.prototype.styleBlackTranslucent = function() {};
StatusBar.prototype.styleBlackOpaque = function() {};

/**
 * @param {string} color
 */
StatusBar.prototype.backgroundColorByName = function(color) {};

/**
 * @param {string} color
 */
StatusBar.prototype.backgroundColorByHexString = function(color) {};
StatusBar.prototype.hide = function() {};
StatusBar.prototype.show = function() {};

/**
 * @type {boolean}
 */
StatusBar.isVisible;
