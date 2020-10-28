const fs = require('fs')
const child_process = require('child_process')

function run(cmd, args) {
  console.debug(cmd, args.join(' '))
  return child_process.spawnSync(cmd, args)
}

const gcArgs = {
  serial: ['-XX:+UseSerialGC'],
  parallel: ['-XX:+UseParallelGC'],
  cms: ['-XX:+UseConcMarkSweepGC'],
  g1: ['-XX:+UseG1GC']
}

const memArgs = {
  '128m': ['-Xmx128m'],
  '512m': ['-Xms512m', '-Xmx512m'],
  '1g': ['-Xms1024m', '-Xmx1024m'],
  '2g': ['-Xms2048m', '-Xmx2048m'],
  '4g': ['-Xms4096m', '-Xmx4096m']
}

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
}

if (require.main === module) {
  main()
}
