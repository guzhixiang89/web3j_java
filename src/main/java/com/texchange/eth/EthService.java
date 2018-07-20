package com.texchange.eth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

public class EthService implements Serializable{

    /**
     * geth节点可调用的json-rpc接口地址和端口
     */
    public static final String URL = "http://192.168.242.128:8545";
    private static final long serialVersionUID = -7508441603463696792L;

    /**
     * 初始化web3j普通api调用
     *
     * @return web3j
     */
    public static Web3j initWeb3j() {
        return Web3j.build(getService());
    }

    /**
     * 初始化personal级别的操作对象
     *
     * @return Geth
     */
    public static Geth initGeth() {
        return Geth.build(getService());
    }

    /**
     * 初始化admin级别操作的对象
     *
     * @return Admin
     */
    public static Admin initAdmin() {
        return Admin.build(getService());
    }

    /**
     * 通过http连接到geth节点
     *
     * @return
     */
    private static HttpService getService() {
        return new HttpService(URL);
    }

    /**
     * 输入密码创建地址
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param password 密码（建议同一个平台的地址使用一个相同的，且复杂度较高的密码）
     * @return 地址hash
     * @throws IOException
     */
    public static String newAccount(String password) throws IOException {
        Admin admin = initAdmin();
        Request<?, NewAccountIdentifier> request = admin.personalNewAccount(password);
        NewAccountIdentifier result = request.send();
        return result.getAccountId();
    }

    /**
     * 获得当前区块高度
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @return 当前区块高度
     * @throws IOException
     */
    public static BigInteger getCurrentBlockNumber() throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        return request.send().getBlockNumber();
    }

    /**
     * 解锁账户，发送交易前需要对账户进行解锁
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param address  地址
     * @param password 密码
     * @param duration 解锁有效时间，单位秒
     * @return
     * @throws IOException
     */
    public static Boolean unlockAccount(String address, String password, BigInteger duration) throws IOException {
        Admin admin = initAdmin();
        Request<?, PersonalUnlockAccount> request = admin.personalUnlockAccount(address, password, duration);
        PersonalUnlockAccount account = request.send();
        return account.accountUnlocked();
    }

    /**
     * 账户解锁，使用完成之后需要锁定
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param address
     * @return
     * @throws IOException
     */
    public static Boolean lockAccount(String address) throws IOException {
        Geth geth = initGeth();
        Request<?, BooleanResponse> request = geth.personalLockAccount(address);
        BooleanResponse response = request.send();
        return response.success();
    }

    /**
     * 根据hash值获取交易
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param hash
     * @return
     * @throws IOException
     */
    public static EthTransaction getTransactionByHash(String hash) throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(hash);
        return request.send();
    }

    /**
     * 获得ethblock
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param blockNumber 根据区块编号
     * @return
     * @throws IOException
     */
    public static EthBlock getBlockEthBlock(Integer blockNumber) throws IOException {
        Web3j web3j = initWeb3j();

        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(blockNumber);
        Request<?, EthBlock> request = web3j.ethGetBlockByNumber(defaultBlockParameter, true);
        EthBlock ethBlock = request.send();
        return ethBlock;
    }

    /**
     * 发送交易并获得交易hash值
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param transaction
     * @param password
     * @return
     * @throws IOException
     */
    public static String sendTransaction(Transaction transaction, String password) throws IOException {
        Admin admin = initAdmin();
        Request<?, EthSendTransaction> request = admin.personalSendTransaction(transaction, password);
        EthSendTransaction ethSendTransaction = request.send();
        return ethSendTransaction.getTransactionHash();

    }

    /**
     * 指定地址发送交易所需nonce获取
     *request.send()同步接口，如果异步 request.sendAsync()，并且需要通过监听器获取结果的通知
     * @param address 待发送交易地址
     * @return
     * @throws IOException
     */
    public static BigInteger getNonce(String address) throws IOException {
        Web3j web3j = initWeb3j();
        Request<?, EthGetTransactionCount> request = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST);
        return request.send().getTransactionCount();
    }
}