FROM amazoncorretto:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# 인자 설정 부분과 jar 파일 복제 부분 합쳐서 진행해도 무방
COPY ${JAR_FILE} mallook-api.jar

# 실행 명령어
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mallook-api.jar", "--spring.profiles.active=dev"]
