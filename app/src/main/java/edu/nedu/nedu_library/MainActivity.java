package edu.nedu.nedu_library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

import edu.nedu.nedu_library.entity.BookInfo;
import edu.nedu.nedu_library.entity.UserInfo;
import edu.nedu.nedu_library.net.GetImgHttpUtil;
import edu.nedu.nedu_library.util.ToolUtil;
import edu.nedu.nedu_library.util.UserInfoUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private RadioGroup radioGroup;
    private EditText edTitle, edAuthor, edISBN, edPublishing, edSubject, edSearchNumber;
    private LinearLayout linearSearch,linearSelect,lyAdvancedSearch;
    private TextView btnAdvancedSearch;
    private EditText etSearchContent;
    private ImageButton btnSerach,btnASearch;
    private ImageView imgAvatar;
    private TextView tvName;

    private UserInfo userInfo;
    private String selectWay = "title";
    private BookInfo bookInfo;
    private boolean type;

    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static Uri tempUri;

    private View nav_headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        initView();     //初始化控件
        initEvent();    //初始化事件
        initData();     //初始化数据

        Intent intent = getIntent();
        boolean ping = intent.getBooleanExtra("ping", true);
        if (!ping) {
            Toast.makeText(mContext, "你和母舰失去联系", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ScanCode) {
            Intent intent = new Intent(MainActivity.this,ScaningActivity.class);
            intent.putExtra("u_id",userInfo.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_borrow) {
            intent = new Intent(MainActivity.this, ShowborrowedinfoActivity.class);
            intent.putExtra("u_id",userInfo.getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_reservation){
            intent = new Intent(MainActivity.this, ShowreservationinfoActivity.class);
            intent.putExtra("u_id",userInfo.getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_feedback) {
            intent = new Intent(MainActivity.this, FeedbackActivity.class);
            intent.putExtra("u_id",userInfo.getId());
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_FAQ) {
            intent = new Intent(MainActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_settings) {
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * activity声明周期 onCreate -> onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        userInfo = UserInfoUtil.getUserInfo(mContext);
        //设置名字
        tvName.setText(userInfo.getName());
        //清空搜索条件
        initData();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.selectTitle:
                selectWay = "title";
                break;
            case R.id.selectAuthor:
                selectWay = "author";
                break;
            case R.id.selectISBN:
                selectWay = "ISBN";
                break;
            case R.id.selectSubject:
                selectWay = "subject";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSerach:
                String str = etSearchContent.getText().toString();
                if (!str.equals("")) {
                    Intent intent = new Intent(MainActivity.this, ShowbookinfoActivity.class);
                    intent.putExtra("str", str);
                    intent.putExtra("way", selectWay);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "no content", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgAvatar://两个按钮实现相同跳转
                showChooseDialog();
                break;
            case R.id.tvName:
                Intent intent = new Intent(MainActivity.this, ShowuserinfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAdvancedSearch:
                type = !type;
                setData();
                setVisibility();
                break;
            case R.id.btnASearch:
                if (getData()) {
                    //Toast.makeText(mContext, bookInfo.toString(), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(MainActivity.this, ShowbookinfoActivity.class);
                    in.putExtra("str", bookInfo.toString());
                    in.putExtra("way", "all");
                    startActivity(in);
                } else {
                    Toast.makeText(mContext, "no content", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        edTitle = (EditText) findViewById(R.id.edTitle);
        edAuthor = (EditText) findViewById(R.id.edAuthor);
        edISBN = (EditText) findViewById(R.id.edISBN);
        edPublishing = (EditText) findViewById(R.id.edPublishing);
        edSubject = (EditText) findViewById(R.id.edSubject);
        edSearchNumber = (EditText) findViewById(R.id.edSearchNumber);
        radioGroup = (RadioGroup) findViewById(R.id.selectGroup);

        linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
        linearSelect = (LinearLayout) findViewById(R.id.linearSelect);
        lyAdvancedSearch = (LinearLayout) findViewById(R.id.lyAdvancedSearch);

        btnAdvancedSearch = (TextView) findViewById(R.id.btnAdvancedSearch);

        etSearchContent = (EditText) findViewById(R.id.etSearchContent);
        btnSerach = (ImageButton) findViewById(R.id.btnSerach);
        btnASearch = (ImageButton) findViewById(R.id.btnASearch);

        imgAvatar = (ImageView) nav_headerView.findViewById(R.id.imgAvatar);
        tvName = (TextView) nav_headerView.findViewById(R.id.tvName);
    }

    private void initEvent(){
        radioGroup.setOnCheckedChangeListener(this);
        btnAdvancedSearch.setOnClickListener(this);
        btnSerach.setOnClickListener(this);
        btnASearch.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        tvName.setOnClickListener(this);
    }

    private void initData(){
        userInfo = UserInfoUtil.getUserInfo(mContext);
        type = true;
        setData();
        setVisibility();
        //initData 头像特殊处理
        //1.先查看头像文件是否存在
        if (ToolUtil.fileIsExists(getExternalFilesDir("images").getAbsoluteFile() + "/" + userInfo.getId() + ".png")) {
            Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir("images").getAbsoluteFile() + "/" + userInfo.getId() + ".png");
            imgAvatar.setImageBitmap(bitmap);
        }
        //2.尝试下载最新头像文件
        String url = ToolUtil.url + "/data/userAvatar/" + userInfo.getId() + ".png";
        GetImgHttpUtil.getImg(setAvatar, url);
    }

    private void setVisibility() {
        if (type){
            linearSearch.setVisibility(View.VISIBLE);
            linearSelect.setVisibility(View.VISIBLE);
            lyAdvancedSearch.setVisibility(View.GONE);
            btnAdvancedSearch.setText("高级搜索>>");
        } else {
            linearSearch.setVisibility(View.GONE);
            linearSelect.setVisibility(View.GONE);
            lyAdvancedSearch.setVisibility(View.VISIBLE);
            btnAdvancedSearch.setText("<<收起");
        }
    }

    private void setData() {
        etSearchContent.setText("");
        edTitle.setText("");
        edAuthor.setText("");
        edISBN.setText("");
        edPublishing.setText("");
        edSubject.setText("");
        edSearchNumber.setText("");
        radioGroup.check(R.id.selectTitle);
        selectWay = "title";
    }

    private boolean getData() {
        boolean flag = false;
        bookInfo = new BookInfo();
        if (!edTitle.getText().toString().equals("")) {
            bookInfo.setTitle(edTitle.getText().toString());
            flag = true;
        }
        if (!edAuthor.getText().toString().equals("")) {
            bookInfo.setAuthor(edAuthor.getText().toString());
            flag = true;
        }
        if (!edISBN.getText().toString().equals("")) {
            bookInfo.setISBN(edISBN.getText().toString());
            flag = true;
        }
        if (!edPublishing.getText().toString().equals("")) {
            bookInfo.setPublishing(edPublishing.getText().toString());
            flag = true;
        }
        if (!edSubject.getText().toString().equals("")) {
            bookInfo.setSubject(edSubject.getText().toString());
            flag = true;
        }
        if (!edSearchNumber.getText().toString().equals("")) {
            bookInfo.setSearchNumber(edSearchNumber.getText().toString());
            flag = true;
        }
        if (!flag) {
            bookInfo = null;
        }
        return flag;
    }

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        //Toast.makeText(mContext, "本地照片", Toast.LENGTH_SHORT).show();
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        //Toast.makeText(mContext, "拍照", Toast.LENGTH_SHORT).show()
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(getExternalFilesDir("images"), "/" + userInfo.getId() + ".jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 数据uri
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        startPhotoZoom(uri, 1, 1, 160, 160);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     * @param aspectX
     * @param aspectY
     * @param width
     * @param height
     */
    private void startPhotoZoom(Uri uri, int aspectX, int aspectY, int width, int height) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        // public Intent setDataAndType(Uri data, String type); 数据Uri 数据类型type
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param data
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ToolUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            imgAvatar.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 上传数据到服务器
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ToolUtil.savePhoto(bitmap, getExternalFilesDir("images").getAbsolutePath(), String.valueOf(userInfo.getId()));
        Log.d("imagePath", imagePath);
        Log.d("getExternalFilesDir", getExternalFilesDir("images").getAbsoluteFile() + "");
        if(imagePath != null){
            fileUpload(imagePath);
        }
    }

    /**
     * 上传头像文件
     * 借助AsyncHttpClient 实现上传
     * @param filepath 要上传的文件path
     */
    private void fileUpload(String filepath){
        //使用开源Utils做上传操作
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Log.d("fileUpload", filepath);
        RequestParams params = new RequestParams();
        try {
            params.put(String.valueOf(userInfo.getId()), new File(filepath));
        } catch (FileNotFoundException e) {
            Log.d("fileUpload", "fail");
            e.printStackTrace();
        }
        //urlStr : 请求服务器的url
        String urlStr = ToolUtil.url + "/UploaderServlet";
        asyncHttpClient.post(urlStr, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "上传失败-1", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "上传失败-2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler setAvatar = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            //保存到手机上
            ToolUtil.savePhoto(bitmap, getExternalFilesDir("images").getAbsolutePath(), String.valueOf(userInfo.getId()));
            imgAvatar.setImageBitmap(bitmap);
        }
    };
}
