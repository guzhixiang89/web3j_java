package com.texchange.eth;


import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class EthServiceTest {

    // GAS价格
    public static BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    // GAS上限
    public static BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000L);

    // 交易费用
    public static BigInteger GAS_VALUE = BigInteger.valueOf(1300_000L);;

    public static void  main(String[] arg) throws IOException {

//        String qbAddr = EthService.newAccount("123456");
//        System.out.println(qbAddr);

     //   System.out.println(EthService.getCurrentBlockNumber());
        // System.out.println(EthService.getBlockEthBlock(2536));
        //System.out.println(EthService.getCurrentBlockNumber());

        try{
            boolean flag = EthService.unlockAccount("0x9fc9f4f4a0723e66d783b8c7a035cdb087cbd1e2", "123456", new BigInteger(1800 + ""));
            System.out.println(flag);

            BigInteger value = Convert.toWei("12", Convert.Unit.ETHER).toBigInteger();

           // Web3j web3j=EthService.initWeb3j();
            //Credentials credentials = WalletUtils.loadCredentials("123456", "D:\\Ethereum\\blockchain\\keystore\\UTC--2018-04-26T06-04-02.691651400Z--dbaea9b9d0b4d9dfff546466c89de34c9711a942");

            BigInteger nonce = EthService.getNonce("0x9fc9f4f4a0723e66d783b8c7a035cdb087cbd1e2");

            Transaction transaction  =Transaction.createEtherTransaction("0x9fc9f4f4a0723e66d783b8c7a035cdb087cbd1e2",
                    nonce, GAS_PRICE, GAS_LIMIT, "0xf2dd9451b6ffbafb01d1e69a7b7b3f5f6b1f2927", value);


//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String inputString = "hello eth";
//            String hexValue = Numeric.toHexString(inputString.getBytes());
            //EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            String hash =EthService.sendTransaction(transaction,"123456");
            System.out.println(hash);
            EthTransaction ethTransaction =null;
            if(null !=hash){
                ethTransaction = EthService.getTransactionByHash(hash);
            }
            System.out.println(ethTransaction);

            Boolean lock = EthService.lockAccount("0x9fc9f4f4a0723e66d783b8c7a035cdb087cbd1e2");
            System.out.println(lock);
        }catch (Exception e){
            System.out.println("----有错误，请查看查看------");
        }
    }
}
