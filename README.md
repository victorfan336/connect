# 远程adb调试手机

## 1.条件
       1）root过的手机：因为我们需要启动adbd端口，所有需要使用adb命令，但是一般手机或用去却没有该权限；

       2）手机或设备与电脑处于一个局域网内

 

## 2.手机或设备安装connect-apk，启动connect-apk
如果我们不安装，而是直接用adb connect ip去连接手机或设备的话，会出现这个错误：
unable to connect to 192.168.1.131:5555: cannot connect to 192.168.1.131:5555: 由于目标计算机积极拒绝，无法连接。 (10061)

该apk主要用于在手机或设备上执行以下命令：
1)setprop service.adb.tcp.port 5555
2)stop adbd
3)start adbd

apk下载链接：https://download.csdn.net/download/fwt336/10676412

## 3.连接手机或设备
使用命令:adb connect ip

