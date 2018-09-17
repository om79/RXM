package viroopa.com.medikart.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import viroopa.com.medikart.R;
import viroopa.com.medikart.util.LruBitmapCache;

public class AppController extends MultiDexApplication {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private String IMEI;
	private String MemberImage;
	private String StreamImage;
	private static final String ALARM_SOUND = "alarm_sound";
	private SharedPreferences SharedPreference;

	public String getSearchWord() {
		return SearchWord;
	}

	public void setSearchWord(String searchWord) {
		SearchWord = searchWord;
	}

	private String SearchWord;

	public String getDrugInteraction() {
		return DrugInteraction;
	}

	public void setDrugInteraction(String drugInteraction) {
		DrugInteraction = drugInteraction;
	}

	public String getMoleculeinteraction() {
		return Moleculeinteraction;
	}

	public void setMoleculeinteraction(String moleculeinteraction) {
		Moleculeinteraction = moleculeinteraction;
	}

	private String DrugInteraction;
	private String Moleculeinteraction;

	public String getMemberName() {
		return MemberName;
	}

	public void setMemberName(String memberName) {
		MemberName = memberName;
	}

	private String MemberName;

	public String getIMEInumber() {
		return IMEInumber;
	}

	public void setIMEInumber(String IMEInumber) {
		this.IMEInumber = IMEInumber;
	}

	private String IMEInumber;
	private String RealationshipId;

	private Boolean isNetwrkpageVisible=false;

	public void setIsNetwrkpageVisible(Boolean isNetwrkpageVisible) {
		this.isNetwrkpageVisible = isNetwrkpageVisible;
	}
	public SharedPreferences getSharedPreference() {
		if (SharedPreference == null) {
			SharedPreference = getApplicationContext().getSharedPreferences("Global", Context.MODE_PRIVATE);;
		}
		return SharedPreference;
	}

	public Boolean getIsNetwrkpageVisible() {
		return isNetwrkpageVisible;
	}


	public String getEmergencyBaseprice() {
		return EmergencyBaseprice;
	}

	public void setEmergencyBaseprice(String emergencyBaseprice) {
		EmergencyBaseprice = emergencyBaseprice;
	}

	public String getEmergencyPromocodeAmount() {
		return EmergencyPromocodeAmount;
	}

	public void setEmergencyPromocodeAmount(String emergencyPromocodeAmount) {
		EmergencyPromocodeAmount = emergencyPromocodeAmount;
	}

	private String EmergencyBaseprice,EmergencyPromocodeAmount;
	private String imagePathForZoom;
	private String parentClass;

	public String getUUIDforWM() {
		return UUIDforWM;
	}

	public void setUUIDforWM(String UUIDforWM) {
		this.UUIDforWM = UUIDforWM;
	}

	private  String UUIDforWM;
	public String getProduct_moleculedetails() {
		return product_moleculedetails;
	}

	public void setProduct_moleculedetails(String product_moleculedetails) {
		this.product_moleculedetails = product_moleculedetails;
	}

	public String getProduct_medicinedetails() {
		return product_medicinedetails;
	}

	public void setProduct_medicinedetails(String product_medicinedetails) {
		this.product_medicinedetails = product_medicinedetails;
	}

	public String getProduct_packsizedetails() {
		return product_packsizedetails;
	}

	public void setProduct_packsizedetails(String product_packsizedetails) {
		this.product_packsizedetails = product_packsizedetails;
	}

	public String getProduct_otherproductdetails() {
		return product_otherproductdetails;
	}

	public void setProduct_otherproductdetails(String product_otherproductdetails) {
		this.product_otherproductdetails = product_otherproductdetails;
	}

	public String getProduct_subdetails() {
		return product_subdetails;
	}

	public void setProduct_subdetails(String product_subdetails) {
		this.product_subdetails = product_subdetails;
	}

	public Uri getCurrentAlarmSound() {
		Uri sound = null;
		// Look up sound in preferences.
		SharedPreferences pref =this.getSharedPreferences("Global", MODE_PRIVATE);
		String uriString = pref.getString(ALARM_SOUND, null);
		if (uriString != null) {
			sound = Uri.parse(uriString);
		}
		// Look up default alarm sound.
		if (sound == null) {
			sound = RingtoneManager.getActualDefaultRingtoneUri(
					getApplicationContext(),
					RingtoneManager.TYPE_ALARM);
		}

		return sound;
	}

	public  void setCurrentAlarmSound(Uri uri) {
		SharedPreferences pref = this.getSharedPreferences("Global", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(ALARM_SOUND, uri.toString());
		editor.commit();
	}

	private String product_moleculedetails,product_medicinedetails,product_packsizedetails,product_otherproductdetails,product_subdetails;

	private String Bp_Default_Site;
	private String Bp_Default_Pos;
	private String Bp_use_last_weight;
	private String weight;
	//private String BPMemberName;
//
//	private String DMMemberName;
//	private String DMMemberImage;

	public String getBp_last_bs() {
		return Bp_last_bs;
	}

	public void setBp_last_bs(String bp_last_bs) {
		Bp_last_bs = bp_last_bs;
	}

	private String Bp_last_bs;


	public String getImagePathForZoom() {
		return imagePathForZoom;
	}

	public void setImagePathForZoom(String imagePathForZoom) {
		this.imagePathForZoom = imagePathForZoom;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}


	public String getWeightType() {
		return WeightType;
	}

	public void setWeightType(String weightType) {
		WeightType = weightType;



	}

	private String WeightType;
	public String getBp_Default_Site() {
		return Bp_Default_Site;


	}

	public void setBp_Default_Site(String bp_Default_Site) {
		Bp_Default_Site = bp_Default_Site;


	}

	public String getWeight() {
		return weight;

	}

	public void setWeight(String weight) {
		this.weight = weight;


	}

	public String getBp_use_last_weight() {
		return Bp_use_last_weight;


	}

	public void setBp_use_last_weight(String bp_use_last_weight) {
		Bp_use_last_weight = bp_use_last_weight;


	}

	public String getBp_Default_Pos() {
		return Bp_Default_Pos;


	}

	public void setBp_Default_Pos(String bp_Default_Pos) {
		Bp_Default_Pos = bp_Default_Pos;


	}


	public String getRealationshipId() {
		return RealationshipId;

	}

	public void setRealationshipId(String realationshipId) {
		RealationshipId = realationshipId;
	}


	//private String BPMemberImage;
	private  Integer ProfilePicFlag;
	private String sCardId,scartjson,ssummaryjson,sPromoCode,sMemberId;

	public String getsCardId() {
		return sCardId;
	}

	public void setsCardId(String sCardId) {
		this.sCardId = sCardId;
	}

	public String getScartjson() {
		return scartjson;
	}

	public void setScartjson(String scartjson) {
		this.scartjson = scartjson;
	}

	public String getSsummaryjson() {
		return ssummaryjson;
	}

	public void setSsummaryjson(String ssummaryjson) {
		this.ssummaryjson = ssummaryjson;
	}

	public String getsPromoCode() {
		return sPromoCode;
	}

	public void setsPromoCode(String sPromoCode) {
		this.sPromoCode = sPromoCode;
	}

	public String getsMemberId() {
		return sMemberId;
	}

	public void setsMemberId(String sMemberId) {
		this.sMemberId = sMemberId;
	}





	public String getStreamImage() {
		return StreamImage;
	}

	public void setStreamImage(String streamImage) {
		StreamImage = streamImage;
	}



	public Integer getProfilePicFlag() {
		return ProfilePicFlag;
	}

	public void setProfilePicFlag(Integer profilePicFlag) {
		ProfilePicFlag = profilePicFlag;
	}



	public String getMemberImage() {
		return MemberImage;
	}

	public void setMemberImage(String memberImage) {
		MemberImage = memberImage;
	}



	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String IMEI) {
		this.IMEI = IMEI;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	private String MemberId;


	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		//CustomActivityOnCrash.install( this);

		mInstance = this;

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/Roboto-.ttf")
						.setFontAttrId( R.attr.fontPath)
						.build()
		);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache ());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}