const _lightgrey = "\u001b[30m";
const _lightred = "\u001b[31m";
const _lightgreen = "\u001b[32m";
const _lightyellow = "\u001b[33m";
const _lightocean = "\u001b[34m";
const _lightpurple = "\u001b[35m";
const _lightblue = "\u001b[36m";

const _grey = "\u001b[90m";
const _white = "\u001b[97m";
const _red = "\u001b[91m";
const _green = "\u001b[92m";
const _ocean = "\u001b[94m";
const _yellow = "\u001b[93m";
const _purple = "\u001b[95m";
const _blue = "\u001b[96m";
const _darkgrey = "\u001b[02m";
const _black = "\u001b[8m";

const _normal = "\u001b[0m";
const _bold = "\u001b[01m";
const _italic = "\u001b[03m";
const _subline = "\u001b[04m";
const _doubleline = "\u001b[21m";
const _innerline = "\u001b[9m";

const _markblack = "\u001b[40m";
const _markred = "\u001b[41m";
const _markgreen = "\u001b[42m";
const _markyellow = "\u001b[43m";
const _markocean = "\u001b[44m";
const _markpurple = "\u001b[45m";
const _markblue = "\u001b[46m";
const _markgrey = "\u001b[47m";
const _markwhite = "\u001b[7m";

const _lightmarkgrey = "\u001b[100m";
const _lightmarkred = "\u001b[101m";
const _lightmarkgreen = "\u001b[102m";
const _lightmarkyellow = "\u001b[103m";
const _lightmarkocean = "\u001b[104m";
const _lightmarkpurple = "\u001b[105m";
const _lightmarkblue = "\u001b[106m";
const _lightmarkwhite = "\u001b[107m";

const coloral = {
    normal: function normal(content) {
        return _normal + content + _normal;
    },
    bold: function bold(content) {
        return _bold + content + _normal;
    },
    italic: function italic(content) {
        return _italic + content + _normal;
    },
    subline: function subline(content) {
        return _subline + content + _normal;
    },
    doubleline: function doubleline(content) {
        return _doubleline + content + _normal;
    },
    innerline: function innerline(content) {
        return _innerline + content + _normal;
    },


    lightgrey: function lightgrey(content) {
        return _lightgrey + content + _normal;
    },
    lightred: function lightred(content) {
        return _lightred + content + _normal;
    },
    lightgreen: function lightgreen(content) {
        return _lightgreen + content + _normal;
    },
    lightyellow: function lightyellow(content) {
        return _lightyellow + content + _normal;
    },
    lightocean: function lightocean(content) {
        return _lightocean + content + _normal;
    },
    lightpurple: function lightpurple(content) {
        return _lightpurple + content + _normal;
    },
    lightblue: function lightblue(content) {
        return _lightblue + content + _normal;
    },

    grey: function grey(content) {
        return _grey + content + _normal;
    },
    white: function white(content) {
        return _white + content + _normal;
    },
    red: function red(content) {
        return _red + content + _normal;
    },
    green: function green(content) {
        return _green + content + _normal;
    },
    ocean: function ocean(content) {
        return _ocean + content + _normal;
    },
    yellow: function yellow(content) {
        return _yellow + content + _normal;
    },
    purple: function purple(content) {
        return _purple + content + _normal;
    },
    blue: function blue(content) {
        return _blue + content + _normal;
    },
    darkgrey: function darkgrey(content) {
        return _darkgrey + content + _normal;
    },
    black: function black(content) {
        return _black + content + _normal;
    },

    markblack: function markblack(content) {
        return _markblack + content + _normal;
    },
    markred: function markred(content) {
        return _markred + content + _normal;
    },
    markgreen: function markgreen(content) {
        return _markgreen + content + _normal;
    },
    markyellow: function markyellow(content) {
        return _markyellow + content + _normal;
    },
    markocean: function markocean(content) {
        return _markocean + content + _normal;
    },
    markpurple: function markpurple(content) {
        return _markpurple + content + _normal;
    },
    markblue: function markblue(content) {
        return _markblue + content + _normal;
    },
    markgrey: function markgrey(content) {
        return _markgrey + content + _normal;
    },
    markwhite: function markwhite(content) {
        return _markwhite + content + _normal;
    },
    lightmarkgrey: function lightmarkgrey(content) {
        return _lightmarkgrey + content + _normal;
    },
    lightmarkred: function lightmarkred(content) {
        return _lightmarkred + content + _normal;
    },
    lightmarkgreen: function lightmarkgreen(content) {
        return _lightmarkgreen + content + _normal;
    },
    lightmarkyellow: function lightmarkyellow(content) {
        return _lightmarkyellow + content + _normal;
    },
    lightmarkocean: function lightmarkocean(content) {
        return _lightmarkocean + content + _normal;
    },
    lightmarkpurple: function lightmarkpurple(content) {
        return _lightmarkpurple + content + _normal;
    },
    lightmarkblue: function lightmarkblue(content) {
        return _lightmarkblue + content + _normal;
    },
    lightmarkwhite: function lightmarkwhite(content) {
        return _lightmarkwhite + content + _normal;
    }
}

module.exports = { coloral };