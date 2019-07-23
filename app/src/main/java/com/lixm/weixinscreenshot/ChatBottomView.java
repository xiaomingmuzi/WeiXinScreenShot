//package com.lixm.weixinscreenshot;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.viewpager.widget.ViewPager;
//
//import com.lixm.weixinscreenshot.PhotoAlbums.MyAlbumActivity;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Describe:自定义底部输入框+表情View
// * <p>
// * Author: Lixm
// * Date: 2019/4/30
// */
//public class ChatBottomView extends LinearLayout implements View.OnClickListener, TextWatcher {
//
//    private static final int INTENT_FROM_IMAGE = 1001;
//    private static final int INTENT_FROM_PHONE = 1002;
//    private static final int INTENT_FROM_REDPACKAGE = 1003;
//    private static final int INTENT_FROM_REDIO = 1004;
//
//    public static final int Type_Live = 0;//图文直播室，专家(红包一对多)；
//    public static final int Type_Dicuss_Host = 1;//互动区，专家(无语音输入，无红包)；
//    public static final int Type_Dicuss_User = 2;//互动区，普通用户((无语音输入，无红包,无更多按钮显示发送))；
//    public static final int Type_Private = 3;//私聊界面(红包一对一)
//
//    private Context mContext;
//    private SharedPreferencesUtil mSpu;
//    private FrameLayout left_text_or_voice;//左边的输入文字还是语音布局
//    private ImageView text;//选择文字
//    private ImageView text1;//右侧键盘选择
//    private ImageView voice;//选择语音
//    //    private LinearLayout input;//文字输入框+表情的布局
//    private TextView voice_input;// 语音录入，按住说话
//    private EditText input;// 输入框
//    private ImageView biaoqing;
//    private TextView textview_to_send;//发送按钮
//    private ImageView more;//扩展功能
//
//    private ViewPager viewPager_chat;//表情viewpager
//    private List<GridView> mGridList = new ArrayList<GridView>();
//    private ExpressionViewPagerAdapter mExpressionVpAdapter; // 装载表情的Vp的Adapter
//
//    private LinearLayout ativelayout;
//    private LinearLayout picture;
//    private LinearLayout camera;
//    private LinearLayout redpakage;
//
//    private final String reg = "^[\u4e00-\u9fa5\u0020-\u007E\uFE30-\uFFA0。、……“”‘’《》——￥~]*$";
//    private Pattern pattern = Pattern.compile(reg);
//    private String camera_path;//相机地址
//    private String targetId, mChatType;
//
//    private ChatClickListener listener;
//    private InputMethodManager imm;
//    private int currentType = 0;
//
//    public ChatBottomView(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public ChatBottomView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public ChatBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        this.mContext = context;
//        mSpu = new SharedPreferencesUtil(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.chat_bottom_layout, this);
//        left_text_or_voice = findViewById(R.id.left_text_or_voice);
//        text = findViewById(R.id.text);
//        text.setOnClickListener(this);
//        text1 = findViewById(R.id.text1);
//        text1.setOnClickListener(this);
//        voice = findViewById(R.id.voice);
//        voice_input = findViewById(R.id.voice_input);
//        input = findViewById(R.id.input);
//        input.setBackground(UIUtils.getGradientDrawable(UIUtils.getColor(R.color.white),2));
//        biaoqing = findViewById(R.id.biaoqing);
//        textview_to_send = findViewById(R.id.textview_to_send);
//        more = findViewById(R.id.more);
//        ativelayout = findViewById(R.id.ativelayout);
//        picture = findViewById(R.id.picture);
//        camera = findViewById(R.id.camera);
//        redpakage = findViewById(R.id.redpakage);
//        viewPager_chat = findViewById(R.id.viewPager_chat);
//
//        setClick(biaoqing, text, textview_to_send, voice, more, picture, camera, redpakage);
//
//        input.addTextChangedListener(this);
//        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        initGridViews();
//    }
//
//    /**
//     * @param listener    回调接口
//     * @param currentType 0 图文直播室，专家(红包一对多)；
//     *                    1 互动区，专家(无语音输入，无红包)；
//     *                    2 互动区，普通用户((无语音输入，无红包,无更多按钮显示发送))；
//     *                    3 私聊界面(红包一对一)
//     */
//    public void setListener(ChatClickListener listener, int currentType) {
//        this.listener = listener;
//        this.currentType = currentType;
//        if (currentType == Type_Live) {//0图文直播室，只有专家身份展示
//            setViewVisible(VISIBLE, left_text_or_voice, voice, redpakage, biaoqing);
//            setViewVisible(GONE, text, voice_input, text1);
//        }
//        if (currentType == Type_Dicuss_Host) {//1 互动区，专家(无语音输入，无红包)；
//            setViewVisible(GONE, left_text_or_voice, voice_input, redpakage, text1);
//            setViewVisible(VISIBLE, biaoqing);
//        }
//        if (currentType == Type_Dicuss_User) {//2 互动区，普通用户((无语音输入，无红包,无更多按钮显示发送))；
//            setViewVisible(GONE, left_text_or_voice, redpakage, more, text1);
//            setViewVisible(VISIBLE, textview_to_send, biaoqing);
//        }
//
//        input.setText("");
//
//    }
//
//    private void setClick(View... views) {
//        for (View view : views) {
//            view.setOnClickListener(this);
//        }
//    }
//
//    /**
//     * 私聊界面发红包时使用两个参数
//     *
//     * @param targetID
//     * @param type
//     */
//    public void setTargetIDAndType(String targetID, String type) {
//        this.targetId = targetID;
//        this.mChatType = type;
//    }
//
//    /**
//     * 界面刷新时，隐藏底部表情ViewPager或者选择更多的Layout
//     */
//    public void refreshView() {
//        setViewVisible(View.GONE, viewPager_chat, ativelayout);
//    }
//
//    /**
//     * 语音输入控件，在相关的Activity中处理,需要添加onTouch监听
//     *
//     * @return
//     */
//    public TextView getVoice_input() {
//        return voice_input;
//    }
//
//    /**
//     * 外部需要设置onTouch,互动区专家设置hint（回复***）
//     *
//     * @return
//     */
//    public EditText getInput() {
//        return input;
//    }
//
//    public String getCameraPath() {
//        return camera_path;
//    }
//
//    /**
//     * 设置按钮的显示或者隐藏
//     *
//     * @param visible
//     * @param views
//     */
//    public void setViewVisible(int visible, View... views) {
//        for (View view : views) {
//            view.setVisibility(visible);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.textview_to_send://发送文字消息
//                String message = input.getText().toString().trim();
//                if (!TextUtils.isEmpty(message)) {
//                    listener.clickSend(input);
//                } else {
//                    FinancialToast.show(mContext, "请输入内容");
//                }
//                break;
//            case R.id.more: //更多模块
////                toggleInput();
//                hideSoftInput();
//                viewPager_chat.setVisibility(View.GONE);
//                if (ativelayout.getVisibility() == View.VISIBLE) {
//                    ativelayout.setVisibility(View.GONE);
//                } else {
//                    ativelayout.setVisibility(View.VISIBLE);
//                }
//                listener.scrollToBottom();
//                break;
//            case R.id.voice://录制音频按钮
////                toggleInput(mContext, text);
////                toggleInput();
//                hideSoftInput();
//                voice_input.setVisibility(View.VISIBLE);
//                voice.setVisibility(View.GONE);
//                text.setVisibility(View.VISIBLE);
//                text1.setVisibility(GONE);
//                biaoqing.setVisibility(VISIBLE);
//                textview_to_send.setVisibility(View.GONE);
//                viewPager_chat.setVisibility(View.GONE);
//                more.setVisibility(View.VISIBLE);
//                ativelayout.setVisibility(View.GONE);//图片,相机,短视频,红包,成员包裹的view
//                listener.scrollToBottom();
//                break;
//            case R.id.text1://右侧输入文字按钮
//                showSoftInput();
//                /**
//                 * 0 图文直播室，专家(红包一对多)；
//                 * 1 互动区，专家(无语音输入，无红包)；
//                 * 2 互动区，普通用户((无语音输入，无红包))；
//                 * 3 私聊界面(红包一对一)
//                 */
//                if (currentType == Type_Live || currentType == Type_Private) {
//                    left_text_or_voice.setVisibility(VISIBLE);
//                    voice.setVisibility(VISIBLE);
//                    text.setVisibility(GONE);
//                }
//                setViewVisible(GONE, ativelayout, viewPager_chat, text1, voice_input, text);
//                setViewVisible(VISIBLE, biaoqing);
//                String con = input.getText().toString();
//                if (!TextUtils.isEmpty(con) || currentType == Type_Dicuss_User) {
//                    textview_to_send.setVisibility(View.VISIBLE);
//                    more.setVisibility(View.GONE);
//                } else {
//                    textview_to_send.setVisibility(View.GONE);
//                    more.setVisibility(View.VISIBLE);
//                }
//                listener.scrollToBottom();
//                break;
//            case R.id.text: //左侧输入文字按钮
//                showSoftInput();
//                setViewVisible(GONE, ativelayout, viewPager_chat, text1, voice_input, text);
//                setViewVisible(VISIBLE, biaoqing, voice);
//                String input_text = input.getText().toString();
//                if (!TextUtils.isEmpty(input_text) || currentType == Type_Dicuss_User) {
//                    textview_to_send.setVisibility(View.VISIBLE);
//                    more.setVisibility(View.GONE);
//                } else {
//                    textview_to_send.setVisibility(View.GONE);
//                    more.setVisibility(View.VISIBLE);
//                }
//                listener.scrollToBottom();
//                break;
//            case R.id.biaoqing:
//                /**
//                 * 0 图文直播室，专家(红包一对多)；
//                 * 1 互动区，专家(无语音输入，无红包)；
//                 * 2 互动区，普通用户((无语音输入，无红包))；
//                 * 3 私聊界面(红包一对一)
//                 */
//                if (currentType == 0 || currentType == 3) {
//                    left_text_or_voice.setVisibility(VISIBLE);
//                    voice.setVisibility(VISIBLE);
//                    text.setVisibility(GONE);
//                }
//                setViewVisible(VISIBLE, text1, viewPager_chat);
//                setViewVisible(GONE, ativelayout, biaoqing,voice_input);
//                hideSoftInput();
//                break;
//            case R.id.picture:
//                intent = new Intent(mContext, MyAlbumActivity.class);
//                intent.putExtra("number", 1);
//                ((Activity) mContext).startActivityForResult(intent, INTENT_FROM_IMAGE);
//                ativelayout.setVisibility(View.GONE);
//                KSYMediaPlayerUtils.getInstense().stopPlayVideo();
//                break;
//            case R.id.camera:
//                if (CheckUtils.checkIsCameraUseable(mContext)) {
//                    String fileTarget = PARAM.IMAGECACHEPATH_SD;
//                    File file1 = new File(fileTarget);
//                    if (!file1.exists()) {
//                        try {
//                            file1.mkdirs();
//                        } catch (Exception e) {
//                            e.toString();
//                        }
//                    }
//                    camera_path = fileTarget + System.currentTimeMillis() + ".jpg";
//                    LogUtil.e("照相创建的文件保存路径_____" + camera_path);
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = createNewFile(camera_path);
//                    if (file != null) {
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                    }
//                    ((Activity) mContext).startActivityForResult(intent, INTENT_FROM_PHONE);
//                    ativelayout.setVisibility(View.GONE);
//                    KSYMediaPlayerUtils.getInstense().stopPlayVideo();
//                } else {
//                    FinancialToast.show(mContext, getResources().getString(R.string.camera_perssion));
//                }
//                break;
//            case R.id.redpakage:
//                //发红包
//                ativelayout.setVisibility(View.GONE);
//                if (mContext instanceof ChatLiveActivity) {
//                    KSYMediaPlayerUtils.getInstense().stopPlayVideo();
//                    Intent intent2 = new Intent(mContext, SendRedPickageActivity.class);
//                    intent2.putExtra("gid", mSpu.getGLP_ExpertID());
//                    intent2.putExtra("type", "1");
//                    intent2.putExtra("package_type", "4");
//                    ((Activity) mContext).startActivityForResult(intent2, INTENT_FROM_REDPACKAGE);
//                } else if (mContext instanceof ChatPrivateActivity) {
//                    Intent intent2 = new Intent(mContext, SendDLRedPickageActivity.class);
//                    intent2.putExtra("ToUserID", targetId);
//                    intent2.putExtra("type", "1");//标示从聊天界面发红包
//                    intent2.putExtra("chatType", mChatType);//0 标示单聊，1 标示提问回答
//                    ((Activity) mContext).startActivityForResult(intent2, INTENT_FROM_REDPACKAGE);
//                }
//                break;
//        }
//    }
//
//    /**
//     * 软键盘显示隐藏
//     */
//    public void showSoftInput() {
//        input.requestFocus();
//        imm.showSoftInput(input, 0);
//    }
//
//    /**
//     * 隐藏输入框
//     */
//    public void hideSoftInput() {
//        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
//    }
//
//    public static File createNewFile(String path) {
//        File file = new File(path);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                return null;
//            }
//        }
//        return file;
//    }
//
//    /**
//     * 初始化表情
//     */
//    private void initGridViews() {
//        mGridList = new ArrayList<>();
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        mGridList.clear();
//        for (int i = 0; i < 6; i++) {
//            final int j = i;
//            GridView gridView = (GridView) inflater.inflate(
//                    R.layout.gridview_qq_expression, null);
//            gridView.setAdapter(new EmotionGridViewAdapter(mContext, i));
//            mGridList.add(gridView);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    if (position == 20 || position == 40 || position == 60
//                            || position == 80 || position == 100
//                            || (j == 5 && position == 5)) {
//                        int selectionStart = input.getSelectionStart();
//                        String text = input.getText().toString();
//
//                        if (!TextUtils.isEmpty(text)) {
//                            String body = input.getText().toString();
//                            String tempStr = body.substring(0, selectionStart);
//                            int i = tempStr.lastIndexOf("]");// 获取最后一个表情的位置
//                            if (i == tempStr.length() - 1) {// 说明光标刚好停在这个表情之后
//                                int j = tempStr.lastIndexOf("[");// 将这两个之间的字符删掉
//                                input.getEditableText().delete(j,
//                                        selectionStart);
//                            } else {
//                                Matcher matcher = pattern.matcher(text);
//                                if (!matcher.matches()) {
//                                    new Thread(new Runnable() {
//
//                                        @Override
//                                        public void run() {
//                                            // TODO Auto-generated method stub
//                                            Instrumentation instrumentation = new Instrumentation();
//                                            instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DEL);
//                                        }
//                                    }) {
//                                    }.start();
//                                } else {
//                                    input.getEditableText().delete(
//                                            tempStr.length() - 1, selectionStart);
//                                }
//                            }
//                        }
//                    } else {
//                        String emotionString = Expressions.emojName[position
//                                + j * 20];
//                        String text = input.getText() + emotionString;
//                        SpannableString spannableString = ExpressionUtil2
//                                .getSpannableString(text, mContext);
//                        input.setText(spannableString);
//                        Editable b = input.getText();
//                        input.setSelection(b.length());
//                    }
//                }
//            });
//        }
//        mExpressionVpAdapter = new ExpressionViewPagerAdapter(mGridList);
//        viewPager_chat.setAdapter(mExpressionVpAdapter);
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        int number = s.length();
//        if (number != 0 || currentType == Type_Dicuss_User) {
//            textview_to_send.setVisibility(View.VISIBLE);
//            more.setVisibility(View.GONE);
//        } else {
//            textview_to_send.setVisibility(View.GONE);
//            more.setVisibility(View.VISIBLE);
//        }
//    }
//}
