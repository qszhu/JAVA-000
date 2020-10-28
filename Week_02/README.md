### GC日志分析

* 下载到的源文件[GCLogAnalysis.java](GCLogAnalysis.java)包含GBK编码的注释，在UTF-8编码的系统中编译错误，可做如下调整：

```bash
$ iconv -f gb18030 -t utf8 GCLogAnalysis.java -o GCLogAnalysis.java
```

* 使用脚本[runner.js](runner.js)批量获得输出

```bash
$ javac GCLogAnalysis.java
$ node runner.js GCLogAnalysis
```

* 测试环境(云服务器)
```bash
$ uname -a
Linux VM-16-2-ubuntu 5.4.0-52-generic #57-Ubuntu SMP Thu Oct 15 10:57:00 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux
```
```bash
$ lscpu
Architecture:                    x86_64
CPU op-mode(s):                  32-bit, 64-bit
Byte Order:                      Little Endian
Address sizes:                   48 bits physical, 48 bits virtual
CPU(s):                          8
On-line CPU(s) list:             0-7
Thread(s) per core:              1
Core(s) per socket:              8
Socket(s):                       1
NUMA node(s):                    1
Vendor ID:                       AuthenticAMD
CPU family:                      23
Model:                           49
Model name:                      AMD EPYC 7K62 48-Core Processor
Stepping:                        0
CPU MHz:                         2595.124
BogoMIPS:                        5190.24
Hypervisor vendor:               KVM
Virtualization type:             full
L1d cache:                       256 KiB
L1i cache:                       256 KiB
L2 cache:                        32 MiB
L3 cache:                        32 MiB
NUMA node0 CPU(s):               0-7
Vulnerability Itlb multihit:     Not affected
Vulnerability L1tf:              Not affected
Vulnerability Mds:               Not affected
Vulnerability Meltdown:          Not affected
Vulnerability Spec store bypass: Vulnerable
Vulnerability Spectre v1:        Mitigation; usercopy/swapgs barriers and __user pointer sanitization
Vulnerability Spectre v2:        Mitigation; Full AMD retpoline, IBPB conditional, STIBP disabled, RSB filling
Vulnerability Srbds:             Not affected
Vulnerability Tsx async abort:   Not affected
Flags:                           fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ht syscall nx mmxext fxsr_opt pdpe1gb rdtscp lm rep_good nopl cpuid ext
                                 d_apicid tsc_known_freq pni pclmulqdq ssse3 fma cx16 sse4_1 sse4_2 x2apic movbe popcnt aes xsave avx f16c rdrand hypervisor lahf_lm cmp_legacy cr8_legacy abm sse4a misal
                                 ignsse 3dnowprefetch osvw topoext ibpb vmmcall fsgsbase bmi1 avx2 smep bmi2 rdseed adx smap clflushopt sha_ni xsaveopt xsavec xgetbv1 arat
```
```bash
$ free -h
              total        used        free      shared  buff/cache   available
Mem:           15Gi       291Mi        13Gi       2.0Mi       1.3Gi        14Gi
Swap:            0B          0B          0B
```
```bash
$ java -version
java version "1.8.0_271"
Java(TM) SE Runtime Environment (build 1.8.0_271-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.271-b09, mixed mode)
```

* 运行一次的结果

GC/Heap | 128M | 512M | 1G | 2G | 4G
--- | --- | --- | --- | --- | ---
serial | [OOM](GCLogAnalysis-serial-128m-stderr.txt) | 12335 | 17020 | 17204 | 16889
parallel | OOM | 10419 | 17983 | 20044 | 20455
cms | OOM | 12448 | 17546 | 17806 | 17071
g1 | OOM | 10848 | 18148 | 16108 | 20219

### 压力测试下的GC

TODO

### 简单HttpServer压测 

* 使用课件上的例子通过wrk压测得到QPS为零：
```
$ wrk http://localhost:8801
Running 10s test @ http://localhost:8801
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     0.00us    0.00us   0.00us    -nan%
    Req/Sec     0.00      0.00     0.00      -nan%
  0 requests in 10.01s, 30.38KB read
  Socket errors: connect 0, read 494, write 0, timeout 0
Requests/sec:      0.00
Transfer/sec:      3.03KB
```

* 通过curl或浏览器访问会返回连接被重置的错误：
```bash
$ curl -i http://localhost:8801
HTTP/1.1 200 OK
Content-Type:text/html;charset=utf-8

curl: (56) Recv failure: Connection reset by peer
hello,nio
```

* Server端代码如下：
```java
    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println();
            printWriter.write("hello,nio");
            printWriter.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
```

* 怀疑是服务端socket过早关闭导致。根据[RFC1122](https://tools.ietf.org/html/rfc1122#page-87)：
```
            A host MAY implement a "half-duplex" TCP close sequence, so
            that an application that has called CLOSE cannot continue to
            read data from the connection.  If such a host issues a
            CLOSE call while received data is still pending in TCP, or
            if new data is received after CLOSE is called, its TCP
            SHOULD send a RST to show that data was lost.
```
即如果服务端未完成接收数据，则TCP会发送RST来告诉客户端发送数据存在丢失。

* 尝试先读完请求数据：
```diff
    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
+
+           BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
+           String line;
+           while ((line = br.readLine()) != null) {
+               System.out.println(line);
+               if (line.isEmpty()) break;
+           }
+
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            ...
```

* 此时通过curl访问正常：
```bash
$ curl -i http://localhost:8801
HTTP/1.1 200 OK
Content-Type:text/html;charset=utf-8

hello,nio
```

* 然而虽然此时wrk可以测出数据，但都被归到了socket error中：
```bash
$ curl -i http://localhost:8801
Running 10s test @ http://localhost:8801
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   389.78ms   57.90ms 406.84ms   93.65%
    Req/Sec    15.20      7.95    30.00     66.93%
  252 requests in 10.01s, 15.50KB read
  Socket errors: connect 0, read 252, write 0, timeout 0
Requests/sec:     25.17
Transfer/sec:      1.55KB
```

* 后课件代码更新，返回了`Content-Length`：
```diff
    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
+            String body = "hello,nio";
+            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
-            printWriter.write("hello,nio");
+            printWriter.write(body);
            printWriter.close()
            ...
```

* 通过curl可正常访问：
```bash
$ curl -i http://localhost:8801
HTTP/1.1 200 OK
Content-Type:text/html;charset=utf-8
Content-Length:9

hello,nio
```

* 但wrk依旧存在socket错误（如果加上前面的读取请求数据的代码，则write错误都会跑到read错误上）：
```bash
$ wrk -i http://localhost:8801
Running 10s test @ http://localhost:8801
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   200.47ms   12.94ms 222.76ms   98.17%
    Req/Sec    24.59      5.48    30.00     97.00%
  492 requests in 10.01s, 38.58KB read
  Socket errors: connect 0, read 314, write 180, timeout 0
Requests/sec:     49.17
Transfer/sec:      3.85KB
```

* 由于HTTP/1.1默认使用长连接，所以如果服务端要关闭连接的话需要通知客户端不使用长连接：
```diff
    private static void service(Socket socket) {
        try {
            Thread.sleep(20);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) break;
            }

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
+            printWriter.println("Connection: close");
            printWriter.println();
            printWriter.write(body);
            printWriter.close()
            ...
```

或是使用HTTP 1.0协议：

```diff
            ...
-            printWriter.println("HTTP/1.1 200 OK");
+            printWriter.println("HTTP/1.0 200 OK");
            ...
```

* 此时wrk没有错误[HttpServer01.java](HttpServer01.java)：
```bash
$ wrk http://localhost:8801
Running 10s test @ http://localhost:8801
  2 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   200.30ms   12.87ms 222.69ms   98.18%
    Req/Sec    24.75      5.10    30.00     52.00%
  495 requests in 10.01s, 47.37KB read
Requests/sec:     49.43
Transfer/sec:      4.73KB
```

* 使用线程[HttpServer02.java](HttpServer02.java)：
```bash
$ wrk -t8 -c40 -d60 http://localhost:8802
Running 1m test @ http://localhost:8802
  8 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.47ms    1.15ms  72.67ms   96.46%
    Req/Sec   244.83     10.35   252.00     86.17%
  117104 requests in 1.00m, 10.94MB read
Requests/sec:   1950.19
Transfer/sec:    186.64KB
```

* 使用线程池[HttpServer03.java](HttpServer03.java)：
```bash
$ wrk -t8 -c40 -d60 http://localhost:8803
Running 1m test @ http://localhost:8803
  8 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.23ms    1.02ms  84.44ms   98.90%
    Req/Sec   247.74      8.43   252.00     94.35%
  118496 requests in 1.00m, 11.07MB read
Requests/sec:   1973.45
Transfer/sec:    188.87KB
```

* 对比[server.js](server.js)（Node.js同步非阻塞）：
```bash
$ node --version
v10.23.0
$ node server.js
```
```bash
$ wrk -t8 -c40 -d60 http://localhost:8805
Running 1m test @ http://localhost:8805
  8 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.56ms    0.90ms  50.57ms   88.16%
    Req/Sec   243.77     14.31   260.00     87.15%
  116606 requests in 1.00m, 13.01MB read
Requests/sec:   1941.92
Transfer/sec:    221.88KB
```

### HttpClient

* [MyHttpClient.java](MyHttpClient.java)
```bash
$ java --version
java 11.0.1 2018-10-16 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.1+13-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.1+13-LTS, mixed mode)
```
```bash
$ java MyHttpClient.java http://localhost:8081
```
