package com.texchange.eth;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;
import rx.Subscription;

import java.io.Serializable;
import java.math.BigInteger;

public class EthFilter {

    private static Web3j web3j;

    public static void main(String[] args) {
        web3j = Web3j.build(new HttpService(EthService.URL));
        /**
         * 新区块监听
         */
        //newBlockFilter(web3j);
        /**
         * 新交易监听
         */
       // newTransactionFilter(web3j);
        /**
         * 遍历旧区块、交易
         */
        replayFilter(web3j);
        /**
         * 从某一区块开始直到最新区块、交易
         */
       //catchUpFilter(web3j);
    }

    private static void newBlockFilter(Web3j web3j) {
        Subscription subscription = web3j.
                blockObservable(false).
                subscribe(block -> {
                    System.out.println("新区块：" + block.getBlock().getNumber());
                });
    }

    private static void newTransactionFilter(Web3j web3j) {
        Subscription subscription = web3j.
                transactionObservable().
                subscribe(transaction -> {
                    System.out.println("新交易的hash值：" + transaction.getHash());
                });
    }

    private static void replayFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(11000);
        BigInteger endBlock = BigInteger.valueOf(2010000);
        /**
         * 遍历旧区块
         */
        Subscription subscription = web3j.
                replayBlocksObservable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        false).
                subscribe(ethBlock -> {
                    System.out.println("旧区块："+ ethBlock.getBlock().getNumber());
                });

        /**
         * 遍历旧交易
         */
        Subscription subscription1 = web3j.
                replayTransactionsObservable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock)).
                subscribe(transaction -> {
                    System.out.println("旧交易hash值：" + transaction.getHash());
                });
    }

    private static void catchUpFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(2000000);

        /**
         * 遍历旧区块，监听新区块
         */
        Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
                DefaultBlockParameter.valueOf(startBlock), false)
                .subscribe(block -> {
                    System.out.println("监听新区块：" + block.getBlock().getNumber());
                });

        /**
         * 遍历旧交易，监听新交易
         */
        Subscription subscription2 = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                DefaultBlockParameter.valueOf(startBlock))
                .subscribe(tx -> {
                    System.out.println("监听新交易hash值："+tx.getHash());
                });
    }
}
