var helpers = require('./helpers'),
    should  = require('should'),
    sinon   = require('sinon'),
    http    = require('http'),
    needle  = require('./../');

const port = 1234;

describe('redirects with bad redirector', function() {

  var spies   = {},
      servers = {};

  before(function(done) {
    servers.http = http.createServer(function(req, res) {
      if (req.url == '/foo/bar') {
        res.end('Redirected successfully!');
        return;
      }

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
    }).listen(port, done);
  })

  after(function(done) {
    servers.http.close(done);
  })

  it('calls back exactly once', function (done) {
    const opts = {
      follow: 5,
    }

    const url = `http://localhost:${port}`
    needle.head(url, opts, function (err, resp, body) {
      //should(body && body.toString()).eql('Redirected successfully!');
      done();
    });
  });

  it('calls back exactly once with follow_keep_method', function (done) {
    const opts = {
      follow: 5,
      follow_keep_method: true,
    }

    const url = `http://localhost:${port}`
    needle.head(url, opts, function (err, resp, body) {
      //should(body && body.toString()).eql('Redirected successfully!');
      done();
    });
  });
});
