/**
 * @fileoverview Externs for Cordova's geolocation plugin
 *
 * @externs
 */

/**
 * @type {Object}
 * @const
 */
navigator.geolocation;

/**
 * @typedef {{coords: Coordinates, timestamp: Date}}
 */
Position;

/**
 * @typedef {{code: number, message: string}}
 */
PositionError;

/**
 * @typedef {{latitude: number, longitude: number, altitude: number,
 accuracy: number, altitudeAccuracy: number, heading: number,
 speed: number}}
 */
Coordinates;

/**
 * @param {function(Coordinates)} geolocationSuccess
 * @param {function(PositionError)=} geolocationError
 * @param {Object=} geolocationOptions
 */
navigator.geolocation.getCurrentPosition = function(geolocationSuccess, geolocationError, geolocationOptions) {};

/**
 * @param {function(Coordinates)} geolocationSuccess
 * @param {function(PositionError)=} geolocationError
 * @param {Object=} geolocationOptions
 * @return {string}
 */
navigator.geolocation.watchPosition = function(geolocationSuccess, geolocationError, geolocationOptions) {};

/**
 * @param {string} watchID
 */
navigator.geolocation.clearWatch = function(watchID) {};
