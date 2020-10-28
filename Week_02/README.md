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

* 测试环境(云服务器)
```bash
$ uname -a
Linux VM-16-2-ubuntu 5.4.0-52-generic #57-Ubuntu SMP Thu Oct 15 10:57:00 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux
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
$ free -h
              total        used        free      shared  buff/cache   available
Mem:           15Gi       291Mi        13Gi       2.0Mi       1.3Gi        14Gi
Swap:            0B          0B          0B
$ java -version
java version "1.8.0_271"
Java(TM) SE Runtime Environment (build 1.8.0_271-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.271-b09, mixed mode)
```

* 运行一次的结果

GC/Heap | 128M | 512M | 1G | 2G | 4G
--- | --- | --- | --- | --- | ---
serial | OOM | 1959 | 2866 | 2103 | 2381
parallel | 1085 | 2471 | 2003 | 2259 | 2458
cms | 1729 | 2678 | 3126 | 2896 | 1419
g1 | 1076 | 2751 | 2149 | 4218 | 7180

### 压力测试下的GC

TODO

### HttpClient

```bash
$ java MyHttpClient.java http://localhost:8081
```
