package com.supermap.imb.appconfig;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.supermap.android.file.MyAssetManager;
import com.supermap.android.file.MySharedPreferences;
import com.supermap.data.Environment;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;

public class MyApplication extends Application {
	public static String DATAPATH = "";
	public static String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	private static MyApplication sInstance = null;
	private Workspace mWorkspace = null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		DATAPATH = this.getFilesDir().getAbsolutePath()+"/";
		sInstance = this;
		
		//��һ���������û�����������ʼ����iMobile
		Environment.setLicensePath(DefaultDataConfig.LicensePath);
		Environment.initialization(this);
		
		//��ʼ��ϵͳ��ص���
		MySharedPreferences.init(this);
		MyAssetManager.init(this);
		
		//��������
//		new DefaultDataConfig().autoConfig();
		mWorkspace = new Workspace();
//		openWorkspace();
//		mWorkspace = new Workspace();
		
	}

	public void openWorkspace() {
		
		
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
		info.setServer(DefaultDataConfig.WorkspacePath);
		info.setType(WorkspaceType.SMWU);
		if(!mWorkspace.open(info)){
			ShowError("�����ռ���!");
		}
	}
	
	/**
	 * ��ȡ�򿪵Ĺ����ռ�
	 * @return
	 */
	public Workspace getOpenedWorkspace(){
		return mWorkspace;
	}
	
	/**
	 * ��ȡ��ǰӦ��Application
	 * @return
	 */
	public static MyApplication getInstance(){
		return sInstance;
	}
	
	/**
	 * Toast��ʾ��Ϣ
	 */
	public void ShowInfo(String info){
		Toast toast = Toast.makeText(sInstance, info, 500);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	/**
	 * Toast��ʾ������Ϣ
	 * @param err
	 */
	public void ShowError(String err){
		Toast toast = Toast.makeText(sInstance, "Error: "+err, 500);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
		Log.e(this.getClass().getName(), err);
	}
	
	/**
	 * ��ȡ��ʾ�ߴ�ֵ
	 * @param dp
	 * @return
	 */
	public static int dp2px(int dp){
		return (int) (dp*sInstance.getResources().getDisplayMetrics().density);
	}
}
