# fetch virtual bridge or device first

def getBridge()
   return %x{ip route show | grep default | grep -w "192.168" | awk '{print $5}'}.chomp
end

Vagrant.configure("2") do |config|

  # ubuntu image
  config.vm.box = "generic/ubuntu2204"
  config.vm.box_version = "4.3.12"
  config.vm.boot_timeout = 900
  config.vm.box_check_update = false

  # master node preparation
  config.vm.define "cks-master" do |controlplane|
    controlplane.vm.provider "virtualbox" do |controlplaneVB|
      controlplaneVB.name = "controlplane"
      controlplaneVB.memory = 5120
      controlplaneVB.cpus = 2
    end
    controlplane.vm.hostname = "controlplane"
    controlplane.vm.network :public_network, ip: "192.168.0.30",  bridge: getBridge()
  end

  # worker node preparation
  config.vm.define "cks-node01" do |worker|
    worker.vm.provider "virtualbox" do |workerVM|
      workerVM.name = "cks-node01"
      workerVM.memory = 4096
      workerVM.cpus = 2
    end
    worker.vm.hostname = "node01"
    worker.vm.network :public_network, ip: "192.168.0.31", bridge: getBridge()
  end

end