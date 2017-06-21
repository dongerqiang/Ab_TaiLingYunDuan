package com.ananda.tailing.bike.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ananda.tailing.bike.util.CommonUtils;

/**
 * @package com.adinnet.android.bluetooth
 * @description:
 * @version 1.0
 * @author JackieCheng
 * @email xiaming5368@163.com
 * @date 2013-12-12 下午4:39:11
 */
public class BluetoothChatService {

	/** 调试 */
	private static final String TAG = "BluetoothChatService";
	
	/** 连接串口的UUID */
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static StringBuffer hexString = new StringBuffer();
	/** 适配器成员 */
	private final  BluetoothAdapter mAdapter;
	private Handler mHandler;
	
	/** 连接socket的线程,如果断开就执行 BluetoothChatService.this.start(),这段句代码来重新获取socket */
	private ConnectThread mConnectThread;
	
	/** 这个线程就是一直运行着来接受输入输出数据的线程,当ConnectThread连接之后就开始执行他. */
	private ConnectedThread mConnectedThread;
	private int mState;
	
	/** 常数,指示当前的连接状态  */
	public static final int STATE_NONE = 0; // 当前没有可用的连接
	public static final int STATE_LISTEN = 1; // 现在侦听传入的连接
	public static final int STATE_CONNECTING = 2; // 现在开始传出联系
	public static final int STATE_CONNECTED = 3; // 现在连接到远程设备
	public static boolean bRun = true;

	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	/**
	 * 构造函数,准备一个新的BlueToothchat会话.
	 * 
	 * @param context
	 *            用户界面活动的背景
	 * @param handler
	 *            一个处理程序发送邮件到用户界面活性
	 */
	public BluetoothChatService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = handler;
	}

	/**
	 * 设置当前状态的聊天连接
	 * 
	 * @param state
	 *            整数定义当前连接状态
	 */
	private synchronized void setState(int state) {
		mState = state;
		// 给新状态的处理程序, 界面活性可以更新
		mHandler.obtainMessage(BlueToothMsg.MESSAGE_STATE_CHANGE, state, -1)
				.sendToTarget();
	}

	/**
	 * 返回当前的连接状态。
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * 开始聊天服务,特别AcceptThread开始 开始 会话听力(服务器)模式.
	 */
	public synchronized void start() {
		// 取消任何线程试图建立连接
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// 取消任何线程正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_LISTEN);
	}

	// 连接按键响应函数
	/**
	 * 开始ConnectThread启动连接到远程设备。
	 * 
	 * @param 装置连接的蓝牙设备
	 */
	public synchronized void connect(BluetoothDevice device) {
		// 取消任何线程试图建立连接
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// 取消任何线程正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动线程连接的设备
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * 开始connectedThread开始管理一个蓝牙连接
	 * 
	 * @param bluetoothsocket插座上连接了
	 * @param 设备已连接的蓝牙设备
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		// 取消线程完成连接
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// 取消任何线程正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动线程管理连接和传输
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		Message msg = mHandler.obtainMessage(BlueToothMsg.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(BlueToothMsg.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		setState(STATE_CONNECTED);
	}

	/**
	 * 停止所有的线程
	 */
	public synchronized void stop() {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// 创建临时对象
		ConnectedThread r;
		// 同步副本的ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		// 执行写同步
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);
		//BluetoothChatService.this.start();
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		setState(STATE_LISTEN);
		//BluetoothChatService.this.start();
	}

	/**
	 * 本线在试图使传出联系 与设备,它径直穿过连接;或者 成功或失败.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		@SuppressLint("NewApi")
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			// 得到一个BluetoothSocket为与连接
			// 由于蓝牙设备			
			try {
				// 这里做了SDK版本的判断, 一下纯属个人理解, 当我加上这句判断的时候,配对框不会
				// 弹出来, 直接后台连接了; 反之...
//				int sdk = Integer.parseInt(Build.VERSION.SDK);
//				if(sdk >= 10) {
//					tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//				} else {
					tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//				}								
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// 总是取消的发现，因为它会减缓连接
			mAdapter.cancelDiscovery();
			// 使一个连接到BluetoothSocket
			try {
				// 这是一个阻塞调用和将只返回一个
				// 成功的连接或例外
				mmSocket.connect();
			} catch (IOException e) {
				// 关闭这个socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG,
							"unable to close() socket during connection failure",
							e2);
				}
				// 启动服务在重新启动聆听模式
				connectionFailed();
				return;
			}
			// 因为我们所做的ConnectThread复位
			synchronized (BluetoothChatService.this) {
				mConnectThread = null;
			}
			// 启动连接线程
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * 本线在连接与远程设备, 它处理所有传入和传出的传输.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			// 获得BluetoothSocket输入输出流
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "没有创建临时sockets", e);
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[2048];
			int bytes = 0;
			String readMessage = "";
			String resultMessage = "";
			while (true) {
				try {
					if ((bytes = mmInStream.read(buffer)) != -1) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						readMessage = CommonUtils.Bytes2HexString(buf_data);
						resultMessage += readMessage;
						if(!TextUtils.isEmpty(CommonUtils.checkBluetoothReuslt(resultMessage))) {
							System.err.println("BluetoothChatService返回数据: -->" 
									+ CommonUtils.checkBluetoothReuslt(resultMessage)); 
							mHandler.obtainMessage(BlueToothMsg.MESSAGE_READ, -1,
									-1, resultMessage).sendToTarget(); 
							resultMessage = "";
						}						
					}
										
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}
		
//		public void run() {
//            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            // Keep listening to the InputStream while connected
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//
//                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(BlueToothMsg.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    Log.e(TAG, "disconnected", e);
//                    connectionLost();
//                    break;
//                }
//            }
//        }

		/**
		 * 写输出的连接。 @param buffer 这是一个字节流
		 */
		public void write(byte[] buffer) {
			try { 
				mmOutStream.write(buffer);
				// 分享发送的信息到Activity
				mHandler.obtainMessage(BlueToothMsg.MESSAGE_WRITE, -1, -1,
						buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "关闭连接失败!", e);
			}
		}
	}
}
