const fs = require('fs')
const child_process = require('child_process')
 
const { run, gcArgs, memArgs, extractData } = require('./utils')

function main() {
  const name = 'gateway-server'
  const baseArgs = ['-jar', 'gateway-server-0.0.1-SNAPSHOT.jar']

  for (const [gcName, gcArg] of Object.entries(gcArgs)) {
    for (const [memName, memArg] of Object.entries(memArgs)) {
      const args = [...gcArg, ...memArg, ...baseArgs]
      const baseName = `${name}-${gcName}-${memName}`

      const server = child_process.spawn('java', args)
      const { stdout, stderr } = run('wrk', ['-t8', '-c40', '-d60', 'http://localhost:8088/api/hello'])
      fs.writeFileSync(`${baseName}-stdout.txt`, stdout)
      fs.writeFileSync(`${baseName}-stderr.txt`, stderr)
      process.kill(server.pid, 'SIGINT')
    } 
  }

  const pattern = /Requests\/sec:\W+(\d+\.\d*)/
  extractData(name, pattern)
}

if (require.main === module) {
  main()
}
