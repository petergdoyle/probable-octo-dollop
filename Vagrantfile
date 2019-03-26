# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  # config.vm.box = "centos/7"
  config.vm.box = "petergdoyle/CentOS-7-x86_64-Minimal-1511"
  config.ssh.insert_key = false

  config.vm.provider "virtualbox" do |vb|
 #   vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    vb.customize ["modifyvm", :id, "--cpuexecutioncap", "80"]
    vb.cpus=4
    vb.memory = "8192"
  end

  config.vm.hostname = "challenge.cleverfishsoftware.com"

  config.vm.provision "shell", inline: <<-SHELL


  eval 'docker --version' > /dev/null 2>&1
  if [ $? -eq 127 ]; then
    echo "installing docker and docker-compose..."

    yum -y remove docker docker-common  docker-selinux docker-engine
    yum -y install yum-utils device-mapper-persistent-data lvm2
    yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    rm -fr /etc/yum.repos.d/docker.repo
    yum-config-manager --enable docker-ce-edge
    yum-config-manager --enable docker-ce-test
    yum -y makecache fast
    yum -y install docker-ce

    systemctl start docker
    systemctl enable docker
    groupadd docker

    yum -y install python-pip
    pip install --upgrade pip
    pip install -U docker-compose

    usermod -aG docker vagrant

  else
    echo "docker and docker-compose already installed"
  fi

  # pull docker images
  docker pull bitnami/kafka:latest

  # pull down the bitnami git repo
  if [ ! -d "/vagrant/bitnami-docker-kafka" ]; then
    cd /vagrant
    git clone https://github.com/bitnami/bitnami-docker-kafka.git
    cd -
  fi

  # install openjdk-8
  eval java -version > /dev/null 2>&1
  if [ $? -eq 127 ]; then

    mkdir -pv /usr/java
    yum -y install java-1.8.0-openjdk-headless && yum -y install java-1.8.0-openjdk-devel
    java_home=`alternatives --list |grep jre_1.8.0_openjdk| awk '{print $3}'`
    ln -s "$java_home" /usr/java/default

    export JAVA_HOME='/usr/java/default'
    cat >/etc/profile.d/java.sh <<-EOF
export JAVA_HOME=$JAVA_HOME
EOF

    # register all the java tools and executables to the OS as executables
    install_dir="$JAVA_HOME/bin"
    for each in $(find $install_dir -executable -type f) ; do
      name=$(basename $each)
      alternatives --install "/usr/bin/$name" "$name" "$each" 99999
    done

  else
    echo -e "openjdk-8 already appears to be installed."
  fi

  # install maven 3
  eval 'mvn -version' > /dev/null 2>&1
  if [ $? -eq 127 ]; then

    local_maven_dir="/usr/maven"
    maven_home="$local_maven_dir/default"
    maven_version='3.3.9'
    download_url="http://www-us.apache.org/dist/maven/maven-3/$maven_version/binaries/apache-maven-$maven_version-bin.tar.gz"

    echo "downloading $download_url..."
    if [ ! -d $local_maven_dir ]; then
      mkdir -pv $local_maven_dir
    fi

    cmd="curl -O $download_url \
      && tar -xvf apache-maven-$maven_version-bin.tar.gz -C $local_maven_dir \
      && ln -s $local_maven_dir/apache-maven-$maven_version $maven_home \
      && rm -f apache-maven-$maven_version-bin.tar.gz"
    eval "$cmd"

    export MAVEN_HOME=$maven_home
    MAVEN_HOME=$maven_home
cat >/etc/profile.d/maven.sh <<-EOF
export MAVEN_HOME=$MAVEN_HOME
export PATH=\$PATH:\$MAVEN_HOME/bin
EOF
  else
    echo -e "apache-maven-$maven_version already appears to be installed. skipping."
  fi

  # download local copy of kafka
  if [ ! -d "/vagrant/kafka_2.12-2.1.1" ]; then
    cd /vagrant
    curl -O http://mirrors.ocf.berkeley.edu/apache/kafka/2.1.1/kafka_2.12-2.1.1.tgz
    tar -xvf kafka_2.12-2.1.1.tgz
    rm kafka_2.12-2.1.1.tgz
    cd -
  fi

  if [ ! -d "/vagrant/spark-2.1.1" ]; then
    cd /vagrant
    curl -O https://archive.apache.org/dist/spark/spark-2.1.1/spark-2.1.1-bin-without-hadoop.tgz
    tar -xvf spark-2.1.1-bin-without-hadoop.tgz
    rm spark-2.1.1-bin-without-hadoop.tgz
    mv spark-2.1.1-bin-without-hadoop spark-2.1.1
    cd -
  fi


  # modify environment for vagrant user
  if ! grep -q '^alias cd' /home/vagrant/.bashrc; then
    echo 'alias cd="HOME=/vagrant cd"' >> /home/vagrant/.bashrc
  fi

  # install any additional packages
  yum -y install net-tools telnet git

  yum -y update

  SHELL
end
