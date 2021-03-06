package exocr.idcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import exocr.exocrengine.EXIDCardResult;

public final class IDPhotoResultActivity extends Activity  {
	public static final String ID_RECO_RESULT = "exocr.idcard.recoResult";
	public static final String ID_FINAL_RESULT = "exocr.idcard.finalResult";
	public static final String ID_EDITED = "exocr.idcard.edited";
	public static final int ID_RETURN_RESULT = 200;

	private static final String PADDING_DIP = "4dip";
	private static final String LABEL_LEFT_PADDING_DEFAULT = "2dip";
	private static final String LABEL_LEFT_PADDING_HOLO = "12dip";
	private static final String FIELD_HALF_GUTTER = PADDING_DIP;
	private int viewIdCounter = 1;
	private static final int editTextIdBase = 100;
	private int editTextIdCounter = editTextIdBase;

	private TextView activityTitleTextView;
	private EditText numberEdit;
	private ImageView cardView;
	private Button doneBtn;
	private Button cancelBtn;
	private EXIDCardResult capture;
	private EXIDCardResult recoResult;
	private EXIDCardResult finalResult;

	private EditText nameValue;
	private EditText sexValue;
	private EditText nationValue;
	private EditText birthdayValue;
	private EditText addressValue;
	private EditText codeValue;
	private EditText officeValue;
	private EditText validDateValue;
	private boolean bRecoFailed = false;
	
	private LinearLayout fullImageLayout;
	private LinearLayout nameLayout;
	private LinearLayout sexLayout;
	private LinearLayout nationLayout;
	private LinearLayout birthLayout;
	private LinearLayout addressLayout;
	private LinearLayout cardnumLayout;
	private LinearLayout officeLayout;
	private LinearLayout validLayout;
	private LinearLayout frontFullImageLayout;
	private LinearLayout backFullImageLayout;

	private boolean autoAcceptDone;
	private String labelLeftPadding;

	private int resultBeginId;// 显示结果的EditText控件的起始ID，按Done时获取结果需要用到
	private int resultEndId; // 显示结果的EditText控件的终止ID，按Done时获取结果需要用到

	private final String TAG = this.getClass().getName();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        // hide titlebar of application
        // must be before setting the layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initResult();
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            throw new IllegalStateException("Didn't find any extras!");
        capture = extras.getParcelable(CaptureActivity.EXTRA_SCAN_RESULT);
        int type = 0;
        
        if(capture != null){
        	type = capture.type;
        	if(type == 1){
        		int idcardfrontrsteditId = ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "layout", "idcardfrontrstedit");
        		setContentView(idcardfrontrsteditId);
//        		setContentView(R.layout.idcardfrontrstedit);
        		
        		nameValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNameEditTextF"));
        		sexValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardSexEditTextF"));
        		nationValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNationEditTextF"));
        		birthdayValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardBirthdayEditTextF"));
        		addressValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardAddressEditTextF"));
        		codeValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardCodeEditTextF")); 
        		
        		nameLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNameBGF"));
        		sexLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardSexBGF"));
        		nationLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNationBGF"));
        		birthLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardBirthBGF"));
        		addressLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardAddressBGF"));
        		cardnumLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardCodeBGF"));
        		frontFullImageLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "frontFullImageBGF"));
        		
//        		nameValue = (EditText) findViewById(R.id.IDCardNameEditTextF);
//        		sexValue = (EditText) findViewById(R.id.IDCardSexEditTextF);
//        		nationValue = (EditText) findViewById(R.id.IDCardNationEditTextF);
//        		birthdayValue = (EditText) findViewById(R.id.IDCardBirthdayEditTextF);
//        		addressValue = (EditText) findViewById(R.id.IDCardAddressEditTextF);
//        		codeValue = (EditText) findViewById(R.id.IDCardCodeEditTextF); 
//        		
//        		nameLayout = (LinearLayout)findViewById(R.id.IDCardNameBGF);
//        		sexLayout = (LinearLayout)findViewById(R.id.IDCardSexBGF);
//        		nationLayout = (LinearLayout)findViewById(R.id.IDCardNationBGF);
//        		birthLayout = (LinearLayout)findViewById(R.id.IDCardBirthBGF);
//        		addressLayout = (LinearLayout)findViewById(R.id.IDCardAddressBGF);
//        		cardnumLayout = (LinearLayout)findViewById(R.id.IDCardCodeBGF);
//        		frontFullImageLayout = (LinearLayout)findViewById(R.id.frontFullImageBGF);
        		
        		if (!EXIDCardResult.SHOW_NAME_ID) {
        			nameLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_SEX_ID) {
        			sexLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_NATION_ID) {
        			nationLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_BIRTH_ID) {
        			birthLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_ADDRESS_ID) {
        			addressLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_CARDNUM_ID) {
        			cardnumLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_FRONTFULLIMG_ID) {
        			frontFullImageLayout.setVisibility(View.GONE);
        		}       		
        	} else if(type == 2){
        		int idcardbackrsteditId = ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "layout", "idcardbackrstedit");
        		setContentView(idcardbackrsteditId);
//        		setContentView(R.layout.idcardbackrstedit);
        		
        		officeValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardOfficeEditTextB"));
        		validDateValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardValidDateEditTextB"));
        		
        		officeLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardOfficeBGB"));
        		validLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardValidDateBGB"));
        		backFullImageLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "backFullImageBGB"));
        		
//        		officeValue = (EditText) findViewById(R.id.IDCardOfficeEditTextB);
//        		validDateValue = (EditText) findViewById(R.id.IDCardValidDateEditTextB);
//        		
//        		officeLayout = (LinearLayout)findViewById(R.id.IDCardOfficeBGB);
//        		validLayout = (LinearLayout)findViewById(R.id.IDCardValidDateBGB);
//        		backFullImageLayout = (LinearLayout)findViewById(R.id.backFullImageBGB);
        		
        		if (!EXIDCardResult.SHOW_OFFICE_ID) {
        			officeLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_VALID_ID) {
        			validLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_BACKFULLIMG_ID) {
        			backFullImageLayout.setVisibility(View.GONE);
        		}
        	} else {
        		bRecoFailed = true;
        		int idcarderrrsteditId = ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "layout", "idcarderrrstedit");
        		setContentView(idcarderrrsteditId);
//        		setContentView(R.layout.idcarderrrstedit);
        		
        		nameValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNameEditTextE"));
        		sexValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardSexEditTextE"));
        		nationValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNationEditTextE"));
        		birthdayValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardBirthdayEditTextE"));
        		addressValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardAddressEditTextE"));
        		codeValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardCodeEditTextE"));
        		officeValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardOfficeEditTextE"));
        		validDateValue = (EditText) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardValidDateEditTextE"));
        		
        		nameLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNameBGE"));
        		sexLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardSexBGE"));
        		nationLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardNationBGE"));
        		birthLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardBirthBGE"));
        		addressLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardAddressBGE"));
        		cardnumLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardCodeBGE"));
        		officeLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardOfficeBGE"));
        		validLayout = (LinearLayout)findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDCardValidDateBGE"));
        		
//        		nameValue = (EditText) findViewById(R.id.IDCardNameEditTextE);
//        		sexValue = (EditText) findViewById(R.id.IDCardSexEditTextE);
//        		nationValue = (EditText) findViewById(R.id.IDCardNationEditTextE);
//        		birthdayValue = (EditText) findViewById(R.id.IDCardBirthdayEditTextE);
//        		addressValue = (EditText) findViewById(R.id.IDCardAddressEditTextE);
//        		codeValue = (EditText) findViewById(R.id.IDCardCodeEditTextE);
//        		officeValue = (EditText) findViewById(R.id.IDCardOfficeEditTextE);
//        		validDateValue = (EditText) findViewById(R.id.IDCardValidDateEditTextE);
//        		
//        		nameLayout = (LinearLayout)findViewById(R.id.IDCardNameBGE);
//        		sexLayout = (LinearLayout)findViewById(R.id.IDCardSexBGE);
//        		nationLayout = (LinearLayout)findViewById(R.id.IDCardNationBGE);
//        		birthLayout = (LinearLayout)findViewById(R.id.IDCardBirthBGE);
//        		addressLayout = (LinearLayout)findViewById(R.id.IDCardAddressBGE);
//        		cardnumLayout = (LinearLayout)findViewById(R.id.IDCardCodeBGE);
//        		officeLayout = (LinearLayout)findViewById(R.id.IDCardOfficeBGE);
//        		validLayout = (LinearLayout)findViewById(R.id.IDCardValidDateBGE);
        		
        		if (!EXIDCardResult.SHOW_NAME_ID) {
        			nameLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_SEX_ID) {
        			sexLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_NATION_ID) {
        			nationLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_BIRTH_ID) {
        			birthLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_ADDRESS_ID) {
        			addressLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_CARDNUM_ID) {
        			cardnumLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_OFFICE_ID) {
        			officeLayout.setVisibility(View.GONE);
        		}
        		if (!EXIDCardResult.SHOW_VALID_ID) {
        			validLayout.setVisibility(View.GONE);
        		}
        	}
        }else{
        	return;
        }
        
        if(type == 1){
        	nameValue.setText(capture.name);
			recoResult.name = capture.name;

			sexValue.setText(capture.sex);
			recoResult.sex = capture.sex;

			nationValue.setText(capture.nation);
			recoResult.nation = capture.nation;

			birthdayValue.setText(capture.birth);
			recoResult.birth = capture.birth;

			addressValue.setText(capture.address);
			recoResult.address = capture.address;

			codeValue.setText(capture.cardnum);
			recoResult.cardnum = capture.cardnum;
        	
//        	ImageView frontFullImg = (ImageView) findViewById(R.id.frontFullImageViewF);
        	ImageView frontFullImg = (ImageView) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "frontFullImageViewF"));
			frontFullImg.setImageBitmap(IDPhoto.markedCardImage);  	
        }else if(type == 2){
        	officeValue.setText(capture.office);
			recoResult.office = capture.office;

			validDateValue.setText(capture.validdate);
			recoResult.validdate = capture.validdate;
        	
			ImageView backFullImg = (ImageView) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "backFullImageViewB"));
//        	ImageView backFullImg = (ImageView) findViewById(R.id.backFullImageViewB);
			backFullImg.setImageBitmap(IDPhoto.markedCardImage);
        }else {
        	ImageView fullImg = (ImageView) findViewById(ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "FullImageViewE"));
//        	ImageView fullImg = (ImageView) findViewById(R.id.FullImageViewE);
        	int w = this.getBaseContext().getResources().getDisplayMetrics().widthPixels;
        	int h = (int) ((int)w * 0.63084);
        	fullImg.setAdjustViewBounds(true);
        	fullImg.setMaxWidth(w);
        	fullImg.setMaxHeight(h);
        	fullImg.setImageBitmap(IDPhoto.markedCardImage);
        	
        }
               
    }

	private void initResult() {
		recoResult = new EXIDCardResult();
		finalResult = new EXIDCardResult();

		recoResult.name = "";
		recoResult.sex = "";
		recoResult.nation = "";
		recoResult.birth = "";
		recoResult.address = "";
		recoResult.cardnum = "";
		recoResult.office = "";
		recoResult.validdate = "";      
	}
	
	private void getFinalResult() {
		if (nameValue != null) {
			finalResult.name = nameValue.getText().toString();
		} else {
			finalResult.name = "";
		}
		
		if (sexValue != null) {
			finalResult.sex = sexValue.getText().toString();
		} else {
			finalResult.sex = "";
		}
		
		if (nationValue != null) {
			finalResult.nation = nationValue.getText().toString();
		} else {
			finalResult.nation = "";
		}
		
		if (birthdayValue != null) {
			finalResult.birth = birthdayValue.getText().toString();
		} else {
			finalResult.birth = "";
		}
		
		if (addressValue != null) {
			finalResult.address = addressValue.getText().toString();
		} else {
			finalResult.address = "";
		}
		
		if (codeValue != null) {
			finalResult.cardnum = codeValue.getText().toString();
		} else {
			finalResult.cardnum = "";
		}
		
		if (officeValue != null) {
			finalResult.office = officeValue.getText().toString();
		} else {
			finalResult.office = "";
		}
		
		if (validDateValue != null) {
			finalResult.validdate = validDateValue.getText().toString();
		} else {
			finalResult.validdate = "";
		}
	}
		
	private boolean isEdited() {
		if (finalResult.name.equals(recoResult.name)  && finalResult.sex.equals(recoResult.sex) && finalResult.nation.equals(recoResult.nation) && finalResult.birth.equals(recoResult.birth) && finalResult.address.equals(recoResult.address) && finalResult.cardnum.equals(recoResult.cardnum) && finalResult.office.equals(recoResult.office) && finalResult.validdate.equals(recoResult.validdate)) {
			return false;
		}	
		return true;
	}
	public void onIDReturn(View v) {
		getFinalResult();

		Intent intent = new Intent();
		intent.putExtra(ID_RECO_RESULT, recoResult);
		intent.putExtra(ID_FINAL_RESULT, finalResult);
		this.setResult(ID_RETURN_RESULT, intent);

		if (isEdited()) {
			intent.putExtra(ID_EDITED, true);
		} else {
			intent.putExtra(ID_EDITED, false);
		}

		recoResult = null;
		finalResult = null;

		this.finish();
	}
}
