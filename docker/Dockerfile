FROM panh199201/alpine:jre-8

MAINTAINER panh1992@163.com

RUN apk add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && apk del tzdata

ADD athena.jar app.jar
ADD config.yml config.yml

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","server","/config.yml"]
