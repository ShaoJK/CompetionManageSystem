package com.wzdx.competionmanagesystem.Activity.Student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.Teacher.GroupApplyTeacherActivity;
import com.wzdx.competionmanagesystem.Adapter.AcademyAdapter;
import com.wzdx.competionmanagesystem.Adapter.DepartmentAdapter;
import com.wzdx.competionmanagesystem.Adapter.TeacherAdapter;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.Academy;
import com.wzdx.competionmanagesystem.JavaBean.AcademyList;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.Department;
import com.wzdx.competionmanagesystem.JavaBean.DepartmentList;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.GroupInfo;
import com.wzdx.competionmanagesystem.JavaBean.Major;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.JavaBean.TeacherList;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BitmapTools;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sjk on 2016/11/17.
 */

public class GroupApplyActivity extends Activity implements View.OnClickListener {
    private EditText edApplyName, edIDCard;
    private TextView tvRed1, tvRed2, tvTeacher, tvRed3, tvCompName, tvPZ, tv_group;
    private View view_bg_shadow;
    private ImageView imgPZ;
    private Button btApply;
    private boolean isHideShow;
    private CompetitionDetail mCompetitionDetail;
    private String mFilePath;
    private Uri mUri;
    private static final int CODE_GALLERY_REQUEST = 0x01;//图库
    private static final int CODE_CAMERA_REQUEST = 0x02;//照相机
    private static final int CODE_RESULT_REQUEST = 0x03;//裁剪图片
    private int mApplyType = 0;//报名的类型（自己创建队伍/选择已有队伍）
    public static final int CODE_CREATE_TEAM = 0x11;//自己创建的队伍
    public static final int CODE_SELECTED_TEAM = 0x12;//选择已有队伍
    private PopupWindow mAcademyWindow, mDepartmentWindow, mTeacherWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果登陆的是老师，则调到老师的报名界面
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            Intent intent = getIntent();
            intent.setClassName(this, GroupApplyTeacherActivity.class.getName());
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_apply_group);
        Bundle bundle = getIntent().getExtras();
        mCompetitionDetail = (CompetitionDetail) bundle.getSerializable("data");
        initView();
    }

    private void initView() {
        //标题栏
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("竞赛报名");
        findViewById(R.id.ib_back).setOnClickListener(this);

        view_bg_shadow = findViewById(R.id.view_bg_shadow);
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
        tv_group = (TextView) findViewById(R.id.tv_group);
        //选择知道老师
        findViewById(R.id.ll_teacher).setOnClickListener(this);
        //选择队伍
        findViewById(R.id.ll_group).setOnClickListener(this);
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

        //用户名和身份证设置
        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            Student stu = (Student) UICommon.INSTANCE.getUser();
            edApplyName.setText(stu.getS_name());
            if (!stu.getIDcard().isEmpty()) {
                edIDCard.setText(stu.getIDcard());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.ll_teacher:
                showChoosemAcademyWindow();
                break;
            case R.id.ll_group:
                showChooseGroup();
                break;
            case R.id.tvPZ:
                showSelectDialog();
                break;
            case R.id.btApply:
                if (checkCanApply()) {
                    apply();
                }
                break;
        }
    }

    /**
     * 报名
     */
    private void apply() {
        Student stu = (Student) UICommon.INSTANCE.getUser();
        //构造参数列表
        List<Part> partList = new ArrayList<Part>();
        partList.add(new StringPart("stuID", stu.getID() + ""));
        partList.add(new StringPart("competionID", mCompetitionDetail.getCompID() + ""));
        partList.add(new StringPart("appTime", new Date().getTime() / 1000 + ""));
        partList.add(new StringPart("teacher", tvTeacher.getTag() + ""));
        partList.add(new StringPart("teamType", mApplyType + ""));

        //如果是选择已有的队伍报名，则需上传选择队伍的编号
        if (mApplyType == CODE_SELECTED_TEAM) {
            partList.add(new StringPart("teamID", tv_group.getTag().toString()));
        } else {
            partList.add(new StringPart("teamID", "-1"));
        }

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
        String url = "CompetitionController/groupApply";
        MyHttpRequest request = new MyHttpRequest(this, url, null) {
            @Override
            public void sucessListener(String result) {
                if (!result.isEmpty()) {
                    General general = new Gson().fromJson(result, General.class);
                    if (general.getSuccess()) {
                        AlertDialog dialog = new AlertDialog.Builder(GroupApplyActivity.this).setMessage(general.getMsg())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GroupApplyActivity.this.finish();
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    } else {
                        if (general.getMsg().equals("未登录")) {
                            UICommon.INSTANCE.toLogin(GroupApplyActivity.this);
                        } else {
                            TispToastFactory.showTip(general.getMsg());
                        }
                    }
                }
            }
        };
        request.startRequestByPostWithFile(partList);
    }

    /**
     * 弹框选择创建小队还是选择已有的小队
     */
    private void showChooseGroup() {
        AlertDialog dialog = new AlertDialog.Builder(this).setItems(new String[]{"选择已有队伍", "创建新的队伍"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {//选择已有队伍
                    //前往队伍列表页
                    Intent intent = new Intent(GroupApplyActivity.this, GroupListActivity.class);
                    intent.putExtra("competitionID", mCompetitionDetail.getCompID());
                    intent.putExtra("maxGroupNum", mCompetitionDetail.getGroup_num());
                    startActivityForResult(intent, 0);
                } else {//创建新的队伍
                    mApplyType = CODE_CREATE_TEAM;
                    tv_group.setText("我的小队");
                }
            }
        }).create();
        dialog.show();
    }

    private boolean checkCanApply() {
        boolean canApply = false;
        boolean majorLimit = false;//专业是否符合
        boolean gradeLimit = false;//年级是否符合

        //验证队伍
        if (mApplyType != CODE_CREATE_TEAM && mApplyType != CODE_SELECTED_TEAM) {
            TispToastFactory.getToast(this, "该竞赛必须以队伍形式进行报名", Toast.LENGTH_LONG).show();
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


        Student stu = (Student) UICommon.INSTANCE.getUser();
        //检查学生的专业是否符合报名标准
        int stuMajor = stu.getmID();
        if (mCompetitionDetail.getLimitedMajor() == null || mCompetitionDetail.getLimitedMajor().size() == 0) {
            majorLimit = true;
        } else {
            for (Major major : mCompetitionDetail.getLimitedMajor()) {
                if (stuMajor == major.getmID()) {
                    majorLimit = true;
                    break;
                }
            }
        }
        //检查学生的年级是否符合
        int stuGrade = stu.getGrade();
        if (mCompetitionDetail.getGrade_limited() == null || mCompetitionDetail.getGrade_limited().isEmpty() ||
                mCompetitionDetail.getGrade_limited().contains(stuGrade + "") ||
                mCompetitionDetail.getGrade_limited().isEmpty()) {
            gradeLimit = true;
        }

        if (!majorLimit) {
            TispToastFactory.getToast(this, "专业不符合要求", Toast.LENGTH_LONG).show();
            canApply = false;
        } else if (!gradeLimit) {
            TispToastFactory.getToast(this, "年级不符合要求", Toast.LENGTH_LONG).show();
            canApply = false;
        } else {
            canApply = true;
        }
        return canApply;
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
        String fileName = sdf.format(new java.util.Date()) + ".png";
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
        } else if (resultCode == CODE_SELECTED_TEAM) {
            mApplyType = CODE_SELECTED_TEAM;
            GroupInfo info = (GroupInfo) data.getSerializableExtra("data");
            tv_group.setText(info.getCaptainName() + "的小队");
            tv_group.setTag(info.getGroupID());
            tvTeacher.setText(info.getTeacherName());
            tvTeacher.setTag(info.getTeacherID() + "");
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

    /**
     * 选择学院的弹框
     */
    private void showChoosemAcademyWindow() {
        isHideShow = true;
        if (mAcademyWindow == null) {
            View academyView = View.inflate(this, R.layout.pop_view_choose_teacher, null);
            mAcademyWindow = new PopupWindow(academyView,
                    ViewGroup.LayoutParams.MATCH_PARENT, MyUtilss.getScreenHeight(this) / 3 * 2);
            mAcademyWindow.setFocusable(true);
            mAcademyWindow.setBackgroundDrawable(new BitmapDrawable());
            initAcademyView(academyView);
        }
        mAcademyWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isHideShow) {
                    hideBgShadow();
                }
            }
        });
        mAcademyWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        showBgShadow();
        mAcademyWindow.showAtLocation(btApply, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 初始化选择学院的弹框
     */
    private void initAcademyView(View contentView) {
        contentView.findViewById(R.id.choose_title).setVisibility(View.GONE);
        final ListView academyList = (ListView) contentView.findViewById(R.id.lv_content);
        //请求学院列表的数据
        String url = "CompetitionController/getAcademyList";
        MyHttpRequest request = new MyHttpRequest(this, url, null) {
            @Override
            public void sucessListener(String result) {
                AcademyList list = new Gson().fromJson(result, AcademyList.class);
                if (list.getSuccess()) {
                    academyList.setAdapter(new AcademyAdapter(list, GroupApplyActivity.this));
                }
            }
        };
        request.startRequestByGet();
        academyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Academy academy = (Academy) parent.getAdapter().getItem(position);
                isHideShow = false;
                mAcademyWindow.dismiss();
                showChooseDepartWindow(academy);
            }
        });

    }

    /**
     * 选择系的弹框
     */
    private void showChooseDepartWindow(Academy academy) {
        isHideShow = true;
        View departmentView = View.inflate(this, R.layout.pop_view_choose_teacher, null);
        mDepartmentWindow = new PopupWindow(departmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, MyUtilss.getScreenHeight(this) / 3 * 2);
        mDepartmentWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        mDepartmentWindow.setFocusable(true);
        mDepartmentWindow.setBackgroundDrawable(new BitmapDrawable());
        mDepartmentWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isHideShow) {
                    hideBgShadow();
                }
            }
        });
        initDepartmentView(departmentView, academy);
        mDepartmentWindow.showAtLocation(btApply, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 初始化选择系的弹框
     */
    private void initDepartmentView(View contentView, final Academy academy) {
        TextView tv_choose_title = (TextView) contentView.findViewById(R.id.choose_title);
        tv_choose_title.setText(academy.getA_name());
        final ListView departmenList = (ListView) contentView.findViewById(R.id.lv_content);
        //请求系列表的数据
        String url = "CompetitionController/getDepartmentList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("academyID", academy.getaID() + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                DepartmentList list = new Gson().fromJson(result, DepartmentList.class);
                if (list.getSuccess()) {
                    departmenList.setAdapter(new DepartmentAdapter(list, GroupApplyActivity.this));
                }
            }
        };
        request.startRequestByPost();
        departmenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department department = (Department) parent.getAdapter().getItem(position);
                showChooseTeacherWindow(academy, department);
                isHideShow = false;
                mDepartmentWindow.dismiss();
            }
        });

    }

    /**
     * 显示选择老师的弹框
     */
    private void showChooseTeacherWindow(Academy academy, Department department) {
        isHideShow = true;
        View teacherView = View.inflate(this, R.layout.pop_view_choose_teacher, null);
        mTeacherWindow = new PopupWindow(teacherView,
                ViewGroup.LayoutParams.MATCH_PARENT, MyUtilss.getScreenHeight(this) / 3 * 2);
        mTeacherWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        mTeacherWindow.setFocusable(true);
        mTeacherWindow.setBackgroundDrawable(new BitmapDrawable());
        mTeacherWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideBgShadow();
            }
        });
        initTeacherView(teacherView, academy, department);
        mTeacherWindow.showAtLocation(btApply, Gravity.BOTTOM, 0, 0);

    }

    /**
     * 初始化老师的弹框
     */
    private void initTeacherView(View contentView, Academy academy, Department department) {
        TextView tv_choose_title = (TextView) contentView.findViewById(R.id.choose_title);
        tv_choose_title.setText(academy.getA_name() + " " + department.getD_name());

        final ListView teacherList = (ListView) contentView.findViewById(R.id.lv_content);
        //请求系列表的数据
        String url = "CompetitionController/getTeacherList";
        Map<String, String> params = new HashMap<String, String>();
        params.put("departmentID", department.getdID() + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                TeacherList list = new Gson().fromJson(result, TeacherList.class);
                if (list.getSuccess()) {
                    teacherList.setAdapter(new TeacherAdapter(list, GroupApplyActivity.this));
                }
            }
        };
        request.startRequestByPost();
        teacherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = (Teacher) parent.getAdapter().getItem(position);
                mTeacherWindow.dismiss();
                tvTeacher.setText(teacher.getT_name());
                tvTeacher.setTag(teacher.getID());
            }
        });
    }

    /**
     * 背景阴影的显示
     */
    private void showBgShadow() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shadow_show);
        view_bg_shadow.startAnimation(animation);
        view_bg_shadow.setVisibility(View.VISIBLE);
    }

    /**
     * 背景阴影的隐藏
     */
    private void hideBgShadow() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shadow_hide);
        view_bg_shadow.startAnimation(animation);
        view_bg_shadow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (view_bg_shadow.getVisibility() == View.VISIBLE) {
            hideBgShadow();
        }
        super.onBackPressed();
    }


}


