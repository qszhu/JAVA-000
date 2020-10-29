const fs = require('fs')

const { run, gcArgs, memArgs, extractData } = require('./utils')

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
  const pattern = /共生成对象次数:(\d+)/
  extractData(className, pattern)
}

if (require.main === module) {
  if (process.argv.length !== 3) {
    console.log('Usage: node runner.js <ClassName>')
    process.exit(1)
  }
  main(process.argv[2])
}
