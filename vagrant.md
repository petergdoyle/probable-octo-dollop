
Create the new vm with vagrant
```
$ vagrant up
```

If you see a message like this. It means that there was an issue installing the VirtualBox Guest Additions and you need to reload the vm but it is a good idea to reload the vm anyway before you go into it
```
Copying additional installer modules ...
Installing additional modules ...
VirtualBox Guest Additions: Building the VirtualBox Guest Additions kernel modules.  This may take a while.
VirtualBox Guest Additions: Running kernel modules will not be replaced until the system is restarted
VirtualBox Guest Additions: Starting.
VirtualBox Guest Additions: modprobe vboxsf failed
An error occurred during installation of VirtualBox Guest Additions 5.2.18. Some functionality may not work as intended.
In most cases it is OK that the "Window System drivers" installation failed.
Redirecting to /bin/systemctl start  vboxadd.service
Redirecting to /bin/systemctl start  vboxadd-service.service
Job for vboxadd-service.service failed because the control process exited with error code. See "systemctl status vboxadd-service.service" and "journalctl -xe" for details.
Unmounting Virtualbox Guest Additions ISO from: /mnt
==> default: Checking for guest additions in VM...
    default: The guest additions on this VM do not match the installed version of
    default: VirtualBox! In most cases this is fine, but in rare cases it can
    default: prevent things such as shared folders from working properly. If you see
    default: shared folder errors, please make sure the guest additions within the
    default: virtual machine match the version of VirtualBox you have installed on
    default: your host and reload your VM.
    default:
    default: Guest Additions Version: 5.1.22
    default: VirtualBox Version: 5.2
==> default: Setting hostname...
==> default: Mounting shared folders...
    default: /vagrant => /Users/peter/vagrant/spark-streaming-log-collector
Vagrant was unable to mount VirtualBox shared folders. This is usually
because the filesystem "vboxsf" is not available. This filesystem is
made available via the VirtualBox Guest Additions and kernel module.
Please verify that these guest additions are properly installed in the
guest. This is not a bug in Vagrant and is usually caused by a faulty
Vagrant box. For context, the command attempted was:

mount -t vboxsf -o uid=1000,gid=1000 vagrant /vagrant

The error output from the command was:

/sbin/mount.vboxsf: mounting failed with the error: No such device

```
To reload the vm
```
$ vagrant reload
```

To ssh into the vm
```
$ vagrant ssh

```
