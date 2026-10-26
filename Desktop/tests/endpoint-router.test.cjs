const { test } = require('node:test');
const assert = require('node:assert/strict');
const { routeEndpoint, setChinaEndpoints } = require('../dist/tsc/endpoint-router.js');

test('routes all services and preserves signed media paths, port, query and fragment', () => {
  for (const service of ['api', 'idk', 'storage', 'images', 'socket']) {
    const source = `https://${service}.neurokaraoke.com:8443/audio/a%20b%2Fc.mp3?sig=a%2Bb&url=https://storage.neurokaraoke.com/#part`;
    const target = source.replace(`${service}.neurokaraoke.com:`, `${service}.neurokaraoke.com.cn:`);
    assert.equal(routeEndpoint(source, true), target);
    assert.equal(routeEndpoint(target, true), target);
    assert.equal(routeEndpoint(target, false), source);
  }
  assert.equal(routeEndpoint('wss://socket.neurokaraoke.com/live', true), 'wss://socket.neurokaraoke.com.cn/live');
});
test('ignores websites, third parties, lookalikes and local files', () => {
  for (const url of ['https://neurokaraoke.com/', 'https://cn.neurokaraoke.com/', 'https://discord.com/', 'https://api.neurokaraoke.com.evil.test/', 'https://notneurokaraoke.com/', 'file:///audio.mp3', 'not a URL']) {
    assert.equal(routeEndpoint(url, true), url);
  }
});
test('uses the current override for each request', () => {
  const source = 'https://api.neurokaraoke.com/api/songs';
  setChinaEndpoints(true);
  assert.equal(routeEndpoint(source), 'https://api.neurokaraoke.com.cn/api/songs');
  setChinaEndpoints(false);
  assert.equal(routeEndpoint(source), source);
});
