package com.timecat.data.bmob.core;

import android.app.Application;
import android.content.Context;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.integration.ConfigModule;
import com.timecat.data.bmob.data.User;
import com.timecat.data.bmob.data.common.Action;
import com.timecat.data.bmob.data.common.Block;
import com.timecat.data.bmob.data.common.Block2Block;
import com.timecat.data.bmob.data.common.InterAction;
import com.timecat.data.bmob.data.common.User2User;
import com.timecat.data.bmob.data.game.OwnActivity;
import com.timecat.data.bmob.data.game.OwnCube;
import com.timecat.data.bmob.data.game.OwnItem;
import com.timecat.data.bmob.data.game.OwnTask;
import com.timecat.data.bmob.data.game.Pay;
import com.timecat.data.bmob.data.mail.OwnMail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import timber.log.Timber;

/**
 * ================================================ CommonSDK 的 GlobalConfiguration
 * 含有有每个组件都可公用的配置信息, 每个组件的 AndroidManifest 都应该声明此 ConfigModule
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by JessYan on 30/03/2018 17:16
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑

        lifecycles.add(0, new AppLifecycles() {

            @Override
            public void attachBaseContext(@NonNull Context base) {
            }

            @Override
            public void onCreate(@NonNull Application application) {
                //初始化Bmob
//                Timber.d("Bmob init start");
//                BmobConfig config = new BmobConfig.Builder(application)
//                        //设置appkey
//                        .setApplicationId(BuildConfig.BMOB_APP_ID)
//                        //请求超时时间（单位为秒）：默认15s
//                        .setConnectTimeout(10)
//                        //文件分片上传时每片的大小（单位字节），默认512*1024
//                        .setUploadBlockSize(1024 * 1024)
//                        //文件的过期时间(单位为秒)：默认1800s
//                        .setFileExpiration(1800)
//                        .build();
//                Bmob.initialize(config);
//                Timber.d("Bmob init end");
                Timber.d("LeanCloud init start");
                User.alwaysUseSubUserClass(User.class);
                AVObject.registerSubclass(Action.class);
                AVObject.registerSubclass(Block.class);
                AVObject.registerSubclass(Block2Block.class);
                AVObject.registerSubclass(User2User.class);
                AVObject.registerSubclass(InterAction.class);
                AVObject.registerSubclass(OwnCube.class);
                AVObject.registerSubclass(OwnItem.class);
                AVObject.registerSubclass(OwnMail.class);
                AVObject.registerSubclass(OwnTask.class);
                AVObject.registerSubclass(OwnActivity.class);
                AVObject.registerSubclass(Pay.class);

                String APP_ID = "lVumM4aviuXnmOCmyODnHaEs-MdYXbMMI";
                String APP_KEY = "Cy0x5uhPiNlFPCp5W8YWKKJw";
//                String serverURL = "https://app.timecat.online";
                AVOSCloud.initialize(application, APP_ID, APP_KEY);
//                AVOSCloud.initializeSecurely(application, APP_ID, null);
                Timber.d("LeanCloud init end");
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context,
            List<Application.ActivityLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectFragmentLifecycle(Context context,
            List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }

}
