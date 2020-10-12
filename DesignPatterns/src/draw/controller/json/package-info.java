/**
 * Responsible for converting Model into JSON and vice versa.
 * <p>
 * Takes advantage of immutable Style objects to construct a minimal repository of all
 * styles used in the Model. Uses multiple visitors to extract information from the Model.
 * The {@link JSON_Export} class does all of the heavy lifting.
 * 
 * Uses the open source JSON-simple libraries, available for download from 
 * <a href="https://code.google.com/archive/p/json-simple/downloads">https://code.google.com/archive/p/json-simple/downloads</a>
 *
 * @since draw.5
 */
package draw.controller.json;
