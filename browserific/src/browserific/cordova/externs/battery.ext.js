/**
* @fileoverview Externs for Cordova's battery-status plugin
*
* @externs
*/

/**
 * @interface
 * @extends {EventTarget}
 */
function batterystatus () {};

/**
 * @type {number}
 */
batterystatus.prototype.level;

/**
 * @type {boolean}
 */
batterystatus.prototype.isPlugged;

/**
 * @interface
 * @extends {EventTarget}
 */
function batterycritical () {};

/**
 * @type {number}
 */
batterycritical.prototype.level;

/**
 * @type {boolean}
 */
batterycritical.prototype.isPlugged;

/**
 * @interface
 * @extends {EventTarget}
 */
function batterylow () {};

/**
 * @type {number}
 */
batterylow.prototype.level;

/**
 * @type {boolean}
 */
batterylow.prototype.isPlugged;
