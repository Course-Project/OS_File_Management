/**
 * 
 * @authors Tom Hu (webmaster@h1994st.com)
 * @date    2014-06-02 13:53:06
 * @version 1.0
 */

function _trim(str) {
  return str.replace(/^\s+/, '').replace(/\s+$/, '')
    .replace(/^\"/, '').replace(/\"$/, '');
}

function parseCommand(command) {
  var parts = /^\s*([A-Za-z]+)\(([^\(\)]*)\)\s*$/.exec(command);
  if (!parts) {
    return null;
  };
  return {
    cmd: _trim(parts[1]),
    args: parts[2].split(/\"\s*,\s*\"/).map(function(val) { return _trim(val); })
  };
};

function main(command) {
  command = parseCommand(command);
  switch (command.cmd) {
    case 'pwd':
      console.log("pwd");
      break;
    case 'create':
      console.log("create");
      break;
    case 'ls':
      console.log("ls");
      break;
    case 'exit':
      console.log("exit");
      break;
    case 'read':
      console.log("read");
      break;
    case 'write':
      console.log("write");
      break;
    case 'unlink':
      console.log("unlink");
      break;
    case 'mkdir':
      console.log("mkdir");
      break;
    case 'cd':
      console.log("cd");
      break;
    case 'goback':
      console.log("goback");
      break;
    case 'info':
      console.log("info");
      break;
    default:
      console.log("命令解析出错，请检查命令格式是否正确");
      break;
  }
};

module.exports = main;

