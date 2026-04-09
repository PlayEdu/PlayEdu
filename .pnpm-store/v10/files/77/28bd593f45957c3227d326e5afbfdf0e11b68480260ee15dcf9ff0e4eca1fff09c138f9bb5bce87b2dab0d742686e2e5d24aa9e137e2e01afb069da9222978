var http = require('http'),
    https = require('https'),
    url = require('url');
    
var port = 1234,
    log  = true,
    request_auth = false;

http.createServer(function(req, res) {

  console.log(req.headers);
  console.log("Got request: " + req.method + " " + req.url);

  /* Don't judge me. I found at least one server on the
  * Internet doing this and it triggered a bug. */
  const body = 'Let me send you a body, although you only asked for a HEAD.';

  const headers =
    'HTTP/1.1 302 Found\r\n' +
    'Connection: close\r\n' +
    'Location: /foo/bar\r\n' +
    `Content-Length: ${Buffer.byteLength(body)}\r\n` +
    '\r\n';

  res.socket.write(headers + body);
  res.socket.destroy();
}).listen(port);

process.on('uncaughtException', function(err){
  console.log('Uncaught exception!');
  console.log(err);
});

console.log("Bad redirector listening on port " + port);
