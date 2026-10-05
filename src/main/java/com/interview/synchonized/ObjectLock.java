package com.interview.synchonized;


import com.interview.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j
public class ObjectLock {
    private Integer amount = 0;

    private boolean flag;

    public void increase() {
        synchronized (this) {
            amount++;
        }
    }

    public Integer getAmount() {
        return amount;
    }

    public String hexHash() {
        int hashCode = this.hashCode();

        byte[] hashCode_LE = ByteUtil.int2Bytes(hashCode);

        return ByteUtil.byteToHex(hashCode_LE);
    }

    public String binaryHash() {
        int hashCode = this.hashCode();

        byte[] hashCode_LE = ByteUtil.int2Bytes(hashCode);

        StringBuilder builder = new StringBuilder();
        for (byte b : hashCode_LE) {
            builder.append(ByteUtil.byte2BinaryString(b));
            builder.append(" ");
        }
        return builder.toString();
    }

    public String hexThreadId() {
        long threadId = Thread.currentThread().getId();

        byte[] threadId_LE = ByteUtil.long2Bytes(threadId);

        return ByteUtil.byteToHex(threadId_LE);
    }

    public String binaryThreadId() {
        long threadId = Thread.currentThread().getId();

        byte[] threadId_LE = ByteUtil.long2Bytes(threadId);

        StringBuilder builder = new StringBuilder();
        for (byte b : threadId_LE) {
            builder.append(ByteUtil.byte2BinaryString(b));
            builder.append(" ");
        }
        return builder.toString();
    }

    public void printSelf() {
        log.info("lock hexHash={}", hexHash());
        log.info("lock binaryHash={}", binaryHash());
        String printable = ClassLayout.parseInstance(this).toPrintable();
        log.info("lock printable={}", printable);
    }

    public void printObjectStruct() {
        String printable = ClassLayout.parseInstance(this).toPrintable();

        log.info("lock printable={}", printable);
    }
}
