###构建docker镜像
cd /root/project/siam-finance/siam-system/system-provider

docker build -t siam-finance:v1.0 .

###先杀死原程序进程
docker stop siam-finance

docker rm siam-finance

###运行镜像
# docker run -d -p 9630:9630 -p 9631:9041 --name siam-finance -v /home/dockerdata/oap/volume/siam-system/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-finance:v1.0
docker run -d -p 9630:9630 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=local" --name siam-finance siam-finance:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-finance:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-finance/siam-finance:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-finance/siam-finance:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-finance
  exit 1
fi