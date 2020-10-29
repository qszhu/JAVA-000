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

function toMarkdown(table) {
  const VL = ' | '
  const sp = new Array(table[0].length).fill('---').join(VL)
  const res = table.map(row => row.join(VL))
  res.splice(1, 0, sp)
  return res.join('\n')
}

function extractData(name, pattern) {
  const table = []
  const header = ['GC/Heap size', ...Object.keys(memArgs)]
  table.push(header)

  for (const [gcName, gcArg] of Object.entries(gcArgs)) {
    const row = [gcName]

    for (const [memName, memArg] of Object.entries(memArgs)) {
      const baseName = `${name}-${gcName}-${memName}`

      const stdout = fs.readFileSync(`${baseName}-stdout.txt`)
      const stderr = fs.readFileSync(`${baseName}-stderr.txt`)

      if (stderr.length === 0) {
        stdout.toString().match(pattern)
        row.push(RegExp.$1)
      } else {
        row.push('Error')
      }
    } 
    table.push(row)
  }

  console.log(table)
  const md = toMarkdown(table)
  fs.writeFileSync(`${name}.md`, md)
}

module.exports = { run, gcArgs, memArgs, extractData }