package com.bjzhijian.bluetoothseal.intelligentseal.bletooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;

import com.bjzhijian.bluetoothseal.intelligentseal.callback.OnConnectCallBack;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.OnLinkStatusCallBack;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.OnRequestCallBack;
import com.fastwork.library.mutils.AesUtil;
import com.fastwork.library.mutils.MLogUtil;

import java.util.UUID;

/**
 * Created by lenovo on 2019/1/3.
 * 蓝牙设备管理 （单例）
 */

public class BleManager {

    private Context mContext;
    private String mDeviceAddress = "";
    private boolean isConnect = false;
    private MGattAdapter mGattAdapter;
    @SuppressLint("StaticFieldLeak")
    private static BleManager mBleManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;// 系统蓝牙的适配
    private OnConnectCallBack onConnectCallBack;// 单例蓝牙连接的接口回调
    private OnRequestCallBack onRequestCallBack;// 指令请求的接口回调
    private String aesKey = "", aesIv = "";// 需要加密解密的秘钥
    private int linkCount = 0, reLinkCount = 3; // 重新连接次数
    private long reLinkTime = 2 * 1000;// 重新连接的时间间隔

    private Handler searchH = new Handler();// 搜索附近蓝牙设备的Handler
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mBluetoothAdapter.stopLeScan(leScanCallback);
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE10004);
            }
        }
    };

    // 获取单例
    public static BleManager getBleManager(Context context) {
        if (mBleManager == null) {
            synchronized (BleManager.class) {
                if (mBleManager == null) {
                    mBleManager = new BleManager(context);
                }
            }
        }
        return mBleManager;
    }

    // 构造
    private BleManager(Context context) {
        this.mContext = context.getApplicationContext();
        if (isSupportBlue()) {
            BluetoothManager mBluetoothManager = null;
            if (mBluetoothManager == null) {
                mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                if (mBluetoothManager != null) {
                    mBluetoothAdapter = mBluetoothManager.getAdapter();
                }
            }
            if (mBluetoothAdapter == null) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            // 将获取的 BluetoothAdapter 添加到 mGattAdapter
            mGattAdapter = new MGattAdapter(mContext, mBluetoothAdapter);
        } else {
            MLogUtil.e("手机设备不支持低功耗蓝牙");
        }
    }

    // 设置秘钥
    public void setAesKeyAndAesIv(String aesKey, String aesIv) {
        this.aesKey = aesKey;
        this.aesIv = aesIv;
    }

    // 设置重连次数
    public void setReLinkCount(int reLinkCount) {
        if (reLinkCount < 3) {
            this.reLinkCount = 3;
        } else {
            this.reLinkCount = reLinkCount;
        }
    }

    // 设置重连时间间隔
    public void setReLinkTime(long reLinkTime) {
        if (reLinkTime < 2000) {
            this.reLinkTime = 2000;
        } else {
            this.reLinkTime = reLinkTime;
        }
    }

    // 单例蓝牙连接的接口回调
    private void setOnConnectCallBack(OnConnectCallBack onConnectCallBack) {
        this.onConnectCallBack = onConnectCallBack;
    }

    // 设置指令请求函数的接口回调
    private void setOnRequestCallBack(OnRequestCallBack onRequestCallBack) {
        this.onRequestCallBack = onRequestCallBack;
    }

    // 判断手机硬件是否支持蓝牙
    private boolean isSupportBlue() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    // 设备蓝牙是否打开
    public boolean isOpenBle() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }
    //
    public void openBle(){
        if (mBluetoothAdapter != null){
            mBluetoothAdapter.enable();
        }
    }

    /**
     * 连接指定mac地址的设备
     *
     * @param mac 设备mac地址
     */
    public void connectByMac(String mac, OnConnectCallBack callBack) {
        setOnConnectCallBack(callBack);
        if (mBluetoothAdapter == null) {
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE10001);
            }
        } else if (TextUtils.isEmpty(mac)) {
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE10002);
            }
        } else if (mac.length() < 12 || mac.length() > 20) {
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE10014);
            }
        } else if (!isOpenBle()) {
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE10003);
            }
        } else {
            this.mDeviceAddress = mac.replace("-", ":");
            disconnectService();
            scanBleDevice(true);
        }
    }

    // 断开连接
    public void disconnectService() {
        if (isConnect && mGattAdapter != null) {
            mGattAdapter.disconnectDevice();
        }
    }

    /**
     * 搜索蓝牙设备
     */
    private void scanBleDevice(boolean isSearch) {
        if (isSearch) {
            searchH.postDelayed(runnable, 2000);
            UUID[] serviceUuids = {UUID.fromString("0000abf3-0000-1000-8000-00805f9b34fb")};
            // 开始扫描
            mBluetoothAdapter.startLeScan(serviceUuids, leScanCallback);
        } else {
            searchH.removeCallbacks(runnable);
            // 停止扫描
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }
    /**
     * 搜索蓝牙设备监听
     */
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            MLogUtil.e("--->搜索到的蓝牙 Mac：", device.getAddress());
            if (mDeviceAddress.equals(device.getAddress())) {
                scanBleDevice(false);
                if (mGattAdapter != null) {
                    linkCount++;
                    mGattAdapter.connectDevice(mDeviceAddress, linkStatusCallBack);
                }
            }
        }
    };
    /**
     * 连接蓝牙设备状态监听
     */
    private OnLinkStatusCallBack linkStatusCallBack = new OnLinkStatusCallBack() {
        @Override
        public void onDisconnectSuccess() {
            isConnect = true;
            if (onConnectCallBack != null) {
                onConnectCallBack.onConnectResult(CodeDesc.CODE1000);
            }
        }

        @Override
        public void onDisconnectFailed(int status) {
            isConnect = false;
            if (status == 0) {
                if (onConnectCallBack != null) {
                    onConnectCallBack.onConnectResult(CodeDesc.CODE1001);
                }
            } else {
                if (linkCount > reLinkCount) {
                    if (onConnectCallBack != null) {
                        onConnectCallBack.onConnectResult(CodeDesc.CODE1002);
                    }
                } else {
                    scanBleDevice(true);
                }
            }
        }

        @Override
        public void onSendDataFinish() {
            MLogUtil.e("数据发送成功");
        }

        @Override
        public void onReciveData(String data) {
            try {
                String msgData = AesUtil.decrypt(data, AesUtil.KEY, AesUtil.IV);
                MLogUtil.e(msgData);
                if (!TextUtils.isEmpty(msgData) && msgData.contains("}")) {
                    int end = msgData.lastIndexOf("}") + 1;
                    if (end > 1) {
                        MLogUtil.e("  最终数据  = " + msgData.substring(0, end));
                        if (onRequestCallBack != null) {
                            onRequestCallBack.onRequestSuccess(msgData.substring(0, end));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReadRemoteRssi(int rssi) {

        }
    };

}
