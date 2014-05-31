/**
 * 
 * @authors Tom Hu (h1994st@gmail.com)
 * @date    2014-05-31 21:36:11
 * @version 1.0
 */

(function (window) {

    function Console(consoleID) {
        this.consoleMain = document.getElementById(consoleID);

        if (this.consoleMain.tagName.toUpperCase() != "TEXTAREA") {
            throw new Error("控制台输出窗口必须为TEXTAREA");
        };

        Console.prototype.log = function (string) {
            this.consoleMain.value += ("> " + string + "\n");
        };

        Console.prototype.error = function (string) {
            this.consoleMain.value += ("> !ERROR: " + string + "\n");
        };

        Console.prototype.clear = function () {
            this.consoleMain.value = "";
        };
    };


})(window)