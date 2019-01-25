package la.xiong.androidquick.demo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import la.xiong.androidquick.demo.injector.component.ApplicationComponent;
import la.xiong.androidquick.demo.injector.component.DaggerApplicationComponent;
import la.xiong.androidquick.demo.injector.module.ApplicationModule;
import la.xiong.androidquick.demo.module.db.greendao.DBManager;
import la.xiong.androidquick.network.retrofit.RetrofitManager;
import la.xiong.androidquick.tool.SpUtil;
import la.xiong.androidquick.tool.ToastUtil;
import spa.lyh.cn.statusbarlightmode.ImmersionConfiguration;
import spa.lyh.cn.statusbarlightmode.ImmersionMode;

/**
 * @author  ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
public class MyApplication extends Application {

    private static MyApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //get application
        if (INSTANCE == null) {
            INSTANCE = this;
        }
        //init ToastUtil
        ToastUtil.register(this);
        //init SpUtil
        SpUtil.init(this);
        //init greendao
        DBManager.getInstance().init(getApplicationContext());
        //init retrofit url
        RetrofitManager.initBaseUrl("http://gank.io/api/");
        //init immersion
        ImmersionConfiguration configuration = new ImmersionConfiguration.Builder(this)
                .enableImmersionMode(ImmersionConfiguration.ENABLE)
                .setColor(R.color.base_bg)//默认标题栏颜色
                .build();
        ImmersionMode.getInstance().init(configuration);
        //init AndroidUtilCode
        Utils.init(this);
        //init stetho
        Stetho.initializeWithDefaults(this);
    }

    public static synchronized MyApplication getInstance() {
        return INSTANCE;
    }

    //dagger2:get ApplicationComponent
    public static ApplicationComponent getApplicationComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(INSTANCE)).build();
    }

}
