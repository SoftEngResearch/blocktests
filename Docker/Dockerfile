FROM ubuntu:20.04

RUN \
  apt-get update && \
  apt-get install -y software-properties-common && \
# Install Git
  apt-get install -y git && \
# Install python
  apt-get update && \
  apt-get install -y python3 python3-pip python-is-python3 && \
  rm -rf /var/lib/apt/lists/* && \
# Install misc
  apt-get update && \
  apt-get install -y sudo && \
  apt-get install -y vim && \
  apt-get install -y emacs && \  
  apt-get install -y wget && \
  apt-get install -y bc && \
  apt-get install -y cloc && \
  apt-get install -y zip unzip && \
  apt-get install -y locales locales-all && \
  apt-get install -y parallel && \
  apt-get install -y pigz && \
# Install OpenJDK 8
  apt-get install -y openjdk-8-jdk && \
  apt-get install -y openjdk-8-dbg && \
  apt-get install -y gdb && \
  mv /usr/lib/jvm/java-8-openjdk* /usr/lib/jvm/java-8-openjdk

# Set up user (blocktests)
RUN useradd -ms /bin/bash -c "blocktests" blocktests && echo "blocktests:docker" | chpasswd && adduser blocktests sudo

USER blocktests

WORKDIR /home/blocktests/

# Use OpenJDK 8 when building the docker image
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk

RUN \
  echo "If the step below failed, try changing 3.8.9 to 3.8.10"

RUN \
  wget http://mirrors.ibiblio.org/apache/maven/maven-3/3.8.9/binaries/apache-maven-3.8.9-bin.tar.gz && \
  tar -xzf apache-maven-3.8.9-bin.tar.gz && mv apache-maven-3.8.9/ apache-maven/ && \
  rm apache-maven-3.8.9-bin.tar.gz && \
  cd /home/blocktests/ && \
  tail -n +10 ~/.bashrc > ~/tmp-bashrc && \
  mv ~/tmp-bashrc ~/.bashrc && \
  wget https://github.com/eclipse-aspectj/aspectj/releases/download/V1_9_7/aspectj-1.9.7.jar && \
  mkdir aspectj-1.9.7 && /usr/lib/jvm/java-8-openjdk/bin/java -jar aspectj-1.9.7.jar -to aspectj-1.9.7 && rm aspectj-1.9.7.jar && \
  sed -i 's/64M/10240M/g' aspectj-1.9.7/bin/ajc && \
  echo 'JAVAHOME=/usr/lib/jvm/java-8-openjdk' >> ~/.bashrc && \
  echo 'export JAVA_HOME=${JAVAHOME}' >> ~/.bashrc && \
  echo 'export M2_HOME=${HOME}/apache-maven' >> ~/.bashrc && \
  echo 'export MAVEN_HOME=${HOME}/apache-maven' >> ~/.bashrc && \
  echo 'ASPECTJ_DIR=${HOME}/aspectj-1.9.7' >> ~/.bashrc && \
  echo 'export PATH=${M2_HOME}/bin:${JAVAHOME}/bin:${ASPECTJ_DIR}/bin:${ASPECTJ_DIR}/lib/aspectjweaver.jar:${PATH}' >> ~/.bashrc && \
  echo 'export CLASSPATH=${ASPECTJ_DIR}/lib/aspectjtools.jar:${ASPECTJ_DIR}/lib/aspectjrt.jar:${ASPECTJ_DIR}/lib/aspectjweaver.jar:${CLASSPATH}' >> ~/.bashrc && \
  git clone https://github.com/blocktest/oopsla26 block-tests


ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
