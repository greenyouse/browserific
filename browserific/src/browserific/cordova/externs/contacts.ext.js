/**
* @fileoverview Externs for Cordova's contacts plugin
*
* @externs
*/

/**
 * @type {Object}
 * @const
 */
navigator.contacts = {};

/**
 * @typedef {{id: string, displayName: string, name: ContactName,
 nickname: string, phoneNumbers: ContactField, emails: ContactField,
 addresses: ContactAddress, ims: ContactField,
 organizations: ContactOrganization, birthday: Date, note: string,
 photos: ContactField, categories: ContactField, urls: ContactField}}
 * @interface
 */
Contact;

/**
 * @return {Contact}
 */
Contact.prototype.clone = function () {};

/**
 * @param {Function} onSuccess
 * @param {Function} onError
 */
Contact.prototype.remove = function(onSuccess, onError) {};

/**
 * @param {Function} onSuccess
 * @param {Function} onError
 */
Contact.prototype.save = function(onSuccess, onError) {};

/**
 * @typedef {{formatted: string, familyName: string, givenName: string,
 middleName: string, honorificPrefix: string, honorificSuffix: string}}
 * @constructor
 */
ContactName;

/**
 * @typedef {{type: string, value: string, pref: boolean}}
 * @constructor
 */
ContactField;

/**
 * @typdef {{pref: boolean, type: string, formatted: string,
 streetAddress: string, locality: string, region: string, region: string
 postalCode: string, country: string}}
 * @constructor
 */
ContactAddress;

/**
 * @typedef {{pref: boolean, type: string, name: string, title: string}}
 * @constructor
 */
ContactOrganization;

/**
 * @typedef {{filter: string, multiple: boolean, desiredFields: string}}
 * @constructor
 */
ContactFindOptions;

/**
 * @enum {number}
 */
ContactError = {
    UNKNOWN_ERROR: 0,
    INVALID_ARGUMENT_ERROR: 1,
    TIMEOUT_ERROR: 2,
    PENDING_OPERATION_ERROR: 3,
    IO_ERROR: 4,
    NOT_SUPPORTED: 5,
    PERMISSION_DENIED_ERROR: 20
};

/**
 * @param {Contact=} contact
 * @return {Contact}
 */
navigator.contacts.create = function(contact) {};

/**
 * TODO: could make options better here
 * @param {string} contactFields
 * @param {function(Array.<Contact>)} contactSuccess
 * @param {function(ContactError)=} contactError
 * @param {ContactFindOptions=} contactFindOptions
 */
navigator.contacts.find = function(contactFields, contactSuccess, contactError, contactFindOptions) {};

/**
 * @param {function(Contact)} contactSuccess
 * @param {function(ContactError)=} contactSuccess
 */
navigator.contacts.pickContact = function(contactSuccess, contactError) {};
