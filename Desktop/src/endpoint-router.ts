/** Route service hosts without re-encoding signed paths or query strings. */
export function routeEndpoint(value: string, china = useChinaEndpoints): string {
  let url: URL;
  try { url = new URL(value); } catch { return value; }
  if (!['https:', 'http:', 'wss:', 'ws:'].includes(url.protocol)) return value;
  const host = url.hostname;
  const globalHost = host.endsWith('.neurokaraoke.com.cn') ? host.slice(0, -3) : host;
  if (!globalHost.endsWith('.neurokaraoke.com')) return value;
  const service = globalHost.slice(0, -'.neurokaraoke.com'.length);
  if (!['api', 'idk', 'storage', 'images', 'socket'].includes(service)) return value;
  const target = globalHost + (china ? '.cn' : '');
  if (target === host) return value;
  return value.replace(/^(\w+:\/\/(?:[^/@]*@)?)[^/:?#]+/, `$1${target}`);
}

let useChinaEndpoints = false;
export function setChinaEndpoints(enabled: boolean): void {
  useChinaEndpoints = enabled;
}
