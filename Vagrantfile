# fetch virtual bridge or device first

def getBridge()
  return %x{ip route show | grep default | awk '{ print $5 }' }.chomp
end

Vagrant.configure("2") do |config|

  # ubuntu image
  config.vm.box = "ubuntu/jammy64"
  config.vm.boot_timeout = 900
  config.vm.box_check_update = false

  # master node preparation
  config.vm.define "master" do |master|
    master.vm.provider "virtualbox" do |masterVB|
      masterVB.name = "master"
      masterVB.memory = 4096
      masterVB.cpus = 4
    end
    master.vm.hostname = "master"
    master.vm.network :public_network, ip: "192.168.56.100",  bridge: getBridge()
  end
  
  # worker node preparation
  config.vm.define "node01" do |worker|
    worker.vm.provider "virtualbox" do |node|
      node.name = "node01"
      node.memory = 4096
      node.cpus = 4
    end
    worker.vm.hostname = "node01"
    worker.vm.network :public_network, ip: "192.168.56.101", bridge: getBridge()
  end
end
