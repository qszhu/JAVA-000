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

function main(className) {
  const baseArgs = ['-XX:+PrintGC', '-XX:+PrintGCDateStamps', className]
  for (const [gcName, gcArg] of Object.entries(gcArgs)) {
    for (const [memName, memArg] of Object.entries(memArgs)) {
      const args = [...gcArg, ...memArg, ...baseArgs]
      const baseName = `${className}-${gcName}-${memName}`

      const { stdout, stderr } = run('java', args)
      fs.writeFileSync(`${baseName}-stdout.txt`, stdout)
      fs.writeFileSync(`${baseName}-stderr.txt`, stderr)
    } 
  }
}

if (require.main === module) {
  if (process.argv.length !== 3) {
    console.log('Usage: node runner.js <ClassName>')
    process.exit(1)
  }
  main(process.argv[2])
}
