/**
 * 
 * @authors Tom Hu (webmaster@h1994st.com)
 * @date    2014-06-02 13:51:28
 * @version 1.0
 */

var cli = require('./sys/cli.js');

process.stdin.on('data', function (chunk) {
  cli(chunk);
});
process.stdin.on('end', function () {
  process.stdout.write('end');
});