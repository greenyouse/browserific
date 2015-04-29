/**
 * @fileoverview Externs for Cordova's network-information (connection) plugin
 *
 * @externs
 */

/**
 * @type {Object}
 * @const
 */
navigator.connection;

/**
 * @return {Connection}
 */
navigator.connection.type = function() {};

/**
 * @enum {string}
 */
Connection = {
    UNKNOWN: "unknown",
    ETHERNET: "ethernet",
    WIFI: "wifi",
    CELL_2G: "2g",
    CELL_3G: "3g",
    CELL_4G: "4g",
    CELL:"cellular",
    NONE: "none"
};
