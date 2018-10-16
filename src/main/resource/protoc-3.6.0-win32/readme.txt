Protocol Buffers - Google's data interchange format
Copyright 2008 Google Inc.
https://developers.google.com/protocol-buffers/

This package contains a precompiled binary version of the protocol buffer
compiler (protoc). This binary is intended for users who want to use Protocol
Buffers in languages other than C++ but do not want to compile protoc
themselves. To install, simply place this binary somewhere in your PATH.

If you intend to use the included well known types then don't forget to
copy the contents of the 'include' directory somewhere as well, for example
into '/usr/local/include/'.

Please refer to our official github site for more installation instructions:
  https://github.com/google/protobuf
  
  
、生成代码
在所使用的proto文件路径下打开cmd窗口执行以下命令：

protoc -I=源地址 --java_out=目标地址  源地址/xxx.proto
1
此处生成时会以 proto 里面注明的java_package为路径完整生成，所以目标地址不必包含java_package及之后的路径，比如：option java_package = "com.test.protocol";，那么就会生成com/test/protocol/XXX.java



参数解释：

-I：主要用于指定待编译的 .proto 消息定义文件所在的目录，即可能出现的包含文件的路径，该选项可以被同时指定多个。此处指定的路径不能为空，如果是当前目录，直接使用.，如果是子目录，直接使用子目录相对径，如：foo/bar/baz，如果要编译的文件指定的文件路径为baz/test.proto，那么应这么写-I=foo/bar，而不要一直写到baz。



生成：

D:\>protoc -I=D:\test_protoc --java_out=D:\test_protoc SubscribeReq.proto
