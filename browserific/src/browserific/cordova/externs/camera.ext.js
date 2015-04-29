/**
* @fileoverview Externs for Cordova's camera plugin
*
* @externs
*/

/**
 * @type {Object}
 * @const
 */
navigator.camera;

/**
 * @enum {number}
 */
Camera.PopoverArrowDirection = {
    ARROW_UP : 1,
    ARROW_DOWN : 2,
    ARROW_LEFT : 4,
    ARROW_RIGHT : 8,
    ARROW_ANY : 15
};

/**
 * @typedef {{x: number, y: number, width: number, height: number, arrowDir: Camera.PopoverArrowDirection}}
 */
CameraPopoverOptions;

/**
 * @interface
 */
CameraPopoverHandle = function() {};

/**
 * @param {CameraPopoverOptions} cameraPopoverOptions
 */
CameraPopoverHandle.prototype.setPosition = function(cameraPopoverOptions) {};

/**
 * @param {function(string)} cameraSuccess
 * @param {function(string)} cameraError
 * @param {Object} cameraOptions
 */
navigator.camera.getPicture = function(cameraSuccess, cameraError, cameraOptions) {};


/**
 * @param {function()} cameraSuccess
 * @param {function(string)} cameraError
 */
navigator.camera.cleanup = function(cameraSuccess, cameraError) {};
