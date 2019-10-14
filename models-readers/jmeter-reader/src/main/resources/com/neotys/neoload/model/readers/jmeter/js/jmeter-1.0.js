/*
* Copyright (c) Neotys 2005-2019.
*/

/**
* Returns a random number that lies between the given min and max values.
* If y parameter is absent, returns a number between 0 and x.
* @param x the min value
* @param y the max value (Optional)
*/
function __Random(x, y) {
    var min, max;

    if (y === undefined) {
        min = 0;
        max = x;
    } else {
        min = x;
        max = y;
    }

    if (min > max) {
        min = max;
    }

    return Math.round(Math.random() * (max - min + 1)  + min);
}

/**
* Encodes an URL.
* @param url string to encode in URL encoded chars
*/
function __urlencode(url) {
    return encodeURI(url);
}

/**
* Decodes an URL.
* @param url the string with URL encoded chars to decode
*/
function __urldecode(url) {
    return decodeURI(url);
}

/**
* Escapes the characters in a String using HTML entities.
* @param s the string to escape
*/
function __escapeHtml(s) {
    return escape(s);
}

/**
* Unescapes a string containing HTML entity escapes to a string containing the actual Unicode characters corresponding to the escapes.
* @param s the string to escape
*/
function __unescapeHtml(s) {
    return unescape(s);
}

/**
* Returns a random String of length using characters in chars to use.
* If chars parameter is absent, returns a string which contains chars from 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&()*+,;='.
* @param length a number length of generated String
* @param chars chars used to generate String (optional)
*/
function __RandomString(length, chars) {
   var result = '';
   var characters;

   if (chars === undefined) {
      characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&()*+,;=';
   } else {
      characters = chars;
   }

   var charactersLength = characters.length;
   for (var i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }

   return result;
}

/**
* Returns the result of evaluating a list of numbers as Unicode characters.
* @param a list of decimal numbers (or hex number, if prefixed by 0x, or octal, if prefixed by 0) to be converted to a Unicode character
*/
function __char() {
   var vars = Array.prototype.slice.call(arguments, 0);
   var ret = "";
   for(var i = 0; i< vars.length; i++) {
       ret += String.fromCharCode(vars[i]);
   }
   return ret;
}

/**
* Returns a string value which case has been changed following a specific mode.
* @param s the String which case will be changed
* @param mode the mode to be used to change case. Can be UPPER, LOWER or CAPITALIZE
*/
function __changeCase(s, mode) {
   if (mode == "UPPER") {
      return s.toUpperCase();
   }

   if (mode == "LOWER") {
      return s.toLowerCase();
   }

   if (mode == "CAPITALIZE") {
      var splits = s.split(" ");
      var caps = new Array(splits.length);
      for(var i = 0; i < splits.length; i++) {
          caps[i] = splits[i].charAt(0).toUpperCase() + splits[i].slice(1);
      }
      return caps.join(" ");
   }

return s;
}

/**
* Returns the sum of two or more integer values.
* @param a list of numbers to sum
*/
function __intSum() {
   var vars = Array.prototype.slice.call(arguments, 0);
   var ret = 0;
   for(var i = 0; i< vars.length; i++) {
       ret += new Number(vars[i]);
   }
   return ret.toFixed(0);
}

/**
* Returns the sum of two or more long values.
* @param a list of numbers to sum
*/
function __longSum() {
   return __intSum.apply(null, arguments);
}

/**
* Returns the id of the VU which is being executed. Matches the context value: context.currentVU.id
*/
function __threadNum() {
   return context.currentVU.id;
}

/**
* Returns the name of the population which is being executed. Matches the context value: context.currentVU.getPopulationName
*/
function __threadGroupName() {
   return context.currentVU.getPopulationName();
}

/**
* Returns the local IP address.
*/
function __machineIP() {
    var host = java.net.InetAddress.getLocalHost();
    var address = host.getHostAddress();
    return String(address).valueOf();
}

/**
* Returns the local hostname.
*/
function __machineName() {
    var host = java.net.InetAddress.getLocalHost();
    var name = host.getHostName();
    return String(name).valueOf();
}

/**
* Returns a pseudo random type 4 Universally Unique IDentifier (UUID).
*/
function __UUID() {
   return String(java.util.UUID.randomUUID()).valueOf();
}

/**
* Returns an encrypted value in the specific hash algorithm with the optional salt and  upper case option.
* @param algorithm the algorithm to be used to encrypt. Possible algorithms are : MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512
* @param strToEncode the string to encode
* @param salt the salt to be added to string (after it) (optional)
* @param upperCase if true, the function returns uppercase results (optional default is false)
*/
function __digest(algorithm, strToEncode, salt, upperCase) {

   var md = java.security.MessageDigest.getInstance(algorithm);
   md.update(__toUTF8Array(strToEncode));
   if (salt !== undefined) {
      md.update(__toUTF8Array(salt));
   }
   var bytes = md.digest();

   var ret = __toHexString(bytes);
   if (upperCase !== undefined && upperCase == true) {
      ret = ret.toUpperCase();
   }
   return ret;
}

/**
* Converts a byte array into a string of hex chars.
* @param byteArray the array of bytes to convert
*/
function __toHexString(byteArray) {
    var s = "";
    byteArray.forEach(function(byte) {
      s += ("0" + (byte & 0xFF).toString(16)).slice(-2);
    });

    return s;
}

/**
* Converts a string in a UTF-8 chars array.
* @param str the string to convert
*/
function __toUTF8Array(str) {
    var utf8 = [];
    for (var i=0; i < str.length; i++) {
        var charcode = str.charCodeAt(i);
        if (charcode < 0x80) utf8.push(charcode);
        else if (charcode < 0x800) {
            utf8.push(0xc0 | (charcode >> 6),
                      0x80 | (charcode & 0x3f));
        }
        else if (charcode < 0xd800 || charcode >= 0xe000) {
            utf8.push(0xe0 | (charcode >> 12),
                      0x80 | ((charcode>>6) & 0x3f),
                      0x80 | (charcode & 0x3f));
        }
        // surrogate pair
        else {
            i++;
            // UTF-16 encodes 0x10000-0x10FFFF by
            // subtracting 0x10000 and splitting the
            // 20 bits of 0x0-0xFFFFF into two halves
            charcode = 0x10000 + (((charcode & 0x3ff)<<10)
                      | (str.charCodeAt(i) & 0x3ff));
            utf8.push(0xf0 | (charcode >>18),
                      0x80 | ((charcode>>12) & 0x3f),
                      0x80 | ((charcode>>6) & 0x3f),
                      0x80 | (charcode & 0x3f));
        }
    }
    return utf8;
}