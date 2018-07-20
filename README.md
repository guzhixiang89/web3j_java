注意点：
1、启用私有链时候需要新增虚拟机地址，端口默认8545

./geth --datadir "./chain" --rpc --rpcaddr "192.168.242.128" --nodiscover --rpcapi "db,eth,net,web3,miner,personal,admin" console 2>>geth.log

2、关闭虚拟机的防火墙：systemctl stop firewalld
