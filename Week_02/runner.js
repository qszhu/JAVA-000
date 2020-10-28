const fs = require('fs')
const child_process = require('child_process')

function run(cmd, args, outStream, errStream) {
  console.debug(cmd, args.join(' '))
  const cp = child_process.spawn(cmd, args)
  cp.stdout.pipe(outStream)
  cp.stderr.pipe(errStream)
  cp.on('exit', () => {
    outStream.end()
    errStream.end()
  })
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
      const outStream = fs.createWriteStream(`${baseName}-stdout.txt`)
      const errStream = fs.createWriteStream(`${baseName}-stderr.txt`)
      run('java', args, outStream, errStream)
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
