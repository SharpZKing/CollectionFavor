package com.sharp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.collectionfavor.LoginActivity;
import com.sharp.collectionfavor.R;
import com.sharp.collectionfavor.SettingActivity;
import com.sharp.entity.User;
import com.sharp.util.SharedUtil;
import com.sharp.util.ToolUtil;
import com.sharp.views.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zjfsharp on 2017/6/3.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{

    private LinearLayout mBeforeLogin;
    private LinearLayout mAfterLogin;
    private LinearLayout mAccountDetail;
    private Button mGoLoginBtn;

    private TextView mUsernameTv;
    private TextView mDescTv;
    private TextView mEmailTv;
    private TextView mSetting;

    private CircleImageView mProfile;

    private CustomDialog dialog;
    private Button mCancel;
    private Button mCamera;
    private Button mPicture;

//    private User currentUser = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mBeforeLogin = (LinearLayout) view.findViewById(R.id.account_before_login);
        mAfterLogin = (LinearLayout) view.findViewById(R.id.account_after_login);
        mAccountDetail = (LinearLayout) view.findViewById(R.id.account_details);
        mGoLoginBtn = (Button) view.findViewById(R.id.account_btn_gologin);
        mGoLoginBtn.setOnClickListener(this);

        mProfile = (CircleImageView) view.findViewById(R.id.profile_image);
        mProfile.setOnClickListener(this);

        mUsernameTv = (TextView) view.findViewById(R.id.account_username);
        mDescTv = (TextView) view.findViewById(R.id.account_desc);
        mEmailTv = (TextView) view.findViewById(R.id.account_email);
        mSetting = (TextView) view.findViewById(R.id.account_setting);
        mSetting.setOnClickListener(this);

        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        mCamera = (Button) dialog.findViewById(R.id.btn_camera);
        mCamera.setOnClickListener(this);
        mPicture = (Button) dialog.findViewById(R.id.btn_picture);
        mPicture.setOnClickListener(this);
        mCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(this);

        boolean isLogin = isLogin();
        if (!isLogin) {
            mBeforeLogin.setVisibility(View.VISIBLE);
            mAfterLogin.setVisibility(View.GONE);
            mAccountDetail.setVisibility(View.GONE);
        } else {
            mBeforeLogin.setVisibility(View.GONE);
            mAfterLogin.setVisibility(View.VISIBLE);
            mAccountDetail.setVisibility(View.VISIBLE);

            mUsernameTv.setText((String)BmobUser.getObjectByKey("username"));
            mDescTv.setText((String)BmobUser.getObjectByKey("desc"));
            mEmailTv.setText("邮箱 ( " + (String)BmobUser.getObjectByKey("email") + " )" );

//            Toast.makeText(getActivity(), +"="+ BmobUser.getObjectByKey("email") +"==="+ BmobUser.getObjectByKey("desc") , Toast.LENGTH_SHORT).show();

        }

        ToolUtil.getImageFromShareToImageView(getActivity(),mProfile);
    }

    public boolean isLogin(){

        BmobUser currentUser = BmobUser.getCurrentUser();
        if (currentUser == null){
            return false;
        }else{
            return true;
        }

        /*String result = SharedUtil.getString(getActivity(), "USER", "USER_NO_LOGIN");
        if ("USER_NO_LOGIN".equals(result)){
            return false;
        }else{
            return true;
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_btn_gologin:
//                Toast.makeText(getActivity(), "Click to login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.account_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent1);
//                Toast.makeText(getActivity(), "Click setting!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                dialog.dismiss();
                toCamera();
                break;
            case R.id.btn_picture:
                dialog.dismiss();
                toPicture();
                break;
        }
    }

    public  static final int REQUEST_IMAGE_CODE = 10001;
    public static final int REQUEST_CAMERA_CODE = 10002;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int RESULT_REQUEST_CODE = 102;
    public File tempFile = null;

    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.putExtra("type","image/*");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_CODE);

    }

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra("output", PHOTO_IMAGE_FILE_NAME);
       intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));

        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=getActivity().RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CAMERA_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case REQUEST_IMAGE_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }

    }

    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            mProfile.setImageBitmap(bitmap);
        }
        ToolUtil.putImageToShare(getActivity(),mProfile);
    }

    /**
     * 裁剪图片
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.e("E","uri == null");
            return;
        }


        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isLogin()) {
            mBeforeLogin.setVisibility(View.VISIBLE);
            mAfterLogin.setVisibility(View.GONE);
            mAccountDetail.setVisibility(View.GONE);
        } else {
            mBeforeLogin.setVisibility(View.GONE);
            mAfterLogin.setVisibility(View.VISIBLE);
            mAccountDetail.setVisibility(View.VISIBLE);

            mUsernameTv.setText((String)BmobUser.getObjectByKey("username"));
            mDescTv.setText((String)BmobUser.getObjectByKey("desc"));
            mEmailTv.setText("邮箱 ( " + (String)BmobUser.getObjectByKey("email") + " )" );

            ToolUtil.getImageFromShareToImageView(getActivity(),mProfile);

//            Toast.makeText(getActivity(), +"="+ BmobUser.getObjectByKey("email") +"==="+ BmobUser.getObjectByKey("desc") , Toast.LENGTH_SHORT).show();

        }
    }
}
