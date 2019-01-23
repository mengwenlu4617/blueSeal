package com.bjzhijian.bluetoothseal.intelligentseal.bletooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bjzhijian.bluetoothseal.intelligentseal.callback.OnLinkStatusCallBack;
import com.fastwork.library.mutils.MLogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by M on 2018/11
 * 蓝牙服务 Gatt
 */

public class MGattAdapter extends BluetoothGattCallback {

    private String TAG = MGattAdapter.class.getSimpleName();
    private Context mContext;
    // 组包,拆包 需要的参数
    private int sendNum = 0;
    private List<byte[]> bodyByteList = new ArrayList<>();// body 列表
    private byte[] headerByt = new byte[3];// 要发送的数据列表

    private OnLinkStatusCallBack linkStatusCallBack;
    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;// 蓝牙的适配
    private BluetoothGatt mBluetoothGatt; // 返回中央的状态和周边提供的数据
    //将被绑定到可扩展的列表视图的数据结构
    private BluetoothGattCharacteristic write = null/*, read = null*/;

    private final int SEND_MSG_TO_DEVICE = 111;// 发送数据

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_MSG_TO_DEVICE:
                    if (write != null && mBluetoothGatt != null) {
                        write.setValue(headerByt);
                        mBluetoothGatt.writeCharacteristic(write);
                    }
                    break;
                case BluetoothGatt.GATT_SUCCESS:
                    if (sendNum < bodyByteList.size()) {
                        write.setValue(bodyByteList.get(sendNum));
                        mBluetoothGatt.writeCharacteristic(write);
                        sendNum++;
                        Log.e(TAG, " 总包数 = " + bodyByteList.size() + " ****  发送第 " + sendNum + " 包");
                    }else {
                        if (linkStatusCallBack != null) {
                            linkStatusCallBack.onSendDataFinish();
                        }
                    }
                    break;
            }
        }
    };

    public MGattAdapter(Context context, BluetoothAdapter bluetoothAdapter) {
        this.mContext = context;
        this.mBluetoothAdapter = bluetoothAdapter;
    }

    private void initializeAdapter() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager != null) {
                mBluetoothAdapter = mBluetoothManager.getAdapter();
            }
        }
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    /**
     * 连接远程蓝牙设备
     *
     * @param address 设备的Mac地址
     */
    public void connectDevice(final String address, OnLinkStatusCallBack linkStatusCallBack) {
        this.linkStatusCallBack = linkStatusCallBack;
        if (mBluetoothAdapter == null) {
            MLogUtil.e(TAG, "connectDevice  BluetoothAdapter is  null");
            initializeAdapter();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            MLogUtil.e(TAG, "connectDevice  adress  is  empty");
            return;
        }
        closeGatt();// 连接之前先释放下资源
        /* 获取远端的蓝牙设备 */
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            MLogUtil.e(TAG, "connectDevice  device  is not find , linkFail");
            return;
        }
        /* 调用device中的connectGatt连接到远程设备 （创建新的连接）*/
        mBluetoothGatt = device.connectGatt(mContext, false, this);
    }

    /**
     * 断开正在连接的蓝牙设备
     */
    public void disconnectDevice() {
        if (mBluetoothAdapter == null) {
            MLogUtil.e(TAG, "disconnectDevice BluetoothAdapter not initialized");
            return;
        }
        if (mBluetoothGatt == null) {
            MLogUtil.e(TAG, "disconnectDevice mBluetoothGatt not initialized");
            return;
        }
        // 先断开连接，在关闭BluetoothGatt，释放资源
        mBluetoothGatt.disconnect();
        closeGatt();
        //连接断开
        if (linkStatusCallBack != null) {
            linkStatusCallBack.onDisconnectFailed(0);
        }
    }

    // 关闭 BluetoothGatt ；释放资源
    private void closeGatt() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    /**
     * 蓝牙状态变化
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        MLogUtil.e(TAG, "--  onConnectionStateChange  --:   status = " + status);
        MLogUtil.e(TAG, "--  onConnectionStateChange  --:   newState =" + newState);
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                boolean diSer = gatt.discoverServices();
                if (!diSer) {
                    try {
                        Thread.sleep(300);
                        diSer = gatt.discoverServices();
                        MLogUtil.e(TAG, "有时候发现服务不回调,需延时 " + diSer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    //连接断开
                    if (linkStatusCallBack != null) {
                        linkStatusCallBack.onDisconnectFailed(0);
                    }
                } else {
                    //连接失败
                    if (linkStatusCallBack != null) {
                        linkStatusCallBack.onDisconnectFailed(-1);
                    }
                }
                break;
        }
    }

    /**
     * 发现蓝牙服务
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        MLogUtil.e(TAG, "--  onServicesDiscovered  --:   status = " + status);
        switch (status) {
            case BluetoothGatt.GATT_SUCCESS:
                getGattServices(gatt);// 获取可写参数
                if (linkStatusCallBack != null) {
                    linkStatusCallBack.onDisconnectSuccess();
                }
                break;
        }
    }

    /**
     * 蓝牙设备的特征值改变
     */
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        //从特征值获取数据
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            MLogUtil.e(TAG, "--  onCharacteristicChanged    ==  ");
            getMessage(data);
        }
    }

    /**
     * 蓝牙设备的特征值可读
     */
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        MLogUtil.e(TAG, "--  onCharacteristicRead  --:   status = " + status);
    }

    /**
     * 蓝牙设备的特征值可写
     */
    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
//        MLogUtil.e(TAG, "--  onCharacteristicWrite  --:   status = " + status);
        Message message = mHandler.obtainMessage(status);
        mHandler.sendMessage(message);
    }

    /**
     * 蓝牙设备的信号
     */
    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        MLogUtil.e(TAG, "--  onReadRemoteRssi  --:   rssi = " + rssi);
        if (linkStatusCallBack != null) {
            linkStatusCallBack.onReadRemoteRssi(rssi);
        }
    }

    /**
     * 设置特征值变化通知
     */
    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null) {
            MLogUtil.d(TAG, "setCharacteristicNotification mBluetoothAdapter not initialized");
            return;
        }
        if (mBluetoothGatt == null) {
            MLogUtil.d(TAG, "setCharacteristicNotification mBluetoothGatt not initialized");
            return;
        }
        boolean b = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        if (b) {
            List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
            if (descriptorList != null && descriptorList.size() > 0) {
                for (BluetoothGattDescriptor descriptor : descriptorList) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
        // (读取特征值)
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * 获取蓝牙相关的服务
     **/
    private void getGattServices(BluetoothGatt gatt) {
        if (gatt == null) {
            MLogUtil.d(TAG, "getGattServices gatt not initialized");
            return;
        }
        List<BluetoothGattService> gattServiceList = gatt.getServices();
        if (gattServiceList != null && gattServiceList.size() > 0) {
            //可用的总协定服务的循环。
            for (BluetoothGattService gattService : gattServiceList) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                    // 可用的特性循环。
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        String uuid = gattCharacteristic.getUuid().toString();
                        if (!TextUtils.isEmpty(uuid)) {
                            if (uuid.startsWith("0000abf2")) {
//                                read = gattCharacteristic;
                                setCharacteristicNotification(gattCharacteristic);
                            } else if (uuid.startsWith("0000abf3")) {
                                write = gattCharacteristic;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理相应的服务
     *
     * @param sendMsg 指令数据
     *                向蓝牙设备发送数据
     */
    public void displayGattServices(String sendMsg) {
        if (mBluetoothGatt == null) {
            MLogUtil.d(TAG, "displayGattServices mBluetoothGatt not initialized");
            return;
        }
        if (TextUtils.isEmpty(sendMsg)) {
            MLogUtil.d(TAG, "displayGattServices sendMsg is  Null");
            return;
        }
        if (write == null) {
            List<BluetoothGattService> gattServiceList = mBluetoothGatt.getServices();
            if (gattServiceList != null && gattServiceList.size() > 0) {
                //可用的总协定服务的循环。
                for (BluetoothGattService gattService : gattServiceList) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                    if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                        // 可用的特性循环。
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            String uuid = gattCharacteristic.getUuid().toString();
                            if (!TextUtils.isEmpty(uuid)) {
                                if (uuid.startsWith("0000abf2")) {
                                    setCharacteristicNotification(gattCharacteristic);
                                } else if (uuid.startsWith("0000abf3")) {
                                    write = gattCharacteristic;
                                    regroupData(sendMsg);
                                    if (headerByt.length > 2) {
                                        if (headerByt[2] == (byte) bodyByteList.size()) {
                                            Message msg = mHandler.obtainMessage(SEND_MSG_TO_DEVICE);
                                            mHandler.sendMessage(msg);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            regroupData(sendMsg);
            if (headerByt.length > 2) {
                if (headerByt[2] == (byte) bodyByteList.size()) {
                    Message msg = mHandler.obtainMessage(SEND_MSG_TO_DEVICE);
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    // (重组数据)组包
    private void regroupData(String sendMessage) {
        sendNum = 0;
        int start = 0;// 开始的位置
        int subCount = 17;// 截取的数量
        bodyByteList.clear();
        final byte[] bytes = sendMessage.getBytes();
        for (byte aByte : bytes) {
            if (start >= bytes.length) return;
            //截取数据包range
            if ((bytes.length - start) < subCount) {
                subCount = bytes.length - start;
            }
            // 拼接包体
            byte[] bodyByte = new byte[20];
            //运用 arraycopy 方法(截取byte数组)
            //  源数组   源数组要复制的起始位置   目的数组   目的数组放置的起始位置    复制的长度
            System.arraycopy(bytes, start, bodyByte, 3, subCount);
            // 累加
            start += subCount;
            // 赋值前三个
            bodyByte[0] = 0x04;
            bodyByte[1] = 0x01;
            if (start % 17 == 0) {
                bodyByte[2] = (byte) (start / 17);
            } else {
                bodyByte[2] = (byte) ((start / 17) + 1);
            }
            bodyByteList.add(bodyByte);
            // 拼接 headerByte
            headerByt[0] = 0x04;
            headerByt[1] = 0x00;
            headerByt[2] = (byte) bodyByteList.size();
        }
    }

    // 获得数据后处理数组
    private int bodyNum = 0;// 包体数量
    private byte[] allByte = new byte[]{};

    private void getMessage(byte[] bytes) {
        Log.e(TAG, "  收到的byte  = " + Arrays.toString(bytes));
        if (bytes.length > 0) {// 数据必须大于0
            // 判断包头 4 0 + 包数
            if (bytes[0] == 0x04 && bytes[1] == 0x00) {
                bodyNum = bytes[2];
                allByte = new byte[bodyNum * 17];
            } else if (bytes[0] == 0x04 && bytes[1] == 0x01) {
                int page = bytes[2];
                try {
                    System.arraycopy(bytes, 3, allByte, (page - 1) * 17, 17);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (bodyNum == page) {
                    String message = new String(allByte);
                    if (!TextUtils.isEmpty(message)) {
                        if (linkStatusCallBack != null) {
                            linkStatusCallBack.onReciveData(message);
                        }
                    }
//                    String messageSave = new String(allByte);
//                    try {
//                        if (!TextUtils.isEmpty(message) && isAes) {
//                            message = AesManager.decrypt(messageSave, key, iv);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (!TextUtils.isEmpty(message)) {
//                        int end = message.lastIndexOf("}") + 1;
//                        if (end > 1) {
//                            Log.e(TAG, "  最终数据  = " + message.substring(0, end));
//                            String result = JsonToModel.msgToModelByStr(message.substring(0, end));
//                            if (serviceListener != null && result != null) {
//                                serviceListener.onReceiveData(result);
//                            }
//                        }
//                    }
                }
            }
        }
    }
}
