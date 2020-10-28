### GC日志分析

* 下载到的源文件包含GBK编码的注释，在UTF-8编码的系统中编译错误，可做如下调整：

```bash
$ iconv -f gb18030 -t utf8 GCLogAnalysis.java -o GCLogAnalysis.java
```

* 使用脚本批量获得输出

```bash
$ javac GCLogAnalysis.java
$ node runner.js GCLogAnalysis
```

* 运行结果
TODO

### 压力测试下的GC

TODO

### HttpClient

```bash
$ java MyHttpClient.java http://localhost:8081
```
