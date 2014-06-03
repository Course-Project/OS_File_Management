/**
 * 
 * @authors Tom Hu (h1994st@gmail.com)
 * @date    2014-05-31 21:36:11
 * @version 1.0
 */

(function (window) {

    // Console
    function Console(consoleID) {

        // Static member variables
        Console.KEY_ENTER = 13; // Enter
        Console.KEY_RETURN = 13; // Return
        Console.KEY_TAB = 9; // Tab
        Console.KEY_BACKSPACE = 8; // Backspace
        Console.KEY_LEFT = 37; // Left
        Console.KEY_RIGHT = 38; // Right
        Console.KEY_UP = 39; // Up
        Console.KEY_DOWN = 40; // Down

        Console.DEFAULT_CONSOLE_ID = "console";

        this.consoleMain = document.getElementById(consoleID || this.DEFAULT_CONSOLE_ID);

        if (this.consoleMain.tagName.toUpperCase() != "TEXTAREA") {
            throw new Error("控制台输出窗口必须为TEXTAREA");
        };

        if (this.consoleMain == null) {
            // TODO - 当没有获取到TEXTAREA对象时
        };


        /**
         * IO Part
         */
        this.stream = new Object();
        this.stream.stdin = new Array(); // stdin
        this.stream.stdout = new Array(); // stdout
        this.stream.sederr = new Array(); // stderr

        this.stream.stdin.opened = false; // default: not open
        // TODO
        

        /**
         * KeyPress, KeyUp, KeyDown, Click Event Part
         */
        // KeyPress
        this.consoleMain.keyPressSender = this;
        this.consoleMain.onkeypress = function (e) {
            var e = e || window.event;
            this.keyPressSender.keyPress(this.keyPressSender, e);
        };

        // KeyUp
        this.consoleMain.keyUpSender = this;
        this.consoleMain.onkeyup = function (e) {
            var e = e || window.event;
            this.keyUpSender.keyUp(this.keyUpSender, e);
        };

        // KeyDown
        this.consoleMain.keyDownSender = this;
        this.consoleMain.onkeydown = function (e) {
            var e = e || window.event;
            this.keyDownSender.keyDown(this.keyDownSender, e);
        };

        // Click
        this.consoleMain.clickSender = this;
        this.consoleMain.onclick = function (e) {
            var e = e || window.event;
            this.clickSender.click(this.clickSender, e);
        };

        /**
         * Event Handler Part
         */
        // KeyPress
        Console.prototype.keyPress = function (sender, enevt) {
            // not Key 'Enter'
            if (event.keyCode != Console.KEY_ENTER) {
                // new input
                if (!this.stream.stdin.opened) {
                    this.stream.stdin.push(String.fromCharCode(event.keyCode));
                    this.stream.stdin.opened = true;
                } else {
                    this.stream.stdin[this.stream.stdin.length - 1] += String.fromCharCode(event.keyCode);
                };
            };

            console.log("Key Press");
        };

        // KeyUp
        Console.prototype.keyUp = function (sender, event) {
            console.log("Key Up");
        };

        // KeyDown
        Console.prototype.keyDown = function (sender, event) {
            // Handle Key 'Tab'
            if (event.keyCode == Console.KEY_TAB) {
                event.preventDefault();
            };

            if (event.keyCode == Console.KEY_LEFT
                || event.keyCode == Console.KEY_UP
                || event.keyCode == Console.KEY_RIGHT
                || event.keyCode == Console.KEY_DOWN) {

                
            };



            console.log("Key Down");

            return true;
        };

        // Click
        Console.prototype.click = function (sender, event) {
            event.preventDefault();
            console.log("Key Click");
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

    window.Console = new Console("console");


})(window)

