package com.supermap.imb.edit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RadioButton;

import com.supermap.data.GeometryType;
import com.supermap.data.Point;
import com.supermap.imb.appconfig.DefaultDataConfig;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.mapping.Action;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.MeasureListener;
/**
 * <p>
 * Title:��ͼ�༭
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------��Ȩ����----------------------------
 * ���ļ�Ϊ SuperMap iMobile ��ʾDemo�Ĵ��� 
 * ��Ȩ���У�������ͼ����ɷ����޹�˾
 * ----------------------------------------------------------------
 * ----------------------------SuperMap iMobile ��ʾDemo˵��---------------------------
 * 
 * 1��Demo��飺
 *   չʾ��ӡ�ɾ�����󣬱༭�ڵ㣬����ȵ�ͼ���ݱ༭�Ĳ�����
 * 2��Demo���ݣ�����Ŀ¼��"SuperMap/Demos/Data/EditData/"
 *           ��ͼ���ݣ�"changchun.smwu", "changchun.udb", "changchun.udd", "edit.udb", "edit.udd"
 *           ���Ŀ¼��"/SuperMap/License/"
 * 3���ؼ�����/��Ա: 
 *    Layer.setEditable();                          ����
 *    MapControl.setAction();                       ����
 *    MapControl.submit();                          ����
 *    MapControl.addGeometrySelectedListener();     ����
 *    MapControl.addActionChangedListener();        ����
 *    MapControl.addMeasureListener();              ����
 *    MapControl.setStrokeColor();                  ����
 *    MapControl.setStrokeWidth();                  ����
 *    MapControl.undo();                            ����
 *    MapControl.redo();                            ����
 *
 * 4������չʾ
 *   (1)��ӵ㡢�ߡ��桢�����ߡ������棬Ϳѻ��
 *   (2)�༭����ӡ�ɾ���ڵ㣬ɾ������
 *   (3)���룬������㡣
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * Company: ������ͼ����ɷ����޹�˾
 * </p>
 * 
 */
public class MainActivity extends Activity implements OnClickListener, OnTouchListener{
	private MapControl mMapControl = null;
	private MeasurePopup mMeasurePopup = null;
	private DrawPopup mDrawPopup = null;
	private EditPopup mEditPopup = null;
	private SettingPopup mSettingPopup = null;
	private SubmitInfo   mSubmitInfo   = null;
	// ֻ������ܽ���
	private RadioButton mBtnReceiveFocus = null;
	private View  anchorView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUI();
        prepareData();
       
        mMapControl.setMagnifierEnabled(true);
        mMapControl.setOnTouchListener(this);
        mMeasurePopup = new MeasurePopup(mMapControl);
        mDrawPopup = new DrawPopup(mMapControl);
        mEditPopup = new EditPopup(mMapControl);
        mSettingPopup = new SettingPopup(mMapControl);
        mSubmitInfo   = new SubmitInfo(mMapControl);
    }
    
    /*
     * ׼����ͼ����
     */
	private void prepareData(){
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("���ݼ�����...");
		progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		progress.show();
		new Thread(){
			@Override
			public void run() {
				super.run();
				//��������
				new DefaultDataConfig().autoConfig();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.dismiss();
						MyApplication.getInstance().openWorkspace();
						mMapControl.getMap().setWorkspace(MyApplication.getInstance().getOpenedWorkspace());
					    mMapControl.getMap().open(MyApplication.getInstance().getOpenedWorkspace().getMaps().get(0));
					    mMapControl.getMap().setFullScreenDrawModel(true);
					    mMapControl.getMap().refresh();
					}
				});
			}
		}.start();
	}
    /**
     * ��ʼ������
     */
    private void initUI(){
    	MapView mapView =  (MapView) findViewById(R.id.mapView);
    	mMapControl = mapView.getMapControl();
    	
    	findViewById(R.id.btn_add).setOnClickListener(this);
    	findViewById(R.id.btn_edit).setOnClickListener(this);
    	findViewById(R.id.btn_undo).setOnClickListener(this);
    	findViewById(R.id.btn_redo).setOnClickListener(this);
    	findViewById(R.id.btn_cancel).setOnClickListener(this);
    	findViewById(R.id.btn_submit).setOnClickListener(this);
    	findViewById(R.id.btn_setting).setOnClickListener(this);
    	findViewById(R.id.btnZoomIn).setOnClickListener(this);
    	findViewById(R.id.btnZoomOut).setOnClickListener(this);
    	findViewById(R.id.btnViewEntire).setOnClickListener(this);  	
    	findViewById(R.id.btn_measure).setOnClickListener(this);
    	
    	mBtnReceiveFocus = (RadioButton)findViewById(R.id.btn_receivefocus1);  	
    	anchorView = findViewById(R.id.btn_add);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			mMeasurePopup.dismiss();
			mEditPopup.dismiss();
			mSettingPopup.dismiss();
			mSubmitInfo.dismiss();
			
			if(mDrawPopup.isShowing()){
				mBtnReceiveFocus.setChecked(true);
				mDrawPopup.dismiss();
			}else{
				mDrawPopup.show(anchorView);
			}
			break;
		case R.id.btn_edit:
			mMeasurePopup.dismiss();
			mDrawPopup.dismiss();
			mSettingPopup.dismiss();
			mSubmitInfo.dismiss();
			
			if(mEditPopup.isShowing()){
				mBtnReceiveFocus.setChecked(true);
				mEditPopup.dismiss();
			}else{
				mEditPopup.show(anchorView);
			}
			break;
		case R.id.btn_undo:
			mMapControl.undo();
			break;
		case R.id.btn_redo:
			mMapControl.redo();
			break;
		case R.id.btn_cancel:
			mDrawPopup.cancel();
			mEditPopup.cancel();
			mMapControl.cancel();
			mMeasurePopup.cancel();
			mSubmitInfo.dismiss();
			
			break;
		case R.id.btn_submit:
		
			boolean isEdittable = isEditting();
			if(isEdittable){
				mMapControl.submit();
				
				mMeasurePopup.cancel();
				mEditPopup.cancel();
				mDrawPopup.cancel();
				mMapControl.cancel();
			}else{
				mMeasurePopup.dismiss();
				mEditPopup.dismiss();
				mSettingPopup.dismiss();
				mDrawPopup.dismiss();
				reset();
				mSubmitInfo.show();
			}
			break;
		case R.id.btn_setting:
			mMeasurePopup.dismiss();
			mDrawPopup.dismiss();
			mEditPopup.dismiss();
			mSubmitInfo.dismiss();
			
			if(mSettingPopup.isShowing()){
				mBtnReceiveFocus.setChecked(true);
				mSettingPopup.dismiss();
			}else{
				mSettingPopup.show(anchorView);
			}
			break;
		case R.id.btnZoomIn:
			mMapControl.getMap().zoom(2);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnZoomOut:
			mMapControl.getMap().zoom(0.5);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnViewEntire:
			mMapControl.getMap().viewEntire();
			mMapControl.getMap().refresh();
			break;
		case R.id.btn_measure:
			mDrawPopup.dismiss();
			mEditPopup.dismiss();
			mSettingPopup.dismiss();
			mSubmitInfo.dismiss();
			
			if(mMeasurePopup.isShowing()){
				mBtnReceiveFocus.setChecked(true);
				mMeasurePopup.dismiss();
			}else{
				mMeasurePopup.show(anchorView);
			}
			break;
		default:
			break;
		}		
	}  
	
	MeasureListener measureListener = new MeasureListener() {
		
		@Override
		public void lengthMeasured(double arg0, Point arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void areaMeasured(double arg0, Point arg1) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mMapControl.onMultiTouch(event);
		 
		int action = event.getAction();
		
		// �����������Զ��ύ
		if(action == MotionEvent.ACTION_UP){
			if (mMapControl.getCurrentGeometry() != null && mMapControl.getCurrentGeometry().getType()==GeometryType.GEOPOINT) {
				
				mMapControl.submit();

				return true;
			}
		}
			
		return true;
		
	}
	
	/**
	 * �ж��Ƿ��ڱ༭
	 * @return
	 */
	private boolean isEditting() {
		
		Action action= mMapControl.getAction();
		if(action.equals(Action.PAN) || action.equals( Action.NULL)){
			return false;
		}else{
		    return true;
		}
	}
	
	/**
	 * ���ð�ť
	 */
	protected void reset(){
		((RadioButton)findViewById(R.id.btn_add)).setChecked(false);
		((RadioButton)findViewById(R.id.btn_edit)).setChecked(false);
		((RadioButton)findViewById(R.id.btn_setting)).setChecked(false);
		((RadioButton)findViewById(R.id.btn_measure)).setChecked(false);
	}
}