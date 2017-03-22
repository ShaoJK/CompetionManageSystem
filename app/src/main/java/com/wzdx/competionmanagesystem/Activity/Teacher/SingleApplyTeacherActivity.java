package com.wzdx.competionmanagesystem.Activity.Teacher;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BitmapTools;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sjk on 2016/11/17.
 */

public class SingleApplyTeacherActivity extends Activity implements View.OnClickListener {
    private EditText edApplyName, edIDCard;
    private TextView tvRed1, tvRed2, tvTeacher, tvRed3, tvCompName, tvPZ;
    private ImageView imgPZ;
    private Button btApply;
    private CompetitionDetail mCompetitionDetail;
    private String mFilePath;
    private Uri mUri;
    private static final int CODE_GALLERY_REQUEST = 0x01;
    private static final int CODE_CAMERA_REQUEST = 0x02;
    private static final int CODE_RESULT_REQUEST = 0x03;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_single_teacher);
        Bundle bundle = getIntent().getExtras();
        mCompetitionDetail = (CompetitionDetail) bundle.getSerializable("data");
        initView();
    }

    private void initView() {
        //标题栏
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("竞赛报名");
        findViewById(R.id.ib_back).setOnClickListener(this);

        tvCompName = (TextView) findViewById(R.id.tvCompName);
        edApplyName = (EditText) findViewById(R.id.edApplyName);
        tvRed1 = (TextView) findViewById(R.id.tvRed1);
        edIDCard = (EditText) findViewById(R.id.edIDCard);
        tvRed2 = (TextView) findViewById(R.id.tvRed2);
        tvTeacher = (TextView) findViewById(R.id.tvTeacher);
        tvTeacher.setTag(-1);
        tvRed3 = (TextView) findViewById(R.id.tvRed3);
        tvPZ = (TextView) findViewById(R.id.tvPZ);
        btApply = (Button) findViewById(R.id.btApply);
        imgPZ = (ImageView) findViewById(R.id.imgPZ);

        tvPZ.setOnClickListener(this);
        btApply.setOnClickListener(this);
        //竞赛名称
        tvCompName.setText(mCompetitionDetail.getComp_name());
        //是否需要身份证
        if (mCompetitionDetail.getIsIDcard() == 1) {
            tvRed1.setVisibility(View.VISIBLE);
        } else {
            tvRed1.setVisibility(View.INVISIBLE);
        }
        //是否需要知道老师
        if (mCompetitionDetail.getIsadviser() == 1) {
            tvRed2.setVisibility(View.VISIBLE);
        } else {
            tvRed2.setVisibility(View.INVISIBLE);
        }
        //是否需要凭证
        if (mCompetitionDetail.getIsproof() == 1) {
            tvRed3.setVisibility(View.VISIBLE);
        } else {
            tvRed3.setVisibility(View.INVISIBLE);
        }

        //指导老师设置
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            Teacher tea = (Teacher) UICommon.INSTANCE.getUser();
            tvTeacher.setText(tea.getT_name());
            tvTeacher.setTag(tea.getID());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.tvPZ:
                showSelectDialog();
                break;
            case R.id.btApply:
                if (checkCanApply()) {
                    //构造参数列表
                    List<Part> partList = new ArrayList<Part>();
                    partList.add(new StringPart("stuNumber", edApplyName.getText().toString()));
                    partList.add(new StringPart("competionID", mCompetitionDetail.getCompID() + ""));
                    partList.add(new StringPart("appTime", new Date().getTime()/1000 + ""));

                    partList.add(new StringPart("teacher", tvTeacher.getTag() + ""));

                    if (mCompetitionDetail.getIsproof() == 1) {
                        partList.add(new StringPart("hasProof", "1"));
                        try {
                            partList.add(new FilePart("proof", new File(mFilePath)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        partList.add(new StringPart("hasProof", "0"));
                    }
                    String url = "CompetitionController/appolySingleCompByTeacher";
                    MyHttpRequest request = new MyHttpRequest(this, url, null) {
                        @Override
                        public void sucessListener(String result) {
                            if (!result.isEmpty()) {
                                try {
                                    General general = new Gson().fromJson(result, General.class);
                                    if (general.getSuccess()) {
                                        AlertDialog dialog = new AlertDialog.Builder(SingleApplyTeacherActivity.this).setMessage(general.getMsg())
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SingleApplyTeacherActivity.this.finish();
                                                        dialog.dismiss();
                                                    }
                                                }).create();
                                        dialog.show();
                                    } else {
                                        if (general.getMsg().equals("未登录")) {
                                            UICommon.INSTANCE.toLogin(SingleApplyTeacherActivity.this);
                                        } else {
                                            TispToastFactory.showTip(general.getMsg());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    TispToastFactory.showTip("系统错误");
                                }
                            }


                        }
                    };
                    request.startRequestByPostWithFile(partList);
                }

                break;
        }
    }

    /**
     * 对学生是否具有报名资格进行初步的校验
     */
    private boolean checkCanApply() {

        //验证是否输入学生学号
        if (edApplyName.getText().toString().isEmpty()) {
            TispToastFactory.getToast(this, "请输入学生学号", Toast.LENGTH_LONG).show();
            return false;
        }

        //验证身份证
        if (mCompetitionDetail.getIsIDcard() == 1) {
            if (edIDCard.getText().toString().trim().isEmpty()) {
                TispToastFactory.getToast(this, "请输入身份证信息", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //验证是否有指导老师
        if (mCompetitionDetail.getIsadviser() == 1) {
            if (tvTeacher.getText().toString().trim().isEmpty()) {
                TispToastFactory.getToast(this, "请选择一位指导老师", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //凭证验证
        if (mCompetitionDetail.getIsproof() == 1) {
            if (mFilePath == null || mFilePath.isEmpty()) {
                TispToastFactory.getToast(this, "请选择一张凭证图片", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void showSelectDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"图库", "照相机"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //选择图库或选择照相机
                if (which == 0) {
                    createFilePath();
                    //  从图库中选择头像
                    choseHeadImageFromGallery();
                } else {
                    //  产生文件名
                    createFilePath();
                    //  调用系统照相机
                    choseHeadImageFromCameraCapture();
                }
            }
        });
        builder.show();
    }

    private void setBitampShow(String fileName) {
        Bitmap bitmap = null;
        //裁剪图片
        bitmap = BitmapTools.decodeSampledBitmapFromFileName(fileName, 70, 70);
        imgPZ.setImageBitmap(bitmap);
    }

    // 产生一个唯一的文件路径
    private void createFilePath() {
        //检查是否存在文件夹，若没有，则创建
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/compms";
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //根据当前时间构建文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINESE);
        String fileName = sdf.format(new Date()) + ".png";
        mFilePath = dirPath + "/" + fileName;
        mUri = Uri.parse("file://" + "/" + mFilePath);
    }

    //从图库中取图
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        //调用系统相机
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = Uri.fromFile(new File(mFilePath));
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        //打开照相机
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    //得到图片后,选择图片显示位置
    private void cropRawPhoto(Uri uri, String fileName) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        //uritempFile为Uri类变量，实例化uritempFile
        Uri uritempFile = Uri.parse("file://" + "/" + fileName);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            mFilePath = "";
            mUri = null;
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST: //图库中选择图片
                //图片显示部位选择
                cropRawPhoto(data.getData(), mFilePath);
                break;
            case CODE_CAMERA_REQUEST: //调用系统照相机
                //图片显示部位选择
                cropRawPhoto(mUri, mFilePath);
                break;
            case CODE_RESULT_REQUEST://图片准备完成后,显示并上传保存
                setBitampShow(mFilePath);
                break;
        }
    }
}


