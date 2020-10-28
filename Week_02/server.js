const net = require("net");

const PORT = 8801;

async function sleep(ms) {
  return new Promise((resolve, reject) => {
    setTimeout(resolve, ms);
  });
}

async function main() {
  const body = "hello node socket!";

  const resp = [
    "HTTP/1.1 200 OK",
    "Content-Type: text/plain; charset=utf-8",
    `Content-Length: ${body.length}`,
    "Connection: close",
    "",
    body,
  ].join("\r\n");

  net
    .createServer(async (socket) => {
      socket.on("error", console.error);
      socket.end(resp);
    })
    .listen(PORT, () => console.log(`Listening on ${PORT}...`))
    .on("error", console.error);
}

if (require.main === module) {
  main().catch(console.error);
}
